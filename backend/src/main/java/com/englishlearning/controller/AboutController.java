package com.englishlearning.controller;

import com.englishlearning.common.R;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/about")
public class AboutController {

    @GetMapping("/info")
    public R<?> getInfo() {
        List<Map<String, String>> items = List.of(
                Map.of("label", "版本号", "value", "v1.0.0"),
                Map.of("label", "适用年龄", "value", "6-12岁"),
                Map.of("label", "开发团队", "value", "少儿英语学习团队")
        );
        return R.ok(items);
    }
}
