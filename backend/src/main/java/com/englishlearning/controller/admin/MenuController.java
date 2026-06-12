package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.config.AdminContext;
import com.englishlearning.entity.SysAdminRole;
import com.englishlearning.entity.SysMenu;
import com.englishlearning.entity.SysRoleMenu;
import com.englishlearning.mapper.SysAdminRoleMapper;
import com.englishlearning.mapper.SysMenuMapper;
import com.englishlearning.mapper.SysRoleMenuMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.englishlearning.entity.table.SysAdminRoleTableDef.SYS_ADMIN_ROLE;
import static com.englishlearning.entity.table.SysMenuTableDef.SYS_MENU;
import static com.englishlearning.entity.table.SysRoleMenuTableDef.SYS_ROLE_MENU;

@RestController
@RequestMapping("/admin/menus")
public class MenuController {

    private final SysMenuMapper menuMapper;
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysAdminRoleMapper adminRoleMapper;

    public MenuController(
            SysMenuMapper menuMapper,
            SysRoleMenuMapper roleMenuMapper,
            SysAdminRoleMapper adminRoleMapper) {
        this.menuMapper = menuMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.adminRoleMapper = adminRoleMapper;
    }

    @GetMapping("/tree")
    public R<?> tree() {
        List<SysMenu> all = menuMapper.selectListByQuery(
                QueryWrapper.create()
                        .where(SYS_MENU.IS_DELETED.eq(false))
                        .orderBy(SYS_MENU.SORT_ORDER, true)
        );
        return R.ok(buildTree(all, 0L));
    }

    @GetMapping("/user-menus")
    public R<?> getUserMenus() {
        Long adminId = AdminContext.getCurrentAdminId();
        if (adminId == null) {
            return R.fail("未登录");
        }

        // 获取当前用户的角色ID列表
        List<SysAdminRole> adminRoles = adminRoleMapper.selectListByQuery(
                QueryWrapper.create().where(SYS_ADMIN_ROLE.ADMIN_ID.eq(adminId))
        );
        List<Long> roleIds = adminRoles.stream().map(SysAdminRole::getRoleId).collect(Collectors.toList());

        List<SysMenu> allMenus;

        if (roleIds.isEmpty()) {
            // 没有分配角色，返回空菜单
            allMenus = Collections.emptyList();
        } else {
            // 获取角色关联的菜单ID
            List<SysRoleMenu> roleMenus = roleMenuMapper.selectListByQuery(
                    QueryWrapper.create().where(SYS_ROLE_MENU.ROLE_ID.in(roleIds))
            );
            Set<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());

            if (menuIds.isEmpty()) {
                allMenus = Collections.emptyList();
            } else {
                // 获取菜单详情
                allMenus = menuMapper.selectListByQuery(
                        QueryWrapper.create()
                                .where(SYS_MENU.ID.in(menuIds))
                                .where(SYS_MENU.IS_DELETED.eq(false))
                                .where(SYS_MENU.VISIBLE.eq(true))
                                .where(SYS_MENU.TYPE.eq(1))
                                .orderBy(SYS_MENU.SORT_ORDER, true)
                );
            }
        }

        return R.ok(buildTree(allMenus, 0L));
    }

    @PostMapping
    public R<?> save(@RequestBody SysMenu menu) {
        if (menu.getId() == null) {
            menu.setIsDeleted(false);
            menuMapper.insert(menu);
        } else {
            menuMapper.update(menu);
        }
        return R.ok("保存成功");
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        SysMenu menu = menuMapper.selectOneById(id);
        if (menu == null) return R.fail("菜单不存在");
        menu.setIsDeleted(true);
        menuMapper.update(menu);
        return R.ok("删除成功");
    }

    private List<Map<String, Object>> buildTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(m -> Objects.equals(m.getParentId(), parentId))
                .map(m -> {
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", m.getId());
                    node.put("parentId", m.getParentId());
                    node.put("name", m.getName());
                    node.put("path", m.getPath());
                    node.put("component", m.getComponent());
                    node.put("icon", m.getIcon());
                    node.put("sortOrder", m.getSortOrder());
                    node.put("visible", m.getVisible());
                    node.put("permission", m.getPermission());
                    node.put("type", m.getType());
                    List<Map<String, Object>> children = buildTree(menus, m.getId());
                    if (!children.isEmpty()) {
                        node.put("children", children);
                    }
                    return node;
                })
                .collect(Collectors.toList());
    }
}
