package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.entity.ListeningLesson;
import com.englishlearning.entity.ListeningQuestion;
import com.englishlearning.mapper.ListeningLessonMapper;
import com.englishlearning.mapper.ListeningQuestionMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.englishlearning.entity.table.ListeningQuestionTableDef.LISTENING_QUESTION;

@RestController
@RequestMapping("/listening")
public class ListeningController {
    private final ListeningLessonMapper lessonMapper;
    private final ListeningQuestionMapper questionMapper;

    public ListeningController(ListeningLessonMapper lessonMapper, ListeningQuestionMapper questionMapper) {
        this.lessonMapper = lessonMapper;
        this.questionMapper = questionMapper;
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
        Long questionId = Long.valueOf(body.get("questionId").toString());
        String answer = body.get("answer").toString();
        ListeningQuestion q = questionMapper.selectOneById(questionId);
        boolean correct = q != null && q.getCorrectOption().equalsIgnoreCase(answer);
        Map<String, Object> data = new HashMap<>();
        data.put("correct", correct);
        data.put("explanation", q != null ? q.getExplanation() : "");
        return R.ok(data);
    }
}
