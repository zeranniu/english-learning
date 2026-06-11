package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("vocab_word")
public class VocabWord {
    @Id(keyType = KeyType.Auto) private Long id;
    private String word; private String phonetic; private String translation;
    private String icon; private String iconColor; private String grade;
    private Boolean isDeleted;
}
