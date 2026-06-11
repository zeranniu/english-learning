package com.englishlearning.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("sys_operation_log")
public class SysOperationLog {
    @Id(keyType = KeyType.Auto)
    private Long id;
    private Long adminId;
    private String username;
    private String module;
    private String action;
    private String method;
    private String params;
    private String result;
    private String ip;
    private LocalDateTime createdAt;
}
