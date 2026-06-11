package com.englishlearning.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("sys_menu")
public class SysMenu {
    @Id(keyType = KeyType.Auto)
    private Long id;
    private Long parentId;
    private String name;
    private String path;
    private String component;
    private String icon;
    private Integer sortOrder;
    private Boolean visible;
    private String permission;
    private Integer type;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
}
