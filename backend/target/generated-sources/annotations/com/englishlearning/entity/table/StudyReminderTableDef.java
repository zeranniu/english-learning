package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class StudyReminderTableDef extends TableDef {

    public static final StudyReminderTableDef STUDY_REMINDER = new StudyReminderTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn LABEL = new QueryColumn(this, "label");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn ENABLED = new QueryColumn(this, "enabled");

    public final QueryColumn REMINDER_TIME = new QueryColumn(this, "reminder_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, LABEL, USER_ID, ENABLED, REMINDER_TIME};

    public StudyReminderTableDef() {
        super("", "study_reminder");
    }

    private StudyReminderTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public StudyReminderTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new StudyReminderTableDef("", "study_reminder", alias));
    }

}
