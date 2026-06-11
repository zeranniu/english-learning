package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class ReadingPassageTableDef extends TableDef {

    public static final ReadingPassageTableDef READING_PASSAGE = new ReadingPassageTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn GRADE = new QueryColumn(this, "grade");

    public final QueryColumn TITLE = new QueryColumn(this, "title");

    public final QueryColumn CONTENT = new QueryColumn(this, "content");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn DIFFICULTY = new QueryColumn(this, "difficulty");

    public final QueryColumn SUGGESTED_MINUTES = new QueryColumn(this, "suggested_minutes");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, GRADE, TITLE, CONTENT, IS_DELETED, DIFFICULTY, SUGGESTED_MINUTES};

    public ReadingPassageTableDef() {
        super("", "reading_passage");
    }

    private ReadingPassageTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ReadingPassageTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ReadingPassageTableDef("", "reading_passage", alias));
    }

}
