package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.RawReceiptBizType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/28 16:44
 */
@Data
@NoArgsConstructor
public class PurchaseRawReceiptDetailVo {
    @ApiModelProperty(value = "id")
    private Long purchaseRawReceiptOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;


    @ApiModelProperty(value = "采购原料收货单号")
    private String purchaseRawReceiptOrderNo;


    @ApiModelProperty(value = "收货单状态")
    private ReceiptOrderStatus receiptOrderStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "采购原料发货单号")
    private String purchaseRawDeliverOrderNo;

    @ApiModelProperty(value = "采购原料收货单明细")
    private List<PurchaseRawReceiptItemVo> purchaseRawReceiptItemList;

    @ApiModelProperty(value = "原料收货类型")
    private RawReceiptBizType rawReceiptBizType;

    @ApiModelProperty(value = "样品子单号")
    private String sampleChildOrderNo;

    @Data
    public static class PurchaseRawReceiptItemVo {
        @ApiModelProperty(value = "id")
        private Long purchaseRawReceiptOrderItemId;

        @ApiModelProperty(value = "version")
        private Integer version;

        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;


        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "产品名称")
        private String skuEncode;


        @ApiModelProperty(value = "发货数量")
        private Integer deliverCnt;


        @ApiModelProperty(value = "收货数量")
        private Integer receiptCnt;

        @ApiModelProperty(value = "供应商产品名称")
        private String supplierProductName;
    }
}
