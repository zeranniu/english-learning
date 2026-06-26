package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.entity.TaskConfig;
import com.englishlearning.mapper.TaskConfigMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import static com.englishlearning.entity.table.TaskConfigTableDef.TASK_CONFIG;

@RestController
@RequestMapping("/admin/tasks")
public class AdminTaskController {
    private final TaskConfigMapper taskConfigMapper;

    public AdminTaskController(TaskConfigMapper taskConfigMapper) {
        this.taskConfigMapper = taskConfigMapper;
    }

    @GetMapping("/list")
    public R<?> list(
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String taskType,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        QueryWrapper qw = QueryWrapper.create()
                .where(TASK_CONFIG.IS_DELETED.eq(false));
        if (grade != null && !grade.isEmpty()) {
            qw.and(TASK_CONFIG.GRADE.eq(grade));
        }
        if (taskType != null && !taskType.isEmpty()) {
            qw.and(TASK_CONFIG.TASK_TYPE.eq(taskType));
        }
        if (enabled != null) {
            qw.and(TASK_CONFIG.ENABLED.eq(enabled));
        }
        qw.orderBy(TASK_CONFIG.GRADE, true)
          .orderBy(TASK_CONFIG.SORT_ORDER, true);

        Page<TaskConfig> result = taskConfigMapper.paginate(Page.of(page, pageSize), qw);
        return R.ok(java.util.Map.of("list", result.getRecords(), "total", result.getTotalRow(), "page", page, "pageSize", pageSize));
    }

    @PostMapping
    public R<?> save(@RequestBody TaskConfig task) {
        if (task.getId() == null) {
            taskConfigMapper.insert(task);
        } else {
            taskConfigMapper.update(task);
        }
        return R.ok("保存成功");
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        TaskConfig task = new TaskConfig();
        task.setId(id);
        task.setIsDeleted(true);
        taskConfigMapper.update(task);
        return R.ok("删除成功");
    }
}
