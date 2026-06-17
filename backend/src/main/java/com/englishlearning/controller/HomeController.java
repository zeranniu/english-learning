package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.*;
import com.englishlearning.mapper.*;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

import static com.englishlearning.entity.table.DailyStudyLogTableDef.DAILY_STUDY_LOG;

@RestController
@RequestMapping("/home")
public class HomeController {
    private final UserMapper userMapper;
    private final DailyStudyLogMapper dailyLogMapper;
    private final UserBadgeMapper badgeMapper;

    public HomeController(UserMapper userMapper, DailyStudyLogMapper dailyLogMapper, UserBadgeMapper badgeMapper) {
        this.userMapper = userMapper;
        this.dailyLogMapper = dailyLogMapper;
        this.badgeMapper = badgeMapper;
    }

    @GetMapping("/stats")
    public R<?> getStats() {
        Long userId = UserContext.getCurrentUserId();
        User user = userMapper.selectOneById(userId);
        DailyStudyLog log = dailyLogMapper.selectOneByQuery(
                QueryWrapper.create().where(DAILY_STUDY_LOG.USER_ID.eq(userId))
                        .and(DAILY_STUDY_LOG.STUDY_DATE.eq(LocalDate.now())));

        Map<String, Object> data = new HashMap<>();
        data.put("nickname", user != null ? user.getNickname() : "用户");
        data.put("grade", user != null ? user.getGrade() : "");
        data.put("streakDays", user != null && user.getStreakDays() != null ? user.getStreakDays() : 0);
        data.put("studyMinutes", log != null && log.getStudyMinutes() != null ? log.getStudyMinutes() : 0);
        data.put("tasksCompleted", log != null && log.getTasksCompleted() != null ? log.getTasksCompleted() : 0);
        data.put("weekScore", log != null && log.getWeekScore() != null ? log.getWeekScore() : 0);
        data.put("weekRank", log != null && log.getWeekRank() != null ? log.getWeekRank() : 0);
        data.put("level", user != null && user.getLevel() != null ? user.getLevel() : 1);
        data.put("levelName", user != null ? user.getLevelName() : "新手");
        data.put("exp", user != null && user.getExp() != null ? user.getExp() : 0);
        data.put("expMax", user != null && user.getExpMax() != null ? user.getExpMax() : 100);
        return R.ok(data);
    }

    @GetMapping("/tasks")
    public R<?> getTasks() {
        Long userId = UserContext.getCurrentUserId();
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.add(Map.of("id", 1, "title", "学习10个单词", "done", true, "progress", "10/10", "target", "/pages/vocab/vocab"));
        tasks.add(Map.of("id", 2, "title", "完成1篇听力", "done", true, "progress", "1/1", "target", "/pages/listening/listening"));
        tasks.add(Map.of("id", 3, "title", "完成1篇阅读", "done", false, "progress", "0/1", "target", "/pages/reading/reading"));
        return R.ok(tasks);
    }

    @GetMapping("/badges")
    public R<?> getBadges() {
        Long userId = UserContext.getCurrentUserId();
        List<UserBadge> badges = badgeMapper.selectListByQuery(
                QueryWrapper.create().where("user_id = " + userId));
        return R.ok(badges);
    }
}
