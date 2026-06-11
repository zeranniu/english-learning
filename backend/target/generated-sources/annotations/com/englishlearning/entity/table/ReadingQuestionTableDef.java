package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class ReadingQuestionTableDef extends TableDef {

    public static final ReadingQuestionTableDef READING_QUESTION = new ReadingQuestionTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn OPTION_A = new QueryColumn(this, "option_a");

    public final QueryColumn OPTION_B = new QueryColumn(this, "option_b");

    public final QueryColumn OPTION_C = new QueryColumn(this, "option_c");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn PASSAGE_ID = new QueryColumn(this, "passage_id");

    public final QueryColumn QUESTION_TEXT = new QueryColumn(this, "question_text");

    public final QueryColumn CORRECT_OPTION = new QueryColumn(this, "correct_option");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, OPTION_A, OPTION_B, OPTION_C, IS_DELETED, PASSAGE_ID, QUESTION_TEXT, CORRECT_OPTION};

    public ReadingQuestionTableDef() {
        super("", "reading_question");
    }

    private ReadingQuestionTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ReadingQuestionTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ReadingQuestionTableDef("", "reading_question", alias));
    }

}
