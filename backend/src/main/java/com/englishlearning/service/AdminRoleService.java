package com.englishlearning.service;

import com.englishlearning.entity.SysRole;
import com.englishlearning.mapper.SysRoleMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.englishlearning.entity.table.SysRoleTableDef.SYS_ROLE;

@Service
public class AdminRoleService {
    private final SysRoleMapper roleMapper;

    public AdminRoleService(SysRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public List<SysRole> listAll() {
        return roleMapper.selectListByQuery(QueryWrapper.create().where(SYS_ROLE.STATUS.ne(0)));
    }

    public void save(SysRole role) {
        if (role.getId() == null) {
            roleMapper.insert(role);
        } else {
            roleMapper.update(role);
        }
    }

    public void delete(Long id) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setStatus(0);
        roleMapper.update(role);
    }
}
