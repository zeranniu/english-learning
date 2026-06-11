package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("reading_passage")
public class ReadingPassage {
    @Id(keyType = KeyType.Auto) private Long id;
    private String title; private String content; private Integer difficulty;
    private Integer suggestedMinutes; private String grade;
    private Boolean isDeleted;
}
