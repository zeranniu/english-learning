package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class PkRankingTableDef extends TableDef {

    public static final PkRankingTableDef PK_RANKING = new PkRankingTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn SCORE = new QueryColumn(this, "score");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, SCORE, USER_ID, UPDATED_AT};

    public PkRankingTableDef() {
        super("", "pk_ranking");
    }

    private PkRankingTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public PkRankingTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new PkRankingTableDef("", "pk_ranking", alias));
    }

}
