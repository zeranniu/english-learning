package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.UserVocabProgress;
import com.englishlearning.entity.VocabWord;
import com.englishlearning.mapper.UserVocabProgressMapper;
import com.englishlearning.mapper.VocabWordMapper;
import com.englishlearning.service.StudySessionService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static com.englishlearning.entity.table.UserVocabProgressTableDef.USER_VOCAB_PROGRESS;

@RestController
@RequestMapping("/vocab")
public class VocabController {
    private final VocabWordMapper vocabMapper;
    private final UserVocabProgressMapper progressMapper;
    private final StudySessionService studySessionService;

    public VocabController(VocabWordMapper vocabMapper, UserVocabProgressMapper progressMapper, StudySessionService studySessionService) {
        this.vocabMapper = vocabMapper;
        this.progressMapper = progressMapper;
        this.studySessionService = studySessionService;
    }

    @GetMapping("/words")
    public R<?> getWords() {
        Long userId = UserContext.getCurrentUserId();
        List<VocabWord> words = vocabMapper.selectAll();
        List<Long> learnedIds = progressMapper.selectListByQuery(
                QueryWrapper.create().select(USER_VOCAB_PROGRESS.WORD_ID)
                        .where(USER_VOCAB_PROGRESS.USER_ID.eq(userId)))
                .stream().map(UserVocabProgress::getWordId).toList();

        Map<String, Object> data = new HashMap<>();
        data.put("words", words);
        data.put("learnedIds", learnedIds);
        data.put("total", words.size());
        data.put("learned", learnedIds.size());
        return R.ok(data);
    }

    @PostMapping("/learn")
    public R<?> learnWord(@RequestBody Map<String, Object> body) {
        Long userId = UserContext.getCurrentUserId();

        // 获取 wordId，兼容前端可能未传id的情况
        Long wordId = null;
        Object wordIdObj = body.get("wordId");
        if (wordIdObj != null) {
            wordId = Long.valueOf(wordIdObj.toString());
        }

        // 如果没有wordId，尝试通过单词文本查找
        if (wordId == null && body.get("word") != null) {
            String wordText = body.get("word").toString();
            VocabWord word = vocabMapper.selectOneByQuery(
                    QueryWrapper.create().where("word = '" + wordText + "'"));
            if (word != null) {
                wordId = word.getId();
            }
        }

        Boolean known = body.get("known") != null ? Boolean.valueOf(body.get("known").toString()) : true;

        // 只有找到wordId才记录进度
        if (wordId != null) {
            UserVocabProgress progress = new UserVocabProgress();
            progress.setUserId(userId);
            progress.setWordId(wordId);
            progress.setKnown(known);
            progress.setLearnedAt(LocalDateTime.now());
            progressMapper.insert(progress);
        }

        return R.ok(Map.of("message", known ? "太棒了，又记住了一个词！" : "已加入高频错题组"));
    }

    @PostMapping("/complete-session")
    public R<?> completeSession(@RequestBody Map<String, Object> body) {
        Long userId = UserContext.getCurrentUserId();
        int durationMinutes = body.get("durationMinutes") != null ? Integer.valueOf(body.get("durationMinutes").toString()) : 1;
        int score = body.get("score") != null ? Integer.valueOf(body.get("score").toString()) : 0;

        studySessionService.recordStudySession(userId, durationMinutes, score);
        return R.ok(Map.of("message", "学习会话已记录", "durationMinutes", durationMinutes, "score", score));
    }

    @PostMapping("/speak")
    public R<?> speakWord(@RequestBody Map<String, String> body) {
        return R.ok(Map.of("message", "发音: " + body.getOrDefault("word", "")));
    }
}
