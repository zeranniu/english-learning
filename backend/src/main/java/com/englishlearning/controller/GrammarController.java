package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.entity.GrammarQuestion;
import com.englishlearning.mapper.GrammarQuestionMapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/grammar")
public class GrammarController {
    private final GrammarQuestionMapper grammarMapper;

    public GrammarController(GrammarQuestionMapper grammarMapper) {
        this.grammarMapper = grammarMapper;
    }

    @GetMapping("/questions")
    public R<?> getQuestions() {
        List<GrammarQuestion> questions = grammarMapper.selectAll();
        return R.ok(questions);
    }

    @PostMapping("/answer")
    public R<?> answerQuestion(@RequestBody Map<String, Object> body) {
        Long questionId = Long.valueOf(body.get("questionId").toString());
        Integer selectedIndex = Integer.valueOf(body.get("selectedIndex").toString());
        GrammarQuestion q = grammarMapper.selectOneById(questionId);
        boolean correct = q != null && q.getCorrectOption().equals(selectedIndex);
        Map<String, Object> data = new HashMap<>();
        data.put("correct", correct);
        data.put("explanation", q != null ? q.getExplanation() : "");
        return R.ok(data);
    }
}
