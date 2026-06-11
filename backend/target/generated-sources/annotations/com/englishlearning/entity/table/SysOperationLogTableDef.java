package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysOperationLogTableDef extends TableDef {

    public static final SysOperationLogTableDef SYS_OPERATION_LOG = new SysOperationLogTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn IP = new QueryColumn(this, "ip");

    public final QueryColumn ACTION = new QueryColumn(this, "action");

    public final QueryColumn METHOD = new QueryColumn(this, "method");

    public final QueryColumn MODULE = new QueryColumn(this, "module");

    public final QueryColumn PARAMS = new QueryColumn(this, "params");

    public final QueryColumn RESULT = new QueryColumn(this, "result");

    public final QueryColumn ADMIN_ID = new QueryColumn(this, "admin_id");

    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, IP, ACTION, METHOD, MODULE, PARAMS, RESULT, ADMIN_ID, USERNAME, CREATED_AT};

    public SysOperationLogTableDef() {
        super("", "sys_operation_log");
    }

    private SysOperationLogTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysOperationLogTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysOperationLogTableDef("", "sys_operation_log", alias));
    }

}
