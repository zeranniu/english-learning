package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.entity.*;
import com.englishlearning.service.AdminContentService;
import com.englishlearning.service.AdminVocabService;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminContentController {
    private final AdminVocabService vocabService;
    private final AdminContentService contentService;

    public AdminContentController(AdminVocabService vocabService, AdminContentService contentService) {
        this.vocabService = vocabService;
        this.contentService = contentService;
    }

    // ---- Vocab ----
    @GetMapping("/vocab/list")
    public R<?> vocabList(@RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam(required = false) String grade,
                          @RequestParam(required = false) String keyword) {
        Page<VocabWord> page = vocabService.getPage(pageNum, pageSize, grade, keyword);
        return R.ok(Map.of("list", page.getRecords(), "total", page.getTotalRow(), "pageNum", page.getPageNumber(), "pageSize", page.getPageSize()));
    }

    @PostMapping("/vocab")
    public R<?> saveVocab(@RequestBody VocabWord word) {
        vocabService.save(word);
        return R.ok("保存成功");
    }

    @DeleteMapping("/vocab/{id}")
    public R<?> deleteVocab(@PathVariable Long id) {
        vocabService.delete(id);
        return R.ok("删除成功");
    }

    // ---- Listening ----
    @GetMapping("/listening/list")
    public R<?> listeningList(@RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        Page<ListeningLesson> page = contentService.getLessons(pageNum, pageSize);
        return R.ok(Map.of("list", page.getRecords(), "total", page.getTotalRow(), "pageNum", page.getPageNumber(), "pageSize", page.getPageSize()));
    }

    @PostMapping("/listening")
    public R<?> saveListening(@RequestBody Map<String, Object> body) {
        ListeningLesson lesson = new ListeningLesson();
        lesson.setTitle((String) body.get("title"));
        lesson.setDuration((String) body.get("duration"));
        lesson.setAudioUrl((String) body.get("audioUrl"));
        lesson.setGrade((String) body.get("grade"));
        ListeningQuestion question = null;
        if (body.get("question") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> q = (Map<String, Object>) body.get("question");
            question = new ListeningQuestion();
            question.setQuestionText((String) q.get("questionText"));
            question.setOptionA((String) q.get("optionA"));
            question.setOptionB((String) q.get("optionB"));
            question.setOptionC((String) q.get("optionC"));
            question.setCorrectOption((String) q.get("correctOption"));
            question.setExplanation((String) q.get("explanation"));
        }
        contentService.saveLesson(lesson, question);
        return R.ok("保存成功");
    }

    // ---- Reading ----
    @GetMapping("/reading/list")
    public R<?> readingList(@RequestParam(defaultValue = "1") int pageNum,
                            @RequestParam(defaultValue = "10") int pageSize) {
        Page<ReadingPassage> page = contentService.getPassages(pageNum, pageSize);
        return R.ok(Map.of("list", page.getRecords(), "total", page.getTotalRow(), "pageNum", page.getPageNumber(), "pageSize", page.getPageSize()));
    }

    @PostMapping("/reading/passage")
    public R<?> savePassage(@RequestBody ReadingPassage passage) {
        contentService.savePassage(passage);
        return R.ok("保存成功");
    }

    @PostMapping("/reading/question")
    public R<?> saveReadingQuestion(@RequestBody ReadingQuestion question) {
        contentService.saveReadingQuestion(question);
        return R.ok("保存成功");
    }

    @PostMapping("/reading/vocab")
    public R<?> saveReadingVocab(@RequestBody ReadingVocab vocab) {
        contentService.saveReadingVocab(vocab);
        return R.ok("保存成功");
    }

    // ---- Grammar ----
    @GetMapping("/grammar/list")
    public R<?> grammarList(@RequestParam(defaultValue = "1") int pageNum,
                            @RequestParam(defaultValue = "10") int pageSize,
                            @RequestParam(required = false) String grade) {
        Page<GrammarQuestion> page = contentService.getGrammarQuestions(pageNum, pageSize, grade);
        return R.ok(Map.of("list", page.getRecords(), "total", page.getTotalRow(), "pageNum", page.getPageNumber(), "pageSize", page.getPageSize()));
    }

    @PostMapping("/grammar")
    public R<?> saveGrammar(@RequestBody GrammarQuestion question) {
        contentService.saveGrammarQuestion(question);
        return R.ok("保存成功");
    }
}
