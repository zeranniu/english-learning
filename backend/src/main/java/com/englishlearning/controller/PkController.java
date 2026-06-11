package com.englishlearning.controller;

import com.englishlearning.common.R;
import com.englishlearning.config.UserContext;
import com.englishlearning.entity.PkRanking;
import com.englishlearning.entity.User;
import com.englishlearning.mapper.PkRankingMapper;
import com.englishlearning.mapper.UserMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.englishlearning.entity.table.PkRankingTableDef.PK_RANKING;

@RestController
@RequestMapping("/pk")
public class PkController {
    private final PkRankingMapper pkMapper;
    private final UserMapper userMapper;

    public PkController(PkRankingMapper pkMapper, UserMapper userMapper) {
        this.pkMapper = pkMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/ranking")
    public R<?> getRanking() {
        List<PkRanking> rankings = pkMapper.selectListByQuery(
                QueryWrapper.create().orderBy(PK_RANKING.SCORE, false).limit(10));
        List<Map<String, Object>> result = new ArrayList<>();
        int rank = 1;
        for (PkRanking r : rankings) {
            User user = userMapper.selectOneById(r.getUserId());
            Map<String, Object> item = new HashMap<>();
            item.put("rank", rank++);
            item.put("userId", r.getUserId());
            item.put("name", user != null ? user.getNickname() : "未知");
            item.put("score", r.getScore());
            item.put("isMe", r.getUserId().equals(UserContext.getCurrentUserId()));
            result.add(item);
        }
        return R.ok(result);
    }

    @PostMapping("/start")
    public R<?> startPK() {
        return R.ok(Map.of("message", "匹配对手中..."));
    }
}
