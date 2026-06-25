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
import static com.englishlearning.entity.table.TaskConfigTableDef.TASK_CONFIG;

@RestController
@RequestMapping("/home")
public class HomeController {
    private final UserMapper userMapper;
    private final DailyStudyLogMapper dailyLogMapper;
    private final UserBadgeMapper badgeMapper;
    private final TaskConfigMapper taskConfigMapper;

    public HomeController(UserMapper userMapper, DailyStudyLogMapper dailyLogMapper, UserBadgeMapper badgeMapper, TaskConfigMapper taskConfigMapper) {
        this.userMapper = userMapper;
        this.dailyLogMapper = dailyLogMapper;
        this.badgeMapper = badgeMapper;
        this.taskConfigMapper = taskConfigMapper;
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
        User user = userMapper.selectOneById(userId);
        String grade = user != null ? user.getGrade() : "三年级";

        // 获取今日学习记录
        DailyStudyLog todayLog = dailyLogMapper.selectOneByQuery(
                QueryWrapper.create()
                        .where(DAILY_STUDY_LOG.USER_ID.eq(userId))
                        .and(DAILY_STUDY_LOG.STUDY_DATE.eq(LocalDate.now()))
        );
        int tasksCompleted = todayLog != null && todayLog.getTasksCompleted() != null ? todayLog.getTasksCompleted() : 0;

        // 从数据库获取该年级的任务配置
        List<TaskConfig> taskConfigs = taskConfigMapper.selectListByQuery(
                QueryWrapper.create()
                        .where(TASK_CONFIG.GRADE.eq(grade))
                        .and(TASK_CONFIG.ENABLED.eq(true))
                        .and(TASK_CONFIG.IS_DELETED.eq(false))
                        .orderBy(TASK_CONFIG.SORT_ORDER, true)
        );

        // 构建任务列表
        List<Map<String, Object>> tasks = new ArrayList<>();
        int taskIndex = 0;
        for (TaskConfig config : taskConfigs) {
            boolean isDone = tasksCompleted > taskIndex;
            Map<String, Object> task = new HashMap<>();
            task.put("id", config.getId());
            task.put("title", config.getTaskTitle());
            task.put("done", isDone);
            task.put("progress", (isDone ? config.getTaskTarget() : 0) + "/" + config.getTaskTarget());
            task.put("target", getTargetUrl(config.getTaskType()));
            tasks.add(task);
            taskIndex++;
        }

        return R.ok(tasks);
    }

    private String getTargetUrl(String taskType) {
        if (taskType == null) return "/pages/index/index";
        return switch (taskType) {
            case "vocab" -> "/pages/vocab/vocab";
            case "listening" -> "/pages/listening/listening";
            case "reading" -> "/pages/reading/reading";
            case "grammar" -> "/pages/grammar/grammar";
            default -> "/pages/index/index";
        };
    }

    @GetMapping("/badges")
    public R<?> getBadges() {
        Long userId = UserContext.getCurrentUserId();
        List<UserBadge> badges = badgeMapper.selectListByQuery(
                QueryWrapper.create().where("user_id = " + userId));
        return R.ok(badges);
    }
}
