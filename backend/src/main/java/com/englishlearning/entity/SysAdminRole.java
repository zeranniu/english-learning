package com.englishlearning.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("sys_admin_role")
public class SysAdminRole {
    @Id(keyType = KeyType.Auto)
    private Long id;
    private Long adminId;
    private Long roleId;
}
