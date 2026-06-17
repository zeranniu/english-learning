package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.ListeningLesson;
import com.englishlearning.entity.ListeningQuestion;
import com.englishlearning.mapper.ListeningLessonMapper;
import com.englishlearning.mapper.ListeningQuestionMapper;
import com.englishlearning.service.StudySessionService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.englishlearning.entity.table.ListeningQuestionTableDef.LISTENING_QUESTION;

@RestController
@RequestMapping("/listening")
public class ListeningController {
    private final ListeningLessonMapper lessonMapper;
    private final ListeningQuestionMapper questionMapper;
    private final StudySessionService studySessionService;

    public ListeningController(ListeningLessonMapper lessonMapper, ListeningQuestionMapper questionMapper, StudySessionService studySessionService) {
        this.lessonMapper = lessonMapper;
        this.questionMapper = questionMapper;
        this.studySessionService = studySessionService;
    }

    @GetMapping("/lessons")
    public R<?> getLessons() {
        List<ListeningLesson> lessons = lessonMapper.selectAll();
        return R.ok(lessons);
    }

    @GetMapping("/lesson/{id}")
    public R<?> getLessonDetail(@PathVariable Long id) {
        ListeningLesson lesson = lessonMapper.selectOneById(id);
        List<ListeningQuestion> questions = questionMapper.selectListByQuery(
                QueryWrapper.create().where(LISTENING_QUESTION.LESSON_ID.eq(id)));
        Map<String, Object> data = new HashMap<>();
        data.put("lesson", lesson);
        data.put("questions", questions);
        return R.ok(data);
    }

    @PostMapping("/answer")
    public R<?> answerQuestion(@RequestBody Map<String, Object> body) {
        Long userId = UserContext.getCurrentUserId();

        // 安全获取参数
        Object questionIdObj = body.get("questionId");
        Long questionId = questionIdObj != null ? Long.valueOf(questionIdObj.toString()) : 1L;
        String answer = body.get("answer") != null ? body.get("answer").toString() : "";

        ListeningQuestion q = questionMapper.selectOneById(questionId);
        boolean correct = q != null && q.getCorrectOption() != null && q.getCorrectOption().equalsIgnoreCase(answer);

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
