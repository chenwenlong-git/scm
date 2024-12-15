package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rockyHuas
 * @date 2023/06/29 17:10
 */
@Data
@NoArgsConstructor
public class PurchaseReturnPrintVo {
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

    @ApiModelProperty(value = "打印人")
    private String printUser;

    @ApiModelProperty(value = "打印人")
    private String printUsername;

    @ApiModelProperty(value = "打印时间")
    private LocalDateTime printTime;

    @ApiModelProperty(value = "实际退货总数")
    private Integer realityReturnCnt;

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
    }
}
