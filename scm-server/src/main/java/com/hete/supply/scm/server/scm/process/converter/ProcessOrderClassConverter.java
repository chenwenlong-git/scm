package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderProcedurePo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;

/**
 * @author weiwenxin
 * @date 2023/9/20 18:59
 */
public class ProcessOrderClassConverter {
    public static ProcessOrderProcedurePo convertProcessPoToProcedurePo(String processOrderNo, ProcessPo processPo,
                                                                        Integer sort) {
        if (null == processPo) {
            return null;
        }
        ProcessOrderProcedurePo processOrderProcedurePo = new ProcessOrderProcedurePo();
        processOrderProcedurePo.setProcessOrderNo(processOrderNo);
        processOrderProcedurePo.setProcessId(processPo.getProcessId());
        processOrderProcedurePo.setProcessCode(processPo.getProcessCode());
        processOrderProcedurePo.setProcessName(processPo.getProcessName());
        processOrderProcedurePo.setSort(sort);
        processOrderProcedurePo.setProcessLabel(processPo.getProcessLabel());
        processOrderProcedurePo.setCommission(processPo.getCommission());
        return processOrderProcedurePo;
    }
}
