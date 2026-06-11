package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.config.AdminContext;
import com.englishlearning.entity.User;
import com.englishlearning.service.AdminUserService;
import com.englishlearning.service.OperationLogService;
import com.mybatisflex.core.paginate.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService userService;
    private final OperationLogService logService;

    public AdminUserController(AdminUserService userService, OperationLogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping("/list")
    public R<?> list(@RequestParam(defaultValue = "1") int pageNum,
                     @RequestParam(defaultValue = "10") int pageSize,
                     @RequestParam(required = false) String keyword,
                     @RequestParam(required = false) String grade) {
        Page<User> page = userService.getPage(pageNum, pageSize, keyword, grade);
        return R.ok(Map.of("list", page.getRecords(), "total", page.getTotalRow(), "pageNum", page.getPageNumber(), "pageSize", page.getPageSize()));
    }

    @GetMapping("/{id}")
    public R<?> detail(@PathVariable Long id) {
        return R.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    public R<?> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        logService.log(AdminContext.getCurrentAdminId(), AdminContext.getCurrentAdminUsername(),
                "用户管理", "编辑学生", "PUT /admin/users/" + id, "", "success", "");
        return R.ok("更新成功");
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        userService.delete(id);
        logService.log(AdminContext.getCurrentAdminId(), AdminContext.getCurrentAdminUsername(),
                "用户管理", "删除学生", "DELETE /admin/users/" + id, "", "success", "");
        return R.ok("删除成功");
    }
}
