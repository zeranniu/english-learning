package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class MistakeRecordTableDef extends TableDef {

    public static final MistakeRecordTableDef MISTAKE_RECORD = new MistakeRecordTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn TAG = new QueryColumn(this, "tag");

    public final QueryColumn TITLE = new QueryColumn(this, "title");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn CATEGORY = new QueryColumn(this, "category");

    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    public final QueryColumn ERROR_INFO = new QueryColumn(this, "error_info");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, TAG, TITLE, USER_ID, CATEGORY, CREATED_AT, ERROR_INFO};

    public MistakeRecordTableDef() {
        super("", "mistake_record");
    }

    private MistakeRecordTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public MistakeRecordTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new MistakeRecordTableDef("", "mistake_record", alias));
    }

}
