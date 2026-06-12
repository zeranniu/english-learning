package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.entity.SysMenu;
import com.englishlearning.mapper.SysMenuMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.englishlearning.entity.table.SysMenuTableDef.SYS_MENU;

@RestController
@RequestMapping("/admin/menus")
public class MenuController {

    private final SysMenuMapper menuMapper;

    public MenuController(SysMenuMapper menuMapper) {
        this.menuMapper = menuMapper;
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
        // 简化版：返回所有菜单（后续可根据角色权限过滤）
        List<SysMenu> all = menuMapper.selectListByQuery(
                QueryWrapper.create()
                        .where(SYS_MENU.IS_DELETED.eq(false))
                        .where(SYS_MENU.VISIBLE.eq(true))
                        .where(SYS_MENU.TYPE.eq(1)) // 只返回菜单类型
                        .orderBy(SYS_MENU.SORT_ORDER, true)
        );
        return R.ok(buildTree(all, 0L));
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
