package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:17
 */
@AllArgsConstructor
@Getter
public enum FinanceRecoFundType implements IRemark {

    // 对账单款项类型 关联收单类型 排序
    PURCHASE_PAYMENT("采购货款", List.of(CollectOrderType.PURCHASE_DELIVER,
            CollectOrderType.PURCHASE_SAMPLE,
            CollectOrderType.RETURN_ORDER), 1),

    PREPAYMENTS("预付款项", List.of(CollectOrderType.PREPAYMENT), 2),

    CUSTOM_PAYMENT("自定义款项", List.of(CollectOrderType.CUSTOMS_RETURN,
            CollectOrderType.CUSTOMS_COST_RETURN,
            CollectOrderType.FACTORY_COST,
            CollectOrderType.QUALITY_INSPECTION,
            CollectOrderType.SALES_MATERIAL,
            CollectOrderType.ADOPT_MATERIAL,
            CollectOrderType.LONG_RANGE_COST,
            CollectOrderType.HAND_HOOK_COST,
            CollectOrderType.FACTORY_LOGISTICS_COST), 3),

    OTHER_PAYMENT("其他款项", List.of(CollectOrderType.DEDUCT, CollectOrderType.SUPPLEMENT), 4),
    ;

    private final String remark;
    private final List<CollectOrderType> collectOrderTypeList;
    private final Integer sort;


    /**
     * 获取按 sort 字段正序排序的款项枚举列表
     *
     * @return List<FinanceRecoFundType>
     * @author ChenWenLong
     * @date 2024/7/10 15:28
     */
    public static List<FinanceRecoFundType> getSortedTypeList() {
        return Arrays.stream(FinanceRecoFundType.values())
                .sorted(Comparator.comparingInt(FinanceRecoFundType::getSort))
                .collect(Collectors.toList());
    }

}
