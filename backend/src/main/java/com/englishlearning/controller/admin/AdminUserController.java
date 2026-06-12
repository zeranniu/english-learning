package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.config.AdminContext;
import com.englishlearning.entity.SysAdmin;
import com.englishlearning.mapper.SysAdminMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.englishlearning.entity.table.SysAdminTableDef.SYS_ADMIN;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final SysAdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(SysAdminMapper adminMapper, PasswordEncoder passwordEncoder) {
        this.adminMapper = adminMapper;
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

        return R.ok(Map.of(
                "list", page.getRecords(),
                "total", page.getTotalRow(),
                "pageNum", page.getPageNumber(),
                "pageSize", page.getPageSize()
        ));
    }

    @PostMapping
    public R<?> create(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String nickname = body.get("nickname");
        String email = body.get("email");
        String phone = body.get("phone");

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
            admin.setStatus((Integer) body.get("status"));
        }
        if (body.containsKey("password") && body.get("password") != null) {
            String newPassword = (String) body.get("password");
            if (!newPassword.isEmpty()) {
                admin.setPassword(passwordEncoder.encode(newPassword));
            }
        }

        adminMapper.update(admin);

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

        return R.ok("删除成功");
    }
}
