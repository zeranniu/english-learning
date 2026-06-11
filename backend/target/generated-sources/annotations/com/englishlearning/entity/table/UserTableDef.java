package com.englishlearning.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserTableDef extends TableDef {

    public static final UserTableDef USER = new UserTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn EXP = new QueryColumn(this, "exp");

    public final QueryColumn GRADE = new QueryColumn(this, "grade");

    public final QueryColumn LEVEL = new QueryColumn(this, "level");

    public final QueryColumn AVATAR = new QueryColumn(this, "avatar");

    public final QueryColumn EXP_MAX = new QueryColumn(this, "exp_max");

    public final QueryColumn NICKNAME = new QueryColumn(this, "nickname");

    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn LEVEL_NAME = new QueryColumn(this, "level_name");

    public final QueryColumn STUDENT_NO = new QueryColumn(this, "student_no");

    public final QueryColumn STUDY_DAYS = new QueryColumn(this, "study_days");

    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    public final QueryColumn STREAK_DAYS = new QueryColumn(this, "streak_days");

    public final QueryColumn TOTAL_SCORE = new QueryColumn(this, "total_score");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, EXP, GRADE, LEVEL, AVATAR, EXP_MAX, NICKNAME, PASSWORD, USERNAME, CREATED_AT, IS_DELETED, LEVEL_NAME, STUDENT_NO, STUDY_DAYS, UPDATED_AT, STREAK_DAYS, TOTAL_SCORE};

    public UserTableDef() {
        super("", "users");
    }

    private UserTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserTableDef("", "users", alias));
    }

}
