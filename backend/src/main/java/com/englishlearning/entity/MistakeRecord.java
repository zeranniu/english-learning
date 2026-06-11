package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @Table("mistake_record")
public class MistakeRecord {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long userId; private String category; private String title;
    private String errorInfo; private String tag; private LocalDateTime createdAt;
}
