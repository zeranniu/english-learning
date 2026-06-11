package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.MistakeRecord;
import com.englishlearning.mapper.MistakeRecordMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.englishlearning.entity.table.MistakeRecordTableDef.MISTAKE_RECORD;

@RestController
@RequestMapping("/mistakes")
public class MistakeController {
    private final MistakeRecordMapper mistakeMapper;

    public MistakeController(MistakeRecordMapper mistakeMapper) {
        this.mistakeMapper = mistakeMapper;
    }

    @GetMapping("/list")
    public R<?> getMistakes(@RequestParam(defaultValue = "vocab") String category) {
        Long userId = UserContext.getCurrentUserId();
        List<MistakeRecord> records = mistakeMapper.selectListByQuery(
                QueryWrapper.create()
                        .where(MISTAKE_RECORD.USER_ID.eq(userId))
                        .and(MISTAKE_RECORD.CATEGORY.eq(category)));
        List<Map<String, Object>> result = new ArrayList<>();
        for (MistakeRecord r : records) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", r.getId().toString());
            item.put("title", r.getTitle());
            item.put("info", r.getErrorInfo());
            item.put("tag", r.getTag());
            result.add(item);
        }
        return R.ok(result);
    }
}
