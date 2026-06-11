package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("user_badge")
public class UserBadge {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long userId; private String badgeName; private Boolean unlocked;
}
