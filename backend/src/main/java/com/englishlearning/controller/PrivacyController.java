package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.User;
import com.englishlearning.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/privacy")
public class PrivacyController {

    @GetMapping("/settings")
    public R<?> getSettings() {
        List<Map<String, Object>> settings = List.of(
                Map.of("label", "个人资料公开", "desc", "其他用户可以看到你的昵称和头像", "enabled", true),
                Map.of("label", "学习数据共享", "desc", "允许将学习数据同步至云端", "enabled", false),
                Map.of("label", "消息推送", "desc", "接收系统通知和学习提醒", "enabled", true)
        );
        return R.ok(settings);
    }
}
