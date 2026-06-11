package com.englishlearning.service;

import com.englishlearning.entity.SysOperationLog;
import com.englishlearning.mapper.SysOperationLogMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;

import static com.englishlearning.entity.table.SysOperationLogTableDef.SYS_OPERATION_LOG;

@Service
public class OperationLogService {
    private final SysOperationLogMapper logMapper;

    public OperationLogService(SysOperationLogMapper logMapper) {
        this.logMapper = logMapper;
    }

    public Page<SysOperationLog> getPage(int pageNum, int pageSize, String module) {
        QueryWrapper qw = QueryWrapper.create();
        if (module != null && !module.isEmpty()) {
            qw.where(SYS_OPERATION_LOG.MODULE.eq(module));
        }
        qw.orderBy(SYS_OPERATION_LOG.CREATED_AT, false);
        return logMapper.paginate(Page.of(pageNum, pageSize), qw);
    }

    public void log(Long adminId, String username, String module, String action, String method, String params, String result, String ip) {
        SysOperationLog entry = new SysOperationLog();
        entry.setAdminId(adminId);
        entry.setUsername(username);
        entry.setModule(module);
        entry.setAction(action);
        entry.setMethod(method);
        entry.setParams(params);
        entry.setResult(result);
        entry.setIp(ip);
        logMapper.insert(entry);
    }
}
