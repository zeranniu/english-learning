package com.englishlearning.service;

import com.englishlearning.entity.*;
import com.englishlearning.mapper.*;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.englishlearning.entity.table.ListeningLessonTableDef.LISTENING_LESSON;
import static com.englishlearning.entity.table.ListeningQuestionTableDef.LISTENING_QUESTION;
import static com.englishlearning.entity.table.ReadingPassageTableDef.READING_PASSAGE;
import static com.englishlearning.entity.table.ReadingQuestionTableDef.READING_QUESTION;
import static com.englishlearning.entity.table.ReadingVocabTableDef.READING_VOCAB;
import static com.englishlearning.entity.table.GrammarQuestionTableDef.GRAMMAR_QUESTION;

@Service
public class AdminContentService {
    private final ListeningLessonMapper lessonMapper;
    private final ListeningQuestionMapper lqMapper;
    private final ReadingPassageMapper passageMapper;
    private final ReadingQuestionMapper rqMapper;
    private final ReadingVocabMapper rvMapper;
    private final GrammarQuestionMapper gqMapper;

    public AdminContentService(ListeningLessonMapper lessonMapper, ListeningQuestionMapper lqMapper,
                               ReadingPassageMapper passageMapper, ReadingQuestionMapper rqMapper,
                               ReadingVocabMapper rvMapper, GrammarQuestionMapper gqMapper) {
        this.lessonMapper = lessonMapper;
        this.lqMapper = lqMapper;
        this.passageMapper = passageMapper;
        this.rqMapper = rqMapper;
        this.rvMapper = rvMapper;
        this.gqMapper = gqMapper;
    }

    // ---- Listening ----
    public Page<ListeningLesson> getLessons(int pageNum, int pageSize) {
        return lessonMapper.paginate(Page.of(pageNum, pageSize), 
            QueryWrapper.create().where(LISTENING_LESSON.IS_DELETED.eq(false)).orderBy(LISTENING_LESSON.ID, true));
    }

    @Transactional
    public void saveLesson(ListeningLesson lesson, ListeningQuestion question) {
        if (lesson.getId() == null) {
            lesson.setIsDeleted(false);
            lessonMapper.insert(lesson);
        } else {
            lessonMapper.update(lesson);
        }
        if (question != null) {
            question.setLessonId(lesson.getId());
            if (question.getId() == null) {
                question.setIsDeleted(false);
                lqMapper.insert(question);
            } else {
                lqMapper.update(question);
            }
        }
    }

    // ---- Reading ----
    public Page<ReadingPassage> getPassages(int pageNum, int pageSize) {
        return passageMapper.paginate(Page.of(pageNum, pageSize), 
            QueryWrapper.create().where(READING_PASSAGE.IS_DELETED.eq(false)).orderBy(READING_PASSAGE.ID, true));
    }

    @Transactional
    public void savePassage(ReadingPassage passage) {
        if (passage.getId() == null) {
            passage.setIsDeleted(false);
            passageMapper.insert(passage);
        } else {
            passageMapper.update(passage);
        }
    }

    @Transactional
    public void saveReadingQuestion(ReadingQuestion question) {
        if (question.getId() == null) {
            question.setIsDeleted(false);
            rqMapper.insert(question);
        } else {
            rqMapper.update(question);
        }
    }

    @Transactional
    public void saveReadingVocab(ReadingVocab vocab) {
        if (vocab.getId() == null) {
            vocab.setIsDeleted(false);
            rvMapper.insert(vocab);
        } else {
            rvMapper.update(vocab);
        }
    }

    // ---- Grammar ----
    public Page<GrammarQuestion> getGrammarQuestions(int pageNum, int pageSize, String grade) {
        QueryWrapper qw = QueryWrapper.create().where(GRAMMAR_QUESTION.IS_DELETED.eq(false));
        if (grade != null && !grade.isEmpty()) {
            qw.where(GRAMMAR_QUESTION.GRADE.eq(grade));
        }
        qw.orderBy(GRAMMAR_QUESTION.ID, true);
        return gqMapper.paginate(Page.of(pageNum, pageSize), qw);
    }

    public void saveGrammarQuestion(GrammarQuestion question) {
        if (question.getId() == null) {
            question.setIsDeleted(false);
            gqMapper.insert(question);
        } else {
            gqMapper.update(question);
        }
    }
}
