package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.config.AdminContext;
import com.englishlearning.entity.SysMenu;
import com.englishlearning.entity.SysRole;
import com.englishlearning.service.AdminMenuService;
import com.englishlearning.service.AdminRoleService;
import com.englishlearning.service.OperationLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminSystemController {
    private final AdminRoleService roleService;
    private final AdminMenuService menuService;
    private final OperationLogService logService;

    public AdminSystemController(AdminRoleService roleService, AdminMenuService menuService, OperationLogService logService) {
        this.roleService = roleService;
        this.menuService = menuService;
        this.logService = logService;
    }

    // ---- Roles ----
    @GetMapping("/roles/list")
    public R<?> listRoles() {
        return R.ok(roleService.listAll());
    }

    @PostMapping("/roles")
    public R<?> saveRole(@RequestBody SysRole role) {
        roleService.save(role);
        return R.ok("保存成功");
    }

    @DeleteMapping("/roles/{id}")
    public R<?> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return R.ok("删除成功");
    }

    @GetMapping("/roles/{id}/menus")
    public R<?> getRoleMenus(@PathVariable Long id) {
        return R.ok(menuService.getMenuIdsByRole(id));
    }

    @PutMapping("/roles/{id}/menus")
    public R<?> assignMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        menuService.assignMenusToRole(id, menuIds);
        logService.log(AdminContext.getCurrentAdminId(), AdminContext.getCurrentAdminUsername(),
                "角色管理", "分配菜单", "PUT /admin/roles/" + id + "/menus", menuIds.toString(), "success", "");
        return R.ok("分配成功");
    }

    // ---- Menus ----
    @GetMapping("/menus/tree")
    public R<?> menuTree() {
        return R.ok(menuService.getTree());
    }

    @PostMapping("/menus")
    public R<?> saveMenu(@RequestBody SysMenu menu) {
        menuService.save(menu);
        return R.ok("保存成功");
    }

    @DeleteMapping("/menus/{id}")
    public R<?> deleteMenu(@PathVariable Long id) {
        menuService.delete(id);
        return R.ok("删除成功");
    }
}
