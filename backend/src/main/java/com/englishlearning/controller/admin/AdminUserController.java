package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.config.AdminContext;
import com.englishlearning.entity.SysAdmin;
import com.englishlearning.entity.SysAdminRole;
import com.englishlearning.mapper.SysAdminMapper;
import com.englishlearning.mapper.SysAdminRoleMapper;
import com.englishlearning.mapper.SysRoleMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.englishlearning.entity.table.SysAdminTableDef.SYS_ADMIN;
import static com.englishlearning.entity.table.SysAdminRoleTableDef.SYS_ADMIN_ROLE;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final SysAdminMapper adminMapper;
    private final SysAdminRoleMapper adminRoleMapper;
    private final SysRoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(
            SysAdminMapper adminMapper,
            SysAdminRoleMapper adminRoleMapper,
            SysRoleMapper roleMapper,
            PasswordEncoder passwordEncoder) {
        this.adminMapper = adminMapper;
        this.adminRoleMapper = adminRoleMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/list")
    public R<?> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname) {

        QueryWrapper qw = QueryWrapper.create()
                .where(SYS_ADMIN.IS_DELETED.eq(false));

        if (username != null && !username.isEmpty()) {
            qw.where(SYS_ADMIN.USERNAME.like(username));
        }
        if (nickname != null && !nickname.isEmpty()) {
            qw.where(SYS_ADMIN.NICKNAME.like(nickname));
        }

        qw.orderBy(SYS_ADMIN.CREATED_AT, false);

        Page<SysAdmin> page = adminMapper.paginate(Page.of(pageNum, pageSize), qw);

        // 为每个用户添加角色信息
        List<Map<String, Object>> userList = new java.util.ArrayList<>();
        for (SysAdmin admin : page.getRecords()) {
            Map<String, Object> userInfo = new java.util.HashMap<>();
            userInfo.put("id", admin.getId());
            userInfo.put("username", admin.getUsername());
            userInfo.put("nickname", admin.getNickname());
            userInfo.put("email", admin.getEmail());
            userInfo.put("phone", admin.getPhone());
            userInfo.put("status", admin.getStatus());
            userInfo.put("createdAt", admin.getCreatedAt());

            // 获取用户角色
            List<SysAdminRole> adminRoles = adminRoleMapper.selectListByQuery(
                    QueryWrapper.create().where(SYS_ADMIN_ROLE.ADMIN_ID.eq(admin.getId()))
            );
            List<Long> roleIds = adminRoles.stream().map(SysAdminRole::getRoleId).toList();
            userInfo.put("roleIds", roleIds);

            // 获取角色详情
            if (!roleIds.isEmpty()) {
                List<com.englishlearning.entity.SysRole> roles = roleMapper.selectListByIds(roleIds);
                userInfo.put("roles", roles.stream().map(r -> Map.of(
                        "id", r.getId(),
                        "roleName", r.getRoleName(),
                        "roleCode", r.getRoleCode()
                )).toList());
            } else {
                userInfo.put("roles", java.util.Collections.emptyList());
            }

            userList.add(userInfo);
        }

        return R.ok(Map.of(
                "list", userList,
                "total", page.getTotalRow(),
                "pageNum", page.getPageNumber(),
                "pageSize", page.getPageSize()
        ));
    }

    @PostMapping
    public R<?> create(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String nickname = (String) body.get("nickname");
        String email = (String) body.get("email");
        String phone = (String) body.get("phone");

        if (username == null || username.isEmpty()) {
            return R.fail("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return R.fail("密码不能为空");
        }
        if (nickname == null || nickname.isEmpty()) {
            return R.fail("昵称不能为空");
        }

        // 检查用户名是否已存在
        SysAdmin existing = adminMapper.selectOneByQuery(
                QueryWrapper.create().where(SYS_ADMIN.USERNAME.eq(username))
        );
        if (existing != null) {
            return R.fail("用户名已存在");
        }

        SysAdmin admin = new SysAdmin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setNickname(nickname);
        admin.setEmail(email);
        admin.setPhone(phone);
        admin.setStatus(1);
        admin.setIsDeleted(false);

        adminMapper.insert(admin);

        // 保存角色关联
        @SuppressWarnings("unchecked")
        List<Object> rawRoleIds = (List<Object>) body.get("roleIds");
        if (rawRoleIds != null && !rawRoleIds.isEmpty()) {
            List<Long> roleIds = rawRoleIds.stream()
                    .map(obj -> obj instanceof Long ? (Long) obj : Long.valueOf(obj.toString()))
                    .toList();
            saveAdminRoles(admin.getId(), roleIds);
        }

        return R.ok("创建成功");
    }

    @PutMapping("/{id}")
    public R<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        SysAdmin admin = adminMapper.selectOneById(id);
        if (admin == null) {
            return R.fail("用户不存在");
        }

        if (body.containsKey("nickname")) {
            admin.setNickname((String) body.get("nickname"));
        }
        if (body.containsKey("email")) {
            admin.setEmail((String) body.get("email"));
        }
        if (body.containsKey("phone")) {
            admin.setPhone((String) body.get("phone"));
        }
        if (body.containsKey("status")) {
            admin.setStatus(body.get("status") instanceof Integer ? (Integer) body.get("status") : Integer.parseInt(body.get("status").toString()));
        }
        if (body.containsKey("password") && body.get("password") != null) {
            String newPassword = (String) body.get("password");
            if (!newPassword.isEmpty()) {
                admin.setPassword(passwordEncoder.encode(newPassword));
            }
        }

        adminMapper.update(admin);

        // 更新角色关联
        @SuppressWarnings("unchecked")
        List<Object> rawRoleIds = (List<Object>) body.get("roleIds");
        if (rawRoleIds != null) {
            // 删除旧的角色关联
            adminRoleMapper.deleteByQuery(
                    QueryWrapper.create().where(SYS_ADMIN_ROLE.ADMIN_ID.eq(id))
            );
            // 转换并保存新的角色关联
            List<Long> roleIds = rawRoleIds.stream()
                    .map(obj -> obj instanceof Long ? (Long) obj : Long.valueOf(obj.toString()))
                    .toList();
            if (!roleIds.isEmpty()) {
                saveAdminRoles(id, roleIds);
            }
        }

        return R.ok("更新成功");
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        SysAdmin admin = adminMapper.selectOneById(id);
        if (admin == null) {
            return R.fail("用户不存在");
        }

        // 逻辑删除
        admin.setIsDeleted(true);
        adminMapper.update(admin);

        // 删除角色关联
        adminRoleMapper.deleteByQuery(
                QueryWrapper.create().where(SYS_ADMIN_ROLE.ADMIN_ID.eq(id))
        );

        return R.ok("删除成功");
    }

    @GetMapping("/{id}/roles")
    public R<?> getUserRoles(@PathVariable Long id) {
        List<SysAdminRole> adminRoles = adminRoleMapper.selectListByQuery(
                QueryWrapper.create().where(SYS_ADMIN_ROLE.ADMIN_ID.eq(id))
        );
        List<Long> roleIds = adminRoles.stream().map(SysAdminRole::getRoleId).toList();
        return R.ok(roleIds);
    }

    @PutMapping("/{id}/roles")
    public R<?> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        SysAdmin admin = adminMapper.selectOneById(id);
        if (admin == null) {
            return R.fail("用户不存在");
        }

        // 删除旧的角色关联
        adminRoleMapper.deleteByQuery(
                QueryWrapper.create().where(SYS_ADMIN_ROLE.ADMIN_ID.eq(id))
        );

        // 保存新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            saveAdminRoles(id, roleIds);
        }

        return R.ok("分配成功");
    }

    private void saveAdminRoles(Long adminId, List<Long> roleIds) {
        for (Long roleId : roleIds) {
            SysAdminRole adminRole = new SysAdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }
    }
}
