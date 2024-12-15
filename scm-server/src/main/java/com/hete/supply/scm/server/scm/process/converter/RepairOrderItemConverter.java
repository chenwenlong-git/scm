package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderResultPo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderItemAndResultVo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderItemDetailVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/1/9 13:46
 */
public class RepairOrderItemConverter {


    public static List<RepairOrderItemDetailVo> poListConvertVoList(List<RepairOrderItemPo> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return new ArrayList<>();
        }
        return poList.stream().map(po -> {
            RepairOrderItemDetailVo repairOrderItemDetailVo = new RepairOrderItemDetailVo();
            repairOrderItemDetailVo.setRepairOrderItemId(po.getRepairOrderItemId());
            repairOrderItemDetailVo.setVersion(po.getVersion());
            repairOrderItemDetailVo.setSpu(po.getSpu());
            repairOrderItemDetailVo.setSku(po.getSku());
            repairOrderItemDetailVo.setBatchCode(po.getBatchCode());
            repairOrderItemDetailVo.setExpectProcessNum(po.getExpectProcessNum());
            repairOrderItemDetailVo.setActProcessedCompleteCnt(po.getActProcessedCompleteCnt());
            repairOrderItemDetailVo.setActProcessScrapCnt(po.getActProcessScrapCnt());
            repairOrderItemDetailVo.setDeliveryNum(po.getDeliveryNum());
            return repairOrderItemDetailVo;
        }).collect(Collectors.toList());
    }

    public static List<RepairOrderItemAndResultVo> itemPoAndResultPoListConvertVoList(List<RepairOrderItemPo> repairOrderItemPoList,
                                                                                      List<RepairOrderResultPo> repairOrderResultPoList,
                                                                                      Map<Long, List<String>> fileCodeMap) {
        List<RepairOrderItemAndResultVo> voList = new ArrayList<>();
        if (CollectionUtils.isEmpty(repairOrderItemPoList)) {
            return voList;
        }

        for (RepairOrderItemPo repairOrderItemPo : repairOrderItemPoList) {
            Long repairOrderItemId = repairOrderItemPo.getRepairOrderItemId();
            List<RepairOrderResultPo> repairOrderResultPos = repairOrderResultPoList.stream()
                    .filter(resultPo -> repairOrderItemId.equals(resultPo.getRepairOrderItemId()))
                    .collect(Collectors.toList());
            // 详情和结果数据组装
            if (CollectionUtils.isNotEmpty(repairOrderResultPos)) {
                for (RepairOrderResultPo repairOrderResultPo : repairOrderResultPos) {
                    RepairOrderItemAndResultVo repairOrderItemAndResultVo = itemPoAndResultPoConvertVo(repairOrderItemPo);
                    repairOrderItemAndResultVo.setRepairOrderResultId(repairOrderResultPo.getRepairOrderResultId());
                    repairOrderItemAndResultVo.setRepairOrderResultVersion(repairOrderResultPo.getVersion());
                    repairOrderItemAndResultVo.setFileCodeList(fileCodeMap.get(repairOrderResultPo.getRepairOrderResultId()));
                    repairOrderItemAndResultVo.setCompletedQuantity(repairOrderResultPo.getCompletedQuantity());
                    repairOrderItemAndResultVo.setRepairUser(repairOrderResultPo.getRepairUser());
                    repairOrderItemAndResultVo.setRepairCreateUsername(repairOrderResultPo.getRepairUsername());
                    repairOrderItemAndResultVo.setRepairTime(repairOrderResultPo.getRepairTime());
                    repairOrderItemAndResultVo.setQcPassQuantity(repairOrderResultPo.getQcPassQuantity());
                    repairOrderItemAndResultVo.setQcFailQuantity(repairOrderResultPo.getQcFailQuantity());
                    repairOrderItemAndResultVo.setMaterialBatchCode(repairOrderResultPo.getMaterialBatchCode());
                    voList.add(repairOrderItemAndResultVo);
                }

                int completedQuantityTotal = repairOrderResultPos.stream()
                        .mapToInt(RepairOrderResultPo::getCompletedQuantity).sum();
                if (completedQuantityTotal < repairOrderItemPo.getExpectProcessNum()) {
                    RepairOrderItemAndResultVo repairOrderItemAndResultVo = itemPoAndResultPoConvertVo(repairOrderItemPo);
                    voList.add(repairOrderItemAndResultVo);
                }
            } else {
                RepairOrderItemAndResultVo repairOrderItemAndResultVo = itemPoAndResultPoConvertVo(repairOrderItemPo);
                voList.add(repairOrderItemAndResultVo);
            }

        }

        return voList;

    }

    public static RepairOrderItemAndResultVo itemPoAndResultPoConvertVo(RepairOrderItemPo repairOrderItemPo) {
        RepairOrderItemAndResultVo repairOrderItemAndResultVo = new RepairOrderItemAndResultVo();
        repairOrderItemAndResultVo.setRepairOrderItemId(repairOrderItemPo.getRepairOrderItemId());
        repairOrderItemAndResultVo.setRepairOrderItemVersion(repairOrderItemPo.getVersion());
        repairOrderItemAndResultVo.setSku(repairOrderItemPo.getSku());
        repairOrderItemAndResultVo.setRepairOrderItemVersion(repairOrderItemPo.getVersion());
        repairOrderItemAndResultVo.setBatchCode(repairOrderItemPo.getBatchCode());
        repairOrderItemAndResultVo.setExpectProcessNum(repairOrderItemPo.getExpectProcessNum());
        repairOrderItemAndResultVo.setDeliveryNum(repairOrderItemPo.getDeliveryNum());
        return repairOrderItemAndResultVo;
    }

}
