package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.GrammarQuestion;
import com.englishlearning.mapper.GrammarQuestionMapper;
import com.englishlearning.service.StudySessionService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/grammar")
public class GrammarController {
    private final GrammarQuestionMapper grammarMapper;
    private final StudySessionService studySessionService;

    public GrammarController(GrammarQuestionMapper grammarMapper, StudySessionService studySessionService) {
        this.grammarMapper = grammarMapper;
        this.studySessionService = studySessionService;
    }

    @GetMapping("/questions")
    public R<?> getQuestions() {
        List<GrammarQuestion> questions = grammarMapper.selectAll();
        return R.ok(questions);
    }

    @PostMapping("/answer")
    public R<?> answerQuestion(@RequestBody Map<String, Object> body) {
        Long userId = UserContext.getCurrentUserId();

        // 安全获取参数
        Object questionIdObj = body.get("questionId");
        Long questionId = questionIdObj != null ? Long.valueOf(questionIdObj.toString()) : 1L;
        Object selectedIndexObj = body.get("selectedIndex");
        Integer selectedIndex = selectedIndexObj != null ? Integer.valueOf(selectedIndexObj.toString()) : 0;

        GrammarQuestion q = grammarMapper.selectOneById(questionId);
        boolean correct = q != null && q.getCorrectOption() != null && q.getCorrectOption().equals(selectedIndex);

        Map<String, Object> data = new HashMap<>();
        data.put("correct", correct);
        data.put("explanation", q != null ? q.getExplanation() : "");
        return R.ok(data);
    }

    @PostMapping("/complete-session")
    public R<?> completeSession(@RequestBody Map<String, Object> body) {
        Long userId = UserContext.getCurrentUserId();
        int durationMinutes = body.get("durationMinutes") != null ? Integer.valueOf(body.get("durationMinutes").toString()) : 1;
        int score = body.get("score") != null ? Integer.valueOf(body.get("score").toString()) : 0;

        studySessionService.recordStudySession(userId, durationMinutes, score);
        return R.ok(Map.of("message", "学习会话已记录", "durationMinutes", durationMinutes, "score", score));
    }
}
