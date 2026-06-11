package com.englishlearning.dto;

import lombok.Data;

@Data
public class ReminderToggleRequest {
    private Long id;
    private Boolean enabled;
}
