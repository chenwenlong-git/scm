package com.hete.supply.scm.server.scm.ibfs.converter;

import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemRelationPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemSkuPo;
import com.hete.supply.scm.server.scm.ibfs.enums.RecoOrderItemRelationType;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/6/14 15:53
 */
public class FinanceRecoOrderItemRelationConverter {


    /**
     * 创建关联使用单据数据
     *
     * @param financeRecoOrderItemSkuPo:
     * @param recoOrderItemRelationType:
     * @param businessId:
     * @param businessNo:
     * @param sku:
     * @param num:
     * @param totalPrice:
     * @return FinanceRecoOrderItemRelationPo
     * @author ChenWenLong
     * @date 2024/6/14 15:58
     */
    public static FinanceRecoOrderItemRelationPo create(FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo,
                                                        RecoOrderItemRelationType recoOrderItemRelationType,
                                                        Long businessId,
                                                        String businessNo,
                                                        String sku,
                                                        Integer num,
                                                        BigDecimal totalPrice) {
        FinanceRecoOrderItemRelationPo financeRecoOrderItemRelationPo = new FinanceRecoOrderItemRelationPo();
        financeRecoOrderItemRelationPo.setFinanceRecoOrderItemId(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemId());
        financeRecoOrderItemRelationPo.setFinanceRecoOrderItemSkuId(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemSkuId());
        financeRecoOrderItemRelationPo.setFinanceRecoOrderNo(financeRecoOrderItemSkuPo.getFinanceRecoOrderNo());
        financeRecoOrderItemRelationPo.setRecoOrderItemRelationType(recoOrderItemRelationType);
        financeRecoOrderItemRelationPo.setBusinessId(businessId);
        financeRecoOrderItemRelationPo.setBusinessNo(businessNo);
        financeRecoOrderItemRelationPo.setSku(sku);
        financeRecoOrderItemRelationPo.setNum(num);
        financeRecoOrderItemRelationPo.setTotalPrice(totalPrice);
        return financeRecoOrderItemRelationPo;
    }

}
