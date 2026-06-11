package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.service.AdminDashboardService;
import com.englishlearning.service.OperationLogService;
import com.englishlearning.entity.SysOperationLog;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminStatsController {
    private final AdminDashboardService dashboardService;
    private final OperationLogService logService;

    public AdminStatsController(AdminDashboardService dashboardService, OperationLogService logService) {
        this.dashboardService = dashboardService;
        this.logService = logService;
    }

    @GetMapping("/dashboard/overview")
    public R<?> overview() {
        Map<String, Object> overview = dashboardService.getOverview();
        List<Map<String, Object>> recentLogs = dashboardService.getRecentLogs(7);
        List<Map<String, Object>> topMistakes = dashboardService.getTopMistakes(10);
        List<?> topStudents = dashboardService.getTopStudents(10);
        return R.ok(Map.of("overview", overview, "recentLogs", recentLogs, "topMistakes", topMistakes, "topStudents", topStudents));
    }

    @GetMapping("/mistakes/stats")
    public R<?> mistakeStats() {
        return R.ok(dashboardService.getTopMistakes(20));
    }

    @GetMapping("/logs/list")
    public R<?> logList(@RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "20") int pageSize,
                        @RequestParam(required = false) String module) {
        Page<SysOperationLog> page = logService.getPage(pageNum, pageSize, module);
        return R.ok(Map.of("list", page.getRecords(), "total", page.getTotalRow(), "pageNum", page.getPageNumber(), "pageSize", page.getPageSize()));
    }
}
