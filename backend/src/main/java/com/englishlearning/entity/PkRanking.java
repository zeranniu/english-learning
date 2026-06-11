package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @Table("pk_ranking")
public class PkRanking {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long userId; private Integer score; private LocalDateTime updatedAt;
}
