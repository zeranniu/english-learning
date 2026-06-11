package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class DailyStudyLogTableDef extends TableDef {

    public static final DailyStudyLogTableDef DAILY_STUDY_LOG = new DailyStudyLogTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn WEEK_RANK = new QueryColumn(this, "week_rank");

    public final QueryColumn STUDY_DATE = new QueryColumn(this, "study_date");

    public final QueryColumn WEEK_SCORE = new QueryColumn(this, "week_score");

    public final QueryColumn STUDY_MINUTES = new QueryColumn(this, "study_minutes");

    public final QueryColumn TASKS_COMPLETED = new QueryColumn(this, "tasks_completed");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, WEEK_RANK, STUDY_DATE, WEEK_SCORE, STUDY_MINUTES, TASKS_COMPLETED};

    public DailyStudyLogTableDef() {
        super("", "daily_study_log");
    }

    private DailyStudyLogTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public DailyStudyLogTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new DailyStudyLogTableDef("", "daily_study_log", alias));
    }

}
