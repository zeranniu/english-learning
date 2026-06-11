package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.dto.FeedbackRequest;
import com.englishlearning.entity.Feedback;
import com.englishlearning.mapper.FeedbackMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackMapper feedbackMapper;

    public FeedbackController(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    @PostMapping("/submit")
    public R<?> submitFeedback(@RequestBody FeedbackRequest req) {
        Long userId = UserContext.getCurrentUserId();
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setFeedbackType(req.getFeedbackType());
        feedback.setContent(req.getContent());
        feedback.setContact(req.getContact());
        feedbackMapper.insert(feedback);
        return R.ok("反馈提交成功，感谢！");
    }
}
