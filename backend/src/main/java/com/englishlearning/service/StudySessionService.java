package com.englishlearning.service;

import com.englishlearning.entity.DailyStudyLog;
import com.englishlearning.entity.User;
import com.englishlearning.mapper.DailyStudyLogMapper;
import com.englishlearning.mapper.UserMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.englishlearning.entity.table.DailyStudyLogTableDef.DAILY_STUDY_LOG;
import static com.englishlearning.entity.table.UserTableDef.USER;

@Service
public class StudySessionService {
    private final DailyStudyLogMapper dailyLogMapper;
    private final UserMapper userMapper;

    public StudySessionService(DailyStudyLogMapper dailyLogMapper, UserMapper userMapper) {
        this.dailyLogMapper = dailyLogMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public void recordStudySession(Long userId, int minutes, int score) {
        // 更新或创建今日学习记录
        DailyStudyLog log = dailyLogMapper.selectOneByQuery(
                QueryWrapper.create()
                        .where(DAILY_STUDY_LOG.USER_ID.eq(userId))
                        .and(DAILY_STUDY_LOG.STUDY_DATE.eq(LocalDate.now()))
        );

        if (log == null) {
            log = new DailyStudyLog();
            log.setUserId(userId);
            log.setStudyDate(LocalDate.now());
            log.setStudyMinutes(minutes);
            log.setTasksCompleted(1);
            log.setWeekScore(score);
            dailyLogMapper.insert(log);
        } else {
            log.setStudyMinutes(log.getStudyMinutes() + minutes);
            log.setTasksCompleted(log.getTasksCompleted() + 1);
            log.setWeekScore(log.getWeekScore() + score);
            dailyLogMapper.update(log);
        }

        // 更新用户总积分和学习天数
        User user = userMapper.selectOneById(userId);
        if (user != null) {
            user.setTotalScore(user.getTotalScore() + score);
            user.setExp(user.getExp() + score / 10);
            userMapper.update(user);
        }
    }

    @Transactional
    public void completeTask(Long userId) {
        DailyStudyLog log = dailyLogMapper.selectOneByQuery(
                QueryWrapper.create()
                        .where(DAILY_STUDY_LOG.USER_ID.eq(userId))
                        .and(DAILY_STUDY_LOG.STUDY_DATE.eq(LocalDate.now()))
        );

        if (log == null) {
            log = new DailyStudyLog();
            log.setUserId(userId);
            log.setStudyDate(LocalDate.now());
            log.setStudyMinutes(0);
            log.setTasksCompleted(1);
            log.setWeekScore(0);
            dailyLogMapper.insert(log);
        } else {
            log.setTasksCompleted(log.getTasksCompleted() + 1);
            dailyLogMapper.update(log);
        }
    }
}
