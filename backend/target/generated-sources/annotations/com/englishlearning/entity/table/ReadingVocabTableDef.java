package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class ReadingVocabTableDef extends TableDef {

    public static final ReadingVocabTableDef READING_VOCAB = new ReadingVocabTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn WORD = new QueryColumn(this, "word");

    public final QueryColumn MEANING = new QueryColumn(this, "meaning");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn PASSAGE_ID = new QueryColumn(this, "passage_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, WORD, MEANING, IS_DELETED, PASSAGE_ID};

    public ReadingVocabTableDef() {
        super("", "reading_vocab");
    }

    private ReadingVocabTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ReadingVocabTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ReadingVocabTableDef("", "reading_vocab", alias));
    }

}
