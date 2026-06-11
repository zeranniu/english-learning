package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("reading_vocab")
public class ReadingVocab {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long passageId; private String word; private String meaning;
    private Boolean isDeleted;
}
