package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("listening_question")
public class ListeningQuestion {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long lessonId; private String questionText;
    private String optionA; private String optionB; private String optionC;
    private String correctOption; private String explanation;
    private Boolean isDeleted;
}
