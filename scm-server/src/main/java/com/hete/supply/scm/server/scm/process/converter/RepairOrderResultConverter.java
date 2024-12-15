package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderResultPo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderResultDetailVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/1/9 13:46
 */
public class RepairOrderResultConverter {


    public static List<RepairOrderResultDetailVo> poListConvertVoList(List<RepairOrderItemPo> repairOrderItemPoList,
                                                                      List<RepairOrderResultPo> poList,
                                                                      Map<Long, List<String>> fileCodeMap) {
        if (CollectionUtils.isEmpty(poList)) {
            return new ArrayList<>();
        }
        return poList.stream()
                .map(po -> {
                    RepairOrderResultDetailVo repairOrderResultDetailVo = new RepairOrderResultDetailVo();
                    repairOrderResultDetailVo.setRepairOrderResultId(po.getRepairOrderResultId());
                    repairOrderResultDetailVo.setVersion(po.getVersion());
                    repairOrderResultDetailVo.setSku(po.getSku());
                    repairOrderResultDetailVo.setBatchCode(po.getBatchCode());
                    repairOrderResultDetailVo.setMaterialBatchCode(po.getMaterialBatchCode());
                    repairOrderResultDetailVo.setFileCodeList(fileCodeMap.get(po.getRepairOrderResultId()));
                    repairOrderResultDetailVo.setCompletedQuantity(po.getCompletedQuantity());
                    repairOrderResultDetailVo.setRepairUser(po.getRepairUser());
                    repairOrderResultDetailVo.setRepairCreateUsername(po.getRepairUsername());
                    repairOrderResultDetailVo.setRepairTime(po.getRepairTime());
                    repairOrderResultDetailVo.setQcPassQuantity(po.getQcPassQuantity());
                    repairOrderResultDetailVo.setQcFailQuantity(po.getQcFailQuantity());

                    Long repairOrderItemId = po.getRepairOrderItemId();
                    RepairOrderItemPo matchRepairItem = repairOrderItemPoList.stream()
                            .filter(repairOrderItemPo -> Objects.equals(repairOrderItemId,
                                    repairOrderItemPo.getRepairOrderItemId()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.nonNull(matchRepairItem)) {
                        repairOrderResultDetailVo.setExpectProcessNum(matchRepairItem.getExpectProcessNum());
                        repairOrderResultDetailVo.setDeliveryNum(matchRepairItem.getDeliveryNum());
                    }
                    return repairOrderResultDetailVo;

                })
                .collect(Collectors.toList());
    }

}
