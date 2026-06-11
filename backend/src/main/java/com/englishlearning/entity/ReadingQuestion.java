package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("reading_question")
public class ReadingQuestion {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long passageId; private String questionText;
    private String optionA; private String optionB; private String optionC; private Integer correctOption;
    private Boolean isDeleted;
}
