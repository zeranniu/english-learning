package com.englishlearning.controller;

import com.englishlearning.common.JwtUtil;
import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.dto.LoginRequest;
import com.englishlearning.dto.RegisterRequest;
import com.englishlearning.entity.User;
import com.englishlearning.mapper.UserMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.englishlearning.entity.table.UserTableDef.USER;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginRequest req) {
        User user = userMapper.selectOneByQuery(
                QueryWrapper.create().where(USER.USERNAME.eq(req.getUsername())));
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return R.fail("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("expiresIn", jwtUtil.getExpiration() / 1000);
        data.put("userId", user.getId());
        data.put("nickname", user.getNickname());
        return R.ok(data);
    }

    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterRequest req) {
        User existing = userMapper.selectOneByQuery(
                QueryWrapper.create().where(USER.USERNAME.eq(req.getUsername())));
        if (existing != null) {
            return R.fail("用户名已存在");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setNickname(req.getNickname() != null ? req.getNickname() : req.getUsername());
        user.setGrade("三年级");
        user.setStudentNo("");
        user.setLevel(1);
        user.setLevelName("英语新手");
        user.setExp(0);
        user.setExpMax(100);
        user.setTotalScore(0);
        user.setStudyDays(0);
        user.setStreakDays(0);
        user.setIsDeleted(false);
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userMapper.insert(user);
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("expiresIn", jwtUtil.getExpiration() / 1000);
        return R.ok(data);
    }

    @GetMapping("/profile")
    public R<?> getProfile() {
        Long userId = UserContext.getCurrentUserId();
        User user = userMapper.selectOneById(userId);
        if (user == null) return R.fail("用户不存在");
        user.setPassword(null);
        return R.ok(user);
    }

    @PutMapping("/profile")
    public R<?> updateProfile(@RequestBody Map<String, String> body) {
        Long userId = UserContext.getCurrentUserId();
        User user = userMapper.selectOneById(userId);
        if (user == null) return R.fail("用户不存在");
        if (body.containsKey("nickname")) user.setNickname(body.get("nickname"));
        if (body.containsKey("avatar")) user.setAvatar(body.get("avatar"));
        userMapper.update(user);
        user.setPassword(null);
        return R.ok(user);
    }
}
