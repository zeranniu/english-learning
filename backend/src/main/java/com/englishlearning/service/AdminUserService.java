package com.englishlearning.service;

import com.englishlearning.entity.User;
import com.englishlearning.mapper.UserMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;

import static com.englishlearning.entity.table.UserTableDef.USER;

@Service
public class AdminUserService {
    private final UserMapper userMapper;

    public AdminUserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Page<User> getPage(int pageNum, int pageSize, String keyword, String grade) {
        QueryWrapper qw = QueryWrapper.create().where(USER.IS_DELETED.eq(false));
        if (keyword != null && !keyword.isEmpty()) {
            qw.where(USER.USERNAME.like(keyword).or(USER.NICKNAME.like(keyword)));
        }
        if (grade != null && !grade.isEmpty()) {
            qw.where(USER.GRADE.eq(grade));
        }
        qw.orderBy(USER.CREATED_AT, false);
        return userMapper.paginate(Page.of(pageNum, pageSize), qw);
    }

    public User getById(Long id) {
        return userMapper.selectOneByQuery(QueryWrapper.create().where(USER.ID.eq(id)).and(USER.IS_DELETED.eq(false)));
    }

    public void update(User user) {
        userMapper.update(user);
    }

    public void delete(Long id) {
        User user = new User();
        user.setId(id);
        user.setIsDeleted(true);
        userMapper.update(user);
    }

    public long getTotalCount() {
        return userMapper.selectCountByQuery(QueryWrapper.create().where(USER.IS_DELETED.eq(false)));
    }
}
