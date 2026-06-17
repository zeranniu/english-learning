package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.ReadingPassage;
import com.englishlearning.entity.ReadingQuestion;
import com.englishlearning.entity.ReadingVocab;
import com.englishlearning.mapper.ReadingPassageMapper;
import com.englishlearning.mapper.ReadingQuestionMapper;
import com.englishlearning.mapper.ReadingVocabMapper;
import com.englishlearning.service.StudySessionService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.englishlearning.entity.table.ReadingQuestionTableDef.READING_QUESTION;
import static com.englishlearning.entity.table.ReadingVocabTableDef.READING_VOCAB;

@RestController
@RequestMapping("/reading")
public class ReadingController {
    private final ReadingPassageMapper passageMapper;
    private final ReadingQuestionMapper questionMapper;
    private final ReadingVocabMapper vocabMapper;
    private final StudySessionService studySessionService;

    public ReadingController(ReadingPassageMapper passageMapper, ReadingQuestionMapper questionMapper, ReadingVocabMapper vocabMapper, StudySessionService studySessionService) {
        this.passageMapper = passageMapper;
        this.questionMapper = questionMapper;
        this.vocabMapper = vocabMapper;
        this.studySessionService = studySessionService;
    }

    @GetMapping("/passages")
    public R<?> getPassages() {
        return R.ok(passageMapper.selectAll());
    }

    @GetMapping("/passage/{id}")
    public R<?> getPassageDetail(@PathVariable Long id) {
        ReadingPassage passage = passageMapper.selectOneById(id);
        List<ReadingVocab> vocabs = vocabMapper.selectListByQuery(
                QueryWrapper.create().where(READING_VOCAB.PASSAGE_ID.eq(id)));
        List<ReadingQuestion> questions = questionMapper.selectListByQuery(
                QueryWrapper.create().where(READING_QUESTION.PASSAGE_ID.eq(id)));
        Map<String, Object> data = new HashMap<>();
        data.put("passage", passage);
        data.put("vocabs", vocabs);
        data.put("questions", questions);
        return R.ok(data);
    }

    @PostMapping("/answer")
    public R<?> answerQuestion(@RequestBody Map<String, Object> body) {
        Long userId = UserContext.getCurrentUserId();

        // 安全获取参数
        Object questionIdObj = body.get("questionId");
        Long questionId = questionIdObj != null ? Long.valueOf(questionIdObj.toString()) : 1L;
        Object answerIndexObj = body.get("answerIndex");
        Integer answerIndex = answerIndexObj != null ? Integer.valueOf(answerIndexObj.toString()) : 0;

        ReadingQuestion q = questionMapper.selectOneById(questionId);
        boolean correct = q != null && q.getCorrectOption() != null && q.getCorrectOption().equals(answerIndex);

        return R.ok(Map.of("correct", correct));
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
