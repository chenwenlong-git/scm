package com.hete.supply.scm.server.scm.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.CreateType;
import com.hete.supply.scm.server.scm.entity.bo.SkuMaterialBo;
import com.hete.supply.scm.server.scm.entity.vo.SkuMaterialVo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderMaterialCompareBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderMaterialRefBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialComparePo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年07月25日 09:16
 */
public class SkuMaterialConverter {

    public static List<ProcessOrderMaterialPo> boToPo(List<SkuMaterialBo> skuMaterialBos, String processOrderNo, Integer totalProcessNum) {
        return skuMaterialBos.stream().map(skuMaterialBo -> {
            ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
            processOrderMaterialPo.setProcessOrderNo(processOrderNo);
            processOrderMaterialPo.setSku(skuMaterialBo.getSku());
            processOrderMaterialPo.setDeliveryNum(skuMaterialBo.getSingleNum() * totalProcessNum);
            processOrderMaterialPo.setCreateType(CreateType.CREATE);
            return processOrderMaterialPo;
        }).collect(Collectors.toList());
    }

    public static List<ProcessOrderMaterialRefBo> toProcMaterialRefBoList(List<SkuMaterialBo> skuMaterialBos,
                                                                          String processOrderNo,
                                                                          Integer totalProcessNum) {
        if (CollectionUtils.isEmpty(skuMaterialBos)) {
            return Collections.emptyList();
        }
        return skuMaterialBos.stream().map(skuMaterialBo -> {
            ProcessOrderMaterialRefBo processOrderMaterialRefBo = new ProcessOrderMaterialRefBo();

            ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
            processOrderMaterialPo.setProcessOrderNo(processOrderNo);
            processOrderMaterialPo.setSku(skuMaterialBo.getSku());
            processOrderMaterialPo.setDeliveryNum(skuMaterialBo.getSingleNum() * totalProcessNum);
            processOrderMaterialPo.setCreateType(CreateType.CREATE);
            processOrderMaterialRefBo.setProcMaterialPo(processOrderMaterialPo);

            List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList = skuMaterialBo.getProcessOrderMaterialCompareBoList();
            if (CollectionUtils.isNotEmpty(procMaterialCompareBoList)) {
                List<ProcessOrderMaterialComparePo> procMaterialComparePoList = procMaterialCompareBoList.stream().map(procMaterialCompareBo -> {
                    ProcessOrderMaterialComparePo processOrderMaterialComparePo = new ProcessOrderMaterialComparePo();
                    processOrderMaterialComparePo.setSku(procMaterialCompareBo.getSku());
                    processOrderMaterialComparePo.setQuantity(procMaterialCompareBo.getQuantity());
                    return processOrderMaterialComparePo;
                }).collect(Collectors.toList());
                processOrderMaterialRefBo.setProcMaterialComparePoList(procMaterialComparePoList);
            }
            return processOrderMaterialRefBo;
        }).collect(Collectors.toList());
    }

    public static List<SkuMaterialVo> boToVo(List<SkuMaterialBo> skuMaterialBos) {
        return skuMaterialBos.stream().map(skuMaterialBo -> {
            SkuMaterialVo skuMaterialVo = new SkuMaterialVo();
            skuMaterialVo.setSku(skuMaterialBo.getSku());
            skuMaterialVo.setSingleNum(skuMaterialBo.getSingleNum());
            return skuMaterialVo;
        }).collect(Collectors.toList());
    }
}
