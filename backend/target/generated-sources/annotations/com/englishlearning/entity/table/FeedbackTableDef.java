package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class FeedbackTableDef extends TableDef {

    public static final FeedbackTableDef FEEDBACK = new FeedbackTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn CONTACT = new QueryColumn(this, "contact");

    public final QueryColumn CONTENT = new QueryColumn(this, "content");

    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    public final QueryColumn FEEDBACK_TYPE = new QueryColumn(this, "feedback_type");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, CONTACT, CONTENT, CREATED_AT, FEEDBACK_TYPE};

    public FeedbackTableDef() {
        super("", "feedback");
    }

    private FeedbackTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public FeedbackTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new FeedbackTableDef("", "feedback", alias));
    }

}
