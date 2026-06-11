package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class ListeningLessonTableDef extends TableDef {

    public static final ListeningLessonTableDef LISTENING_LESSON = new ListeningLessonTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn GRADE = new QueryColumn(this, "grade");

    public final QueryColumn TITLE = new QueryColumn(this, "title");

    public final QueryColumn AUDIO_URL = new QueryColumn(this, "audio_url");

    public final QueryColumn DURATION = new QueryColumn(this, "duration");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, GRADE, TITLE, AUDIO_URL, DURATION, IS_DELETED};

    public ListeningLessonTableDef() {
        super("", "listening_lesson");
    }

    private ListeningLessonTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ListeningLessonTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ListeningLessonTableDef("", "listening_lesson", alias));
    }

}
