package com.englishlearning.mapper;

import com.englishlearning.entity.SysAdmin;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SysAdminMapper extends BaseMapper<SysAdmin> {
    @Select("SELECT sa.* FROM sys_admin sa JOIN sys_admin_role sar ON sa.id = sar.admin_id WHERE sar.role_id = #{roleId}")
    List<SysAdmin> selectByRoleId(Long roleId);
}
