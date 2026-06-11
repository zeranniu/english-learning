package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysMenuTableDef extends TableDef {

    public static final SysMenuTableDef SYS_MENU = new SysMenuTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn ICON = new QueryColumn(this, "icon");

    public final QueryColumn NAME = new QueryColumn(this, "name");

    public final QueryColumn PATH = new QueryColumn(this, "path");

    public final QueryColumn TYPE = new QueryColumn(this, "type");

    public final QueryColumn VISIBLE = new QueryColumn(this, "visible");

    public final QueryColumn PARENT_ID = new QueryColumn(this, "parent_id");

    public final QueryColumn COMPONENT = new QueryColumn(this, "component");

    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn SORT_ORDER = new QueryColumn(this, "sort_order");

    public final QueryColumn PERMISSION = new QueryColumn(this, "permission");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ICON, NAME, PATH, TYPE, VISIBLE, PARENT_ID, COMPONENT, CREATED_AT, IS_DELETED, SORT_ORDER, PERMISSION};

    public SysMenuTableDef() {
        super("", "sys_menu");
    }

    private SysMenuTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysMenuTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysMenuTableDef("", "sys_menu", alias));
    }

}
