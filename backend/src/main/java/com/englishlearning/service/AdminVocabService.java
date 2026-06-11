package com.englishlearning.service;

import com.englishlearning.entity.VocabWord;
import com.englishlearning.mapper.VocabWordMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;

import static com.englishlearning.entity.table.VocabWordTableDef.VOCAB_WORD;

@Service
public class AdminVocabService {
    private final VocabWordMapper vocabMapper;

    public AdminVocabService(VocabWordMapper vocabMapper) {
        this.vocabMapper = vocabMapper;
    }

    public Page<VocabWord> getPage(int pageNum, int pageSize, String grade, String keyword) {
        QueryWrapper qw = QueryWrapper.create().where(VOCAB_WORD.IS_DELETED.eq(false));
        if (grade != null && !grade.isEmpty()) {
            qw.where(VOCAB_WORD.GRADE.eq(grade));
        }
        if (keyword != null && !keyword.isEmpty()) {
            qw.where(VOCAB_WORD.WORD.like(keyword).or(VOCAB_WORD.TRANSLATION.like(keyword)));
        }
        qw.orderBy(VOCAB_WORD.ID, true);
        return vocabMapper.paginate(Page.of(pageNum, pageSize), qw);
    }

    public void save(VocabWord word) {
        if (word.getId() == null) {
            word.setIsDeleted(false);
            vocabMapper.insert(word);
        } else {
            vocabMapper.update(word);
        }
    }

    public void delete(Long id) {
        VocabWord word = new VocabWord();
        word.setId(id);
        word.setIsDeleted(true);
        vocabMapper.update(word);
    }
}
