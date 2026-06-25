package com.englishlearning.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("task_config")
public class TaskConfig {
    @Id(keyType = KeyType.Auto)
    private Long id;
    private String grade;
    private String taskType;
    private String taskTitle;
    private Integer taskTarget;
    private Integer sortOrder;
    private Boolean enabled;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
