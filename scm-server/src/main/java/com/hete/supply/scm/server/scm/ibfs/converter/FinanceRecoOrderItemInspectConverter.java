package com.hete.supply.scm.server.scm.ibfs.converter;

import com.hete.supply.scm.api.scm.entity.enums.RecoOrderInspectType;
import com.hete.supply.scm.api.scm.entity.enums.RecoOrderItemSkuStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemInspectPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemSkuPo;
import com.hete.support.api.enums.BooleanType;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/5/29 15:56
 */
public class FinanceRecoOrderItemInspectConverter {


    /**
     * 创建验证数据
     *
     * @param financeRecoOrderItemSkuPo:
     * @param recoOrderInspectType:检验类型
     * @param originalValue:收单数据
     * @param inspectValue:检验数据
     * @return FinanceRecoOrderItemInspectPo
     * @author ChenWenLong
     * @date 2024/5/29 16:05
     */
    public static FinanceRecoOrderItemInspectPo create(FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo,
                                                       RecoOrderInspectType recoOrderInspectType,
                                                       BigDecimal originalValue,
                                                       BigDecimal inspectValue) {
        FinanceRecoOrderItemInspectPo financeRecoOrderItemInspectPo = new FinanceRecoOrderItemInspectPo();
        financeRecoOrderItemInspectPo.setFinanceRecoOrderItemId(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemId());
        financeRecoOrderItemInspectPo.setFinanceRecoOrderItemSkuId(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemSkuId());
        financeRecoOrderItemInspectPo.setFinanceRecoOrderNo(financeRecoOrderItemSkuPo.getFinanceRecoOrderNo());
        financeRecoOrderItemInspectPo.setRecoOrderInspectType(recoOrderInspectType);
        financeRecoOrderItemInspectPo.setOriginalValue(originalValue);
        financeRecoOrderItemInspectPo.setInspectValue(inspectValue);
        if (originalValue.compareTo(inspectValue) == 0) {
            financeRecoOrderItemInspectPo.setInspectResult(BooleanType.TRUE);
        } else {
            financeRecoOrderItemSkuPo.setRecoOrderItemSkuStatus(RecoOrderItemSkuStatus.EXCEPTION);
            financeRecoOrderItemInspectPo.setInspectResult(BooleanType.FALSE);
        }
        return financeRecoOrderItemInspectPo;
    }

}
