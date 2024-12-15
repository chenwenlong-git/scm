package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:17
 */
@AllArgsConstructor
@Getter
public enum CollectOrderType implements IRemark {

    // 收单类型及其对应的对账单应付、应收类型
    PURCHASE_DELIVER("采购发货单", FinanceRecoPayType.HANDLE),
    PURCHASE_SAMPLE("样品采购单", FinanceRecoPayType.HANDLE),
    RETURN_ORDER("库内退供单", FinanceRecoPayType.RECEIVABLE),
    CUSTOMS_RETURN("报关返点", FinanceRecoPayType.RECEIVABLE),
    CUSTOMS_COST_RETURN("报关运费返还", FinanceRecoPayType.RECEIVABLE),
    FACTORY_COST("工厂垫付运费", FinanceRecoPayType.HANDLE),
    QUALITY_INSPECTION("驻场质检", FinanceRecoPayType.RECEIVABLE),
    SALES_MATERIAL("售卖网料网帽", FinanceRecoPayType.RECEIVABLE),
    ADOPT_MATERIAL("代采辅料", FinanceRecoPayType.RECEIVABLE),
    LONG_RANGE_COST("长档超比费用", FinanceRecoPayType.HANDLE),
    HAND_HOOK_COST("手勾点加急费用", FinanceRecoPayType.HANDLE),
    FACTORY_LOGISTICS_COST("工厂物流加急费用", FinanceRecoPayType.HANDLE),
    DEDUCT("扣款单", FinanceRecoPayType.RECEIVABLE),
    SUPPLEMENT("补款单", FinanceRecoPayType.HANDLE),
    PREPAYMENT("预付款单", FinanceRecoPayType.RECEIVABLE),
    ;

    private final String remark;
    private final FinanceRecoPayType financeRecoPayType;

    /**
     * 对应检验规则说明
     *
     * @param recoOrderInspectType:
     * @return String
     * @author ChenWenLong
     * @date 2024/7/22 15:11
     */
    public String getInspectRule(RecoOrderInspectType recoOrderInspectType) {
        String inspectRule = "";
        if (PURCHASE_DELIVER.equals(this) && RecoOrderInspectType.QUANTITY.equals(recoOrderInspectType)) {
            return "发货单与收货单上架数比对";
        }
        if (PURCHASE_DELIVER.equals(this) && RecoOrderInspectType.PRICE.equals(recoOrderInspectType)) {
            return "发货单与采购单详情的(采购单价-原料单价±补扣款)比对";
        }

        if (PREPAYMENT.equals(this) && RecoOrderInspectType.QUANTITY.equals(recoOrderInspectType)) {
            return "预付款单完成且无关联对账单";
        }
        if (PREPAYMENT.equals(this) && RecoOrderInspectType.PRICE.equals(recoOrderInspectType)) {
            return "预付款单完成且无关联对账单";
        }

        if (PURCHASE_SAMPLE.equals(this) && RecoOrderInspectType.QUANTITY.equals(recoOrderInspectType)) {
            return "样品单与收货单上架数比对";
        }
        if (PURCHASE_SAMPLE.equals(this) && RecoOrderInspectType.PRICE.equals(recoOrderInspectType)) {
            return "样品单与批次码的价格比对";
        }

        if (RETURN_ORDER.equals(this) && RecoOrderInspectType.QUANTITY.equals(recoOrderInspectType)) {
            return "退货单的收货数量与实际退货数比对";
        }
        if (RETURN_ORDER.equals(this) && RecoOrderInspectType.PRICE.equals(recoOrderInspectType)) {
            return "库内退供单的单价与批次码的价格比对";
        }

        return inspectRule;

    }

}
