package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("listening_lesson")
public class ListeningLesson {
    @Id(keyType = KeyType.Auto) private Long id;
    private String title; private String duration; private String audioUrl; private String grade; private Boolean isDeleted;
}
