package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @Table("users")
public class User {
    @Id(keyType = KeyType.Auto) private Long id;
    private String username; private String password; private String nickname;
    private String avatar; private String grade; private String studentNo;
    private Integer level; private String levelName; private Integer exp;
    private Integer expMax; private Integer totalScore; private Integer studyDays;
    private Integer streakDays; private Boolean isDeleted;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
