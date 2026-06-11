package com.englishlearning.controller.admin;

import com.englishlearning.common.AdminJwtUtil;
import com.englishlearning.common.R;
import com.englishlearning.config.AdminContext;
import com.englishlearning.dto.AdminLoginRequest;
import com.englishlearning.dto.ChangePasswordRequest;
import com.englishlearning.entity.SysAdmin;
import com.englishlearning.entity.SysRole;
import com.englishlearning.mapper.SysAdminMapper;
import com.englishlearning.mapper.SysRoleMapper;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.englishlearning.entity.table.SysAdminTableDef.SYS_ADMIN;

@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController {
    private final SysAdminMapper adminMapper;
    private final SysRoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminJwtUtil adminJwtUtil;

    public AdminAuthController(SysAdminMapper adminMapper, SysRoleMapper roleMapper,
                               PasswordEncoder passwordEncoder, AdminJwtUtil adminJwtUtil) {
        this.adminMapper = adminMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
        this.adminJwtUtil = adminJwtUtil;
    }

    @PostMapping("/login")
    public R<?> login(@RequestBody AdminLoginRequest req, HttpServletRequest httpRequest) {
        SysAdmin admin = adminMapper.selectOneByQuery(
                QueryWrapper.create().where(SYS_ADMIN.USERNAME.eq(req.getUsername())));
        if (admin == null || !passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
            return R.fail("用户名或密码错误");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            return R.fail("账号已被禁用");
        }

        // 更新登录信息
        admin.setLastLoginAt(LocalDateTime.now());
        admin.setLastLoginIp(getClientIp(httpRequest));
        adminMapper.update(admin);

        // 查询角色
        List<SysRole> roles = roleMapper.selectByAdminId(admin.getId());
        List<String> roleCodes = roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList());

        // 生成 Token
        String token = adminJwtUtil.generateToken(admin.getId(), admin.getUsername(), roleCodes);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("expiresIn", adminJwtUtil.getExpiration() / 1000);
        data.put("adminId", admin.getId());
        data.put("username", admin.getUsername());
        data.put("nickname", admin.getNickname());
        data.put("avatar", admin.getAvatar());
        data.put("roles", roleCodes);
        return R.ok(data);
    }

    @GetMapping("/info")
    public R<?> getInfo() {
        Long adminId = AdminContext.getCurrentAdminId();
        if (adminId == null) return R.fail("未登录");
        SysAdmin admin = adminMapper.selectOneById(adminId);
        if (admin == null) return R.fail("管理员不存在");
        List<SysRole> roles = roleMapper.selectByAdminId(adminId);
        Map<String, Object> data = new HashMap<>();
        data.put("id", admin.getId());
        data.put("username", admin.getUsername());
        data.put("nickname", admin.getNickname());
        data.put("avatar", admin.getAvatar());
        data.put("email", admin.getEmail());
        data.put("phone", admin.getPhone());
        data.put("roles", roles.stream().map(r -> Map.of("id", r.getId(), "roleName", r.getRoleName(), "roleCode", r.getRoleCode())).collect(Collectors.toList()));
        return R.ok(data);
    }

    @PutMapping("/password")
    public R<?> changePassword(@RequestBody ChangePasswordRequest req) {
        Long adminId = AdminContext.getCurrentAdminId();
        if (adminId == null) return R.fail("未登录");
        SysAdmin admin = adminMapper.selectOneById(adminId);
        if (!passwordEncoder.matches(req.getOldPassword(), admin.getPassword())) {
            return R.fail("原密码错误");
        }
        admin.setPassword(passwordEncoder.encode(req.getNewPassword()));
        adminMapper.update(admin);
        return R.ok("密码修改成功");
    }

    @PostMapping("/logout")
    public R<?> logout() {
        return R.ok("退出成功");
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
        return ip;
    }
}
