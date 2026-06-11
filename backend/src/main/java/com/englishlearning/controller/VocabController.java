package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.UserVocabProgress;
import com.englishlearning.entity.VocabWord;
import com.englishlearning.mapper.UserVocabProgressMapper;
import com.englishlearning.mapper.VocabWordMapper;
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

    public VocabController(VocabWordMapper vocabMapper, UserVocabProgressMapper progressMapper) {
        this.vocabMapper = vocabMapper;
        this.progressMapper = progressMapper;
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
        Long wordId = Long.valueOf(body.get("wordId").toString());
        Boolean known = Boolean.valueOf(body.get("known").toString());

        UserVocabProgress progress = new UserVocabProgress();
        progress.setUserId(userId);
        progress.setWordId(wordId);
        progress.setKnown(known);
        progress.setLearnedAt(LocalDateTime.now());
        progressMapper.insert(progress);
        return R.ok(Map.of("message", known ? "太棒了，又记住了一个词！" : "已加入高频错题组"));
    }

    @PostMapping("/speak")
    public R<?> speakWord(@RequestBody Map<String, String> body) {
        return R.ok(Map.of("message", "发音: " + body.getOrDefault("word", "")));
    }
}
