package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysAdminTableDef extends TableDef {

    public static final SysAdminTableDef SYS_ADMIN = new SysAdminTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    public final QueryColumn AVATAR = new QueryColumn(this, "avatar");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn NICKNAME = new QueryColumn(this, "nickname");

    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    public final QueryColumn LAST_LOGIN_AT = new QueryColumn(this, "last_login_at");

    public final QueryColumn LAST_LOGIN_IP = new QueryColumn(this, "last_login_ip");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, EMAIL, PHONE, AVATAR, STATUS, NICKNAME, PASSWORD, USERNAME, CREATED_AT, UPDATED_AT, LAST_LOGIN_AT, LAST_LOGIN_IP};

    public SysAdminTableDef() {
        super("", "sys_admin");
    }

    private SysAdminTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysAdminTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysAdminTableDef("", "sys_admin", alias));
    }

}
