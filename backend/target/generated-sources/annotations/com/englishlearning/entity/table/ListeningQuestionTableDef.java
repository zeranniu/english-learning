package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class ListeningQuestionTableDef extends TableDef {

    public static final ListeningQuestionTableDef LISTENING_QUESTION = new ListeningQuestionTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn OPTION_A = new QueryColumn(this, "option_a");

    public final QueryColumn OPTION_B = new QueryColumn(this, "option_b");

    public final QueryColumn OPTION_C = new QueryColumn(this, "option_c");

    public final QueryColumn LESSON_ID = new QueryColumn(this, "lesson_id");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn EXPLANATION = new QueryColumn(this, "explanation");

    public final QueryColumn QUESTION_TEXT = new QueryColumn(this, "question_text");

    public final QueryColumn CORRECT_OPTION = new QueryColumn(this, "correct_option");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, OPTION_A, OPTION_B, OPTION_C, LESSON_ID, IS_DELETED, EXPLANATION, QUESTION_TEXT, CORRECT_OPTION};

    public ListeningQuestionTableDef() {
        super("", "listening_question");
    }

    private ListeningQuestionTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ListeningQuestionTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ListeningQuestionTableDef("", "listening_question", alias));
    }

}
