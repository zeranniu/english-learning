package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
@Data @Table("study_reminder")
public class StudyReminder {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long userId; private String label; private String reminderTime; private Boolean enabled;
}
