package com.englishlearning.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("sys_role_menu")
public class SysRoleMenu {
    @Id(keyType = KeyType.Auto)
    private Long id;
    private Long roleId;
    private Long menuId;
}
