package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysAdminRoleTableDef extends TableDef {

    public static final SysAdminRoleTableDef SYS_ADMIN_ROLE = new SysAdminRoleTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn ROLE_ID = new QueryColumn(this, "role_id");

    public final QueryColumn ADMIN_ID = new QueryColumn(this, "admin_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ROLE_ID, ADMIN_ID};

    public SysAdminRoleTableDef() {
        super("", "sys_admin_role");
    }

    private SysAdminRoleTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysAdminRoleTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysAdminRoleTableDef("", "sys_admin_role", alias));
    }

}
