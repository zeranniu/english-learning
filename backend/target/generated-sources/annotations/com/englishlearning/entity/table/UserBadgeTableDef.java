package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserBadgeTableDef extends TableDef {

    public static final UserBadgeTableDef USER_BADGE = new UserBadgeTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn UNLOCKED = new QueryColumn(this, "unlocked");

    public final QueryColumn BADGE_NAME = new QueryColumn(this, "badge_name");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, UNLOCKED, BADGE_NAME};

    public UserBadgeTableDef() {
        super("", "user_badge");
    }

    private UserBadgeTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserBadgeTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserBadgeTableDef("", "user_badge", alias));
    }

}
