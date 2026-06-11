package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.Notification;
import com.englishlearning.mapper.NotificationMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.englishlearning.entity.table.NotificationTableDef.NOTIFICATION;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("/list")
    public R<?> getNotifications() {
        Long userId = UserContext.getCurrentUserId();
        List<Notification> notifications = notificationMapper.selectListByQuery(
                QueryWrapper.create().where(NOTIFICATION.USER_ID.eq(userId))
                        .or(NOTIFICATION.USER_ID.isNull())
                        .orderBy(NOTIFICATION.CREATED_AT, false));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Notification n : notifications) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", n.getId());
            item.put("title", n.getTitle());
            item.put("desc", n.getDescription());
            item.put("time", n.getCreatedAt() != null ? n.getCreatedAt().toString() : "");
            item.put("read", n.getIsRead());
            item.put("icon", n.getIcon());
            result.add(item);
        }
        return R.ok(result);
    }

    @PutMapping("/{id}/read")
    public R<?> markAsRead(@PathVariable Long id) {
        Notification n = notificationMapper.selectOneById(id);
        if (n != null) {
            n.setIsRead(true);
            notificationMapper.update(n);
        }
        return R.ok();
    }
}
