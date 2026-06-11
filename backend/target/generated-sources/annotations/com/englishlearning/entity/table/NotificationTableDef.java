package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class NotificationTableDef extends TableDef {

    public static final NotificationTableDef NOTIFICATION = new NotificationTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn ICON = new QueryColumn(this, "icon");

    public final QueryColumn TITLE = new QueryColumn(this, "title");

    public final QueryColumn IS_READ = new QueryColumn(this, "is_read");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    public final QueryColumn DESCRIPTION = new QueryColumn(this, "description");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ICON, TITLE, IS_READ, USER_ID, CREATED_AT, DESCRIPTION};

    public NotificationTableDef() {
        super("", "notification");
    }

    private NotificationTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public NotificationTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new NotificationTableDef("", "notification", alias));
    }

}
