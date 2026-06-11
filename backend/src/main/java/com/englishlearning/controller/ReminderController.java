package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.dto.ReminderToggleRequest;
import com.englishlearning.entity.StudyReminder;
import com.englishlearning.mapper.StudyReminderMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminders")
public class ReminderController {
    private final StudyReminderMapper reminderMapper;

    public ReminderController(StudyReminderMapper reminderMapper) {
        this.reminderMapper = reminderMapper;
    }

    @GetMapping("/list")
    public R<?> getReminders() {
        Long userId = UserContext.getCurrentUserId();
        List<StudyReminder> reminders = reminderMapper.selectListByQuery(
                QueryWrapper.create().where("user_id = " + userId));
        return R.ok(reminders);
    }

    @PutMapping("/toggle")
    public R<?> toggleReminder(@RequestBody ReminderToggleRequest req) {
        StudyReminder reminder = reminderMapper.selectOneById(req.getId());
        if (reminder != null) {
            reminder.setEnabled(req.getEnabled());
            reminderMapper.update(reminder);
        }
        return R.ok();
    }
}
