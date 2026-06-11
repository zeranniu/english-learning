package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class VocabWordTableDef extends TableDef {

    public static final VocabWordTableDef VOCAB_WORD = new VocabWordTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn ICON = new QueryColumn(this, "icon");

    public final QueryColumn WORD = new QueryColumn(this, "word");

    public final QueryColumn GRADE = new QueryColumn(this, "grade");

    public final QueryColumn PHONETIC = new QueryColumn(this, "phonetic");

    public final QueryColumn ICON_COLOR = new QueryColumn(this, "icon_color");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn TRANSLATION = new QueryColumn(this, "translation");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ICON, WORD, GRADE, PHONETIC, ICON_COLOR, IS_DELETED, TRANSLATION};

    public VocabWordTableDef() {
        super("", "vocab_word");
    }

    private VocabWordTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public VocabWordTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new VocabWordTableDef("", "vocab_word", alias));
    }

}
