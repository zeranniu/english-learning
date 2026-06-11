package com.englishlearning.mapper;

import com.englishlearning.entity.SysRole;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    @Select("SELECT sr.* FROM sys_role sr JOIN sys_admin_role sar ON sr.id = sar.role_id WHERE sar.admin_id = #{adminId}")
    List<SysRole> selectByAdminId(Long adminId);
}
