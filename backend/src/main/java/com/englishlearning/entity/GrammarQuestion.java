package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("grammar_question")
public class GrammarQuestion {
    @Id(keyType = KeyType.Auto) private Long id;
    private String questionText; private String optionA; private String optionB;
    private String optionC; private String optionD; private Integer correctOption;
    private String explanation; private String grade;
    private Boolean isDeleted;
}
