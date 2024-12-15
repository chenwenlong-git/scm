package com.hete.supply.scm.server.scm.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.entity.bo.SkuProcedureBo;
import com.hete.supply.scm.server.scm.entity.vo.SkuProcedureVo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderProcedurePo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年07月25日 08:20
 */
public class SkuProcedureConverter {

    public static List<ProcessOrderProcedurePo> boToPo(List<SkuProcedureBo> skuProcedureBos, String processNo) {
        return skuProcedureBos.stream().map(skuProcedureBo -> {
            ProcessOrderProcedurePo processOrderProcedurePo = new ProcessOrderProcedurePo();
            processOrderProcedurePo.setProcessOrderNo(processNo);
            processOrderProcedurePo.setProcessId(skuProcedureBo.getProcessId());
            processOrderProcedurePo.setProcessName(skuProcedureBo.getProcessName());
            processOrderProcedurePo.setProcessCode(skuProcedureBo.getProcessCode());
            processOrderProcedurePo.setProcessLabel(skuProcedureBo.getProcessLabel());
            processOrderProcedurePo.setCommission(skuProcedureBo.getCommission());
            processOrderProcedurePo.setSort(skuProcedureBo.getSort());
            return processOrderProcedurePo;
        }).collect(Collectors.toList());
    }

    public static List<SkuProcedureVo> boToVo(List<SkuProcedureBo> skuProcedureBos) {
        if (CollectionUtils.isEmpty(skuProcedureBos)) {
            return Collections.emptyList();
        }
        return skuProcedureBos.stream().map(skuProcedureBo -> {
            SkuProcedureVo skuProcedureVo = new SkuProcedureVo();
            skuProcedureVo.setProcessId(skuProcedureBo.getProcessId());
            skuProcedureVo.setProcessName(skuProcedureBo.getProcessName());
            skuProcedureVo.setProcessCode(skuProcedureBo.getProcessCode());
            skuProcedureVo.setProcessLabel(skuProcedureBo.getProcessLabel());
            skuProcedureVo.setCommission(skuProcedureBo.getCommission());
            skuProcedureVo.setSort(skuProcedureBo.getSort());
            return skuProcedureVo;
        }).collect(Collectors.toList());
    }

}
