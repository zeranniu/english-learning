package com.englishlearning.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private String feedbackType;
    private String content;
    private String contact;
}
