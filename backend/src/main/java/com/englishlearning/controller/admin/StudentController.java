package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.entity.User;
import com.englishlearning.mapper.UserMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.englishlearning.entity.table.UserTableDef.USER;

@RestController
@RequestMapping("/admin/students")
public class StudentController {

    private final UserMapper userMapper;

    public StudentController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/list")
    public R<?> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String grade) {

        QueryWrapper qw = QueryWrapper.create()
                .where(USER.IS_DELETED.eq(false));

        if (keyword != null && !keyword.isEmpty()) {
            qw.where(USER.USERNAME.like(keyword).or(USER.NICKNAME.like(keyword)));
        }
        if (grade != null && !grade.isEmpty()) {
            qw.where(USER.GRADE.eq(grade));
        }

        qw.orderBy(USER.CREATED_AT, false);

        Page<User> page = userMapper.paginate(Page.of(pageNum, pageSize), qw);

        return R.ok(Map.of(
                "list", page.getRecords(),
                "total", page.getTotalRow(),
                "pageNum", page.getPageNumber(),
                "pageSize", page.getPageSize()
        ));
    }

    @GetMapping("/{id}")
    public R<?> detail(@PathVariable Long id) {
        User user = userMapper.selectOneById(id);
        if (user == null) return R.fail("学生不存在");
        return R.ok(user);
    }

    @PutMapping("/{id}")
    public R<?> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userMapper.update(user);
        return R.ok("更新成功");
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        User user = userMapper.selectOneById(id);
        if (user == null) return R.fail("学生不存在");
        user.setIsDeleted(true);
        userMapper.update(user);
        return R.ok("删除成功");
    }
}
