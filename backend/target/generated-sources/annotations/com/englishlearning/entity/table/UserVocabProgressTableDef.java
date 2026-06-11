package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserVocabProgressTableDef extends TableDef {

    public static final UserVocabProgressTableDef USER_VOCAB_PROGRESS = new UserVocabProgressTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn KNOWN = new QueryColumn(this, "known");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn WORD_ID = new QueryColumn(this, "word_id");

    public final QueryColumn LEARNED_AT = new QueryColumn(this, "learned_at");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, KNOWN, USER_ID, WORD_ID, LEARNED_AT};

    public UserVocabProgressTableDef() {
        super("", "user_vocab_progress");
    }

    private UserVocabProgressTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserVocabProgressTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserVocabProgressTableDef("", "user_vocab_progress", alias));
    }

}
