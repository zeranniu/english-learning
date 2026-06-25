package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class TaskConfigTableDef extends TableDef {

    public static final TaskConfigTableDef TASK_CONFIG = new TaskConfigTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn GRADE = new QueryColumn(this, "grade");

    public final QueryColumn ENABLED = new QueryColumn(this, "enabled");

    public final QueryColumn TASK_TYPE = new QueryColumn(this, "task_type");

    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn SORT_ORDER = new QueryColumn(this, "sort_order");

    public final QueryColumn TASK_TITLE = new QueryColumn(this, "task_title");

    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    public final QueryColumn TASK_TARGET = new QueryColumn(this, "task_target");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, GRADE, ENABLED, TASK_TYPE, CREATED_AT, IS_DELETED, SORT_ORDER, TASK_TITLE, UPDATED_AT, TASK_TARGET};

    public TaskConfigTableDef() {
        super("", "task_config");
    }

    private TaskConfigTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public TaskConfigTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new TaskConfigTableDef("", "task_config", alias));
    }

}
