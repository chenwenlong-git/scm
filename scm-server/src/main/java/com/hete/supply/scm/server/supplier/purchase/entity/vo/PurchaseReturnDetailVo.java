package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 10:15
 */
@Data
@NoArgsConstructor
public class PurchaseReturnDetailVo {
    @ApiModelProperty(value = "id")
    private Long purchaseReturnOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "退货单类型")
    private ReturnType returnType;

    @ApiModelProperty(value = "退货单状态")
    private ReturnOrderStatus returnOrderStatus;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "退货单备注")
    private String note;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "退货单明细列表")
    private List<PurchaseReturnItem> purchaseReturnItemList;

    @Data
    @ApiModel(value = "退货单明细")
    public static class PurchaseReturnItem {
        @ApiModelProperty(value = "id")
        private Long purchaseReturnOrderItemId;

        @ApiModelProperty(value = "version")
        private Integer version;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "产品名称")
        private String skuEncode;

        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "来源单据号")
        private String returnBizNo;

        @ApiModelProperty(value = "实际退货数量")
        private Integer realityReturnCnt;

        @ApiModelProperty(value = "预计退货数量")
        private Integer expectedReturnCnt;

        @ApiModelProperty(value = "收货数量")
        private Integer receiptCnt;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "需扣款金额")
        private BigDecimal deductPrice;

        @ApiModelProperty(value = "供应商产品名称")
        private String supplierProductName;

        @ApiModelProperty(value = "结算单价(收单规则计算)")
        private BigDecimal settleRecoOrderPrice;

        @ApiModelProperty(value = "结算总价(收单规则计算)")
        private BigDecimal settleRecoOrderPriceTotal;
    }
}
