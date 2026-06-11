package com.englishlearning.service;

import com.englishlearning.entity.SysMenu;
import com.englishlearning.entity.SysRole;
import com.englishlearning.entity.SysRoleMenu;
import com.englishlearning.mapper.SysMenuMapper;
import com.englishlearning.mapper.SysRoleMapper;
import com.englishlearning.mapper.SysRoleMenuMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.englishlearning.entity.table.SysMenuTableDef.SYS_MENU;
import static com.englishlearning.entity.table.SysRoleMenuTableDef.SYS_ROLE_MENU;

@Service
public class AdminMenuService {
    private final SysMenuMapper menuMapper;
    private final SysRoleMenuMapper roleMenuMapper;

    public AdminMenuService(SysMenuMapper menuMapper, SysRoleMenuMapper roleMenuMapper) {
        this.menuMapper = menuMapper;
        this.roleMenuMapper = roleMenuMapper;
    }

    public List<SysMenu> getTree() {
        List<SysMenu> all = menuMapper.selectListByQuery(
                QueryWrapper.create().where(SYS_MENU.IS_DELETED.eq(false)).orderBy(SYS_MENU.SORT_ORDER, true));
        return buildTree(all, 0L);
    }

    public List<SysMenu> getMenuIdsByRole(Long roleId) {
        List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleId(roleId);
        if (menuIds.isEmpty()) return List.of();
        return menuMapper.selectListByIds(menuIds);
    }

    @Transactional
    public void assignMenusToRole(Long roleId, List<Long> menuIds) {
        roleMenuMapper.deleteByQuery(QueryWrapper.create().where(SYS_ROLE_MENU.ROLE_ID.eq(roleId)));
        if (menuIds != null) {
            for (Long menuId : menuIds) {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                roleMenuMapper.insert(rm);
            }
        }
    }

    public void save(SysMenu menu) {
        if (menu.getId() == null) {
            menu.setIsDeleted(false);
            menuMapper.insert(menu);
        } else {
            menuMapper.update(menu);
        }
    }

    public void delete(Long id) {
        SysMenu menu = new SysMenu();
        menu.setId(id);
        menu.setIsDeleted(true);
        menuMapper.update(menu);
        roleMenuMapper.deleteByQuery(QueryWrapper.create().where(SYS_ROLE_MENU.MENU_ID.eq(id)));
    }

    private List<SysMenu> buildTree(List<SysMenu> all, Long parentId) {
        return all.stream()
                .filter(m -> m.getParentId() != null && m.getParentId().equals(parentId))
                .peek(m -> m.setVisible(m.getVisible() != null ? m.getVisible() : true))
                .toList();
    }
}
