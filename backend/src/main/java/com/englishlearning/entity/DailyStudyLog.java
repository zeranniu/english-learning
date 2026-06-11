package com.englishlearning.entity;
import com.mybatisflex.annotation.*;
import lombok.Data;
import java.time.LocalDate;
@Data @Table("daily_study_log")
public class DailyStudyLog {
    @Id(keyType = KeyType.Auto) private Long id;
    private Long userId; private LocalDate studyDate; private Integer studyMinutes;
    private Integer tasksCompleted; private Integer weekScore; private Integer weekRank;
}
