package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @Table("notification")
public class Notification {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long userId; private String title; private String description;
    private String icon; private Boolean isRead; private LocalDateTime createdAt;
}
