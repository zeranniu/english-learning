package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @Table("user_vocab_progress")
public class UserVocabProgress {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long userId; private Long wordId; private Boolean known; private LocalDateTime learnedAt;
}
