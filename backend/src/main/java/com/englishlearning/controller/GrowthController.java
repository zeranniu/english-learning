package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.User;
import com.englishlearning.entity.UserBadge;
import com.englishlearning.mapper.UserMapper;
import com.englishlearning.mapper.UserBadgeMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/growth")
public class GrowthController {
    private final UserMapper userMapper;
    private final UserBadgeMapper badgeMapper;

    public GrowthController(UserMapper userMapper, UserBadgeMapper badgeMapper) {
        this.userMapper = userMapper;
        this.badgeMapper = badgeMapper;
    }

    @GetMapping("/info")
    public R<?> getGrowthInfo() {
        Long userId = UserContext.getCurrentUserId();
        User user = userMapper.selectOneById(userId);
        List<UserBadge> badges = badgeMapper.selectListByQuery(
                QueryWrapper.create().where("user_id = " + userId));
        Map<String, Object> data = new HashMap<>();
        data.put("level", user.getLevel());
        data.put("levelName", user.getLevelName());
        data.put("exp", user.getExp());
        data.put("expMax", user.getExpMax());
        data.put("badges", badges);
        return R.ok(data);
    }
}
