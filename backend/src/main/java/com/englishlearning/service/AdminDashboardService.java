package com.englishlearning.service;

import com.englishlearning.entity.*;
import com.englishlearning.mapper.*;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.englishlearning.entity.table.DailyStudyLogTableDef.DAILY_STUDY_LOG;
import static com.englishlearning.entity.table.MistakeRecordTableDef.MISTAKE_RECORD;
import static com.englishlearning.entity.table.UserTableDef.USER;

@Service
public class AdminDashboardService {
    private final UserMapper userMapper;
    private final DailyStudyLogMapper logMapper;
    private final MistakeRecordMapper mistakeMapper;

    public AdminDashboardService(UserMapper userMapper, DailyStudyLogMapper logMapper, MistakeRecordMapper mistakeMapper) {
        this.userMapper = userMapper;
        this.logMapper = logMapper;
        this.mistakeMapper = mistakeMapper;
    }

    public Map<String, Object> getOverview() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", userMapper.selectCountByQuery(QueryWrapper.create().where(USER.IS_DELETED.eq(false))));
        data.put("todayLogs", logMapper.selectCountByQuery(
                QueryWrapper.create().where(DAILY_STUDY_LOG.STUDY_DATE.eq(LocalDate.now()))));
        data.put("totalMistakes", mistakeMapper.selectCountByQuery(QueryWrapper.create()));
        return data;
    }

    public List<Map<String, Object>> getRecentLogs(int days) {
        LocalDate start = LocalDate.now().minusDays(days);
        List<DailyStudyLog> logs = logMapper.selectListByQuery(
                QueryWrapper.create()
                        .where(DAILY_STUDY_LOG.STUDY_DATE.ge(start))
                        .orderBy(DAILY_STUDY_LOG.STUDY_DATE, true));
        List<Map<String, Object>> result = new ArrayList<>();
        for (DailyStudyLog log : logs) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", log.getStudyDate().toString());
            item.put("studyMinutes", log.getStudyMinutes());
            item.put("tasksCompleted", log.getTasksCompleted());
            result.add(item);
        }
        return result;
    }

    public List<Map<String, Object>> getTopMistakes(int limit) {
        // 简单统计：按 title 分组计数
        List<MistakeRecord> records = mistakeMapper.selectListByQuery(
                QueryWrapper.create().orderBy(MISTAKE_RECORD.CREATED_AT, false));
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (MistakeRecord r : records) {
            countMap.merge(r.getTitle(), 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        countMap.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(limit)
                .forEach(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("title", e.getKey());
                    item.put("count", e.getValue());
                    result.add(item);
                });
        return result;
    }

    public List<User> getTopStudents(int limit) {
        return userMapper.selectListByQuery(
                QueryWrapper.create().where(USER.IS_DELETED.eq(false)).orderBy(USER.TOTAL_SCORE, false).limit(limit));
    }
}
