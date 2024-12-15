package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/5/21 11:17
 */
@Data
@NoArgsConstructor
public class RecoOrderExportItemVo {

    @ApiModelProperty(value = "主键id")
    private Long financeRecoOrderItemSkuId;

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "对账单状态")
    private FinanceRecoOrderStatus financeRecoOrderStatus;

    @ApiModelProperty(value = "对账单状态")
    private String financeRecoOrderStatusName;

    @ApiModelProperty(value = "供应商")
    private String supplierCode;

    @ApiModelProperty(value = "对账周期开始时间")
    private LocalDateTime reconciliationStartTime;

    @ApiModelProperty(value = "对账周期开始时间")
    private String reconciliationStartTimeStr;

    @ApiModelProperty(value = "对账周期结束时间")
    private LocalDateTime reconciliationEndTime;

    @ApiModelProperty(value = "对账周期结束时间")
    private String reconciliationEndTimeStr;

    @ApiModelProperty(value = "对账周期")
    private ReconciliationCycle reconciliationCycle;

    @ApiModelProperty(value = "对账周期")
    private String reconciliationCycleName;

    @ApiModelProperty(value = "关联结算单号")
    private String financeSettleOrderNo;

    @ApiModelProperty(value = "收单单据")
    private String collectOrderNo;

    @ApiModelProperty(value = "收单状态")
    private RecoOrderItemSkuStatus recoOrderItemSkuStatus;

    @ApiModelProperty(value = "收单状态")
    private String recoOrderItemSkuStatusName;

    @ApiModelProperty(value = "收单时间")
    private LocalDateTime associationTime;

    @ApiModelProperty(value = "收单时间")
    private String associationTimeStr;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "收单金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "对账总金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "款项类型")
    private FinanceRecoFundType financeRecoFundType;

    @ApiModelProperty(value = "款项类型")
    private String financeRecoFundTypeName;

    @ApiModelProperty(value = "收单类型")
    private CollectOrderType collectOrderType;

    @ApiModelProperty(value = "收单类型")
    private String collectOrderTypeName;

    @ApiModelProperty(value = "收付类型")
    private FinanceRecoPayType financeRecoPayType;

    @ApiModelProperty(value = "收付类型")
    private String financeRecoPayTypeName;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "关联补扣款单")
    private String deductSupplementOrder;

    @ApiModelProperty(value = "补扣款类型（补款/扣款/补扣款）")
    private String deductSupplementType;

    @ApiModelProperty(value = "调整金额（补款-扣款）")
    private BigDecimal deductSupplementPrice;

    @ApiModelProperty(value = "关联单明细的ID")
    private Long collectOrderItemId;

    @ApiModelProperty(value = "关联采购订单")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购类型")
    private String purchaseBizType;

    @ApiModelProperty(value = "收货仓")
    private String warehouseName;

    @ApiModelProperty(value = "发货时间")
    private String purchaseDeliverTime;

    @ApiModelProperty(value = "上架时间")
    private String onShelvesTime;

    @ApiModelProperty(value = "发货数")
    private Integer deliveryAmount;

    @ApiModelProperty(value = "收货数")
    private Integer receiveAmount;

    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "退货数")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "上架数")
    private Integer onShelvesAmount;

    @ApiModelProperty(value = "结算单价（采购订单结算单价）")
    private BigDecimal purchaseSettlePrice;

    @ApiModelProperty(value = "结算总价（结算单价*上架数）")
    private BigDecimal settleTotalPrice;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
