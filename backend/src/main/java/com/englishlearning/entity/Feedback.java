package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @Table("feedback")
public class Feedback {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long userId; private String feedbackType; private String content;
    private String contact; private LocalDateTime createdAt;
}
