package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderItemType;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderType;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseReturnSimpleVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 12:00
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverDetailVo {
    @ApiModelProperty(value = "id")
    private Long purchaseDeliverOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "发货单状态")
    private DeliverOrderStatus deliverOrderStatus;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "发货单类型")
    private DeliverOrderType deliverOrderType;

    @ApiModelProperty(value = "发货单明细列表")
    private List<PurchaseDeliverItem> purchaseDeliverItemList;

    @ApiModelProperty(value = "退货信息")
    private List<PurchaseReturnSimpleVo> purchaseReturnSimpleList;

    @ApiModelProperty(value = "关联单据信息列表")
    private List<AssociateDocumentsItem> associateDocumentsItemList;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @Data
    @ApiModel(value = "发货单明细")
    public static class PurchaseDeliverItem {

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "sku变体属性")
        private String variantProperties;

        @ApiModelProperty(value = "发货数")
        private Integer deliverCnt;


        @ApiModelProperty(value = "收货数")
        private Integer receiptCnt;


        @ApiModelProperty(value = "正品数")
        private Integer qualityGoodsCnt;


        @ApiModelProperty(value = "次品数")
        private Integer defectiveGoodsCnt;

        @ApiModelProperty(value = "sku产品名称")
        private String skuEncode;

        @ApiModelProperty(value = "供应商产品名称")
        private String supplierProductName;

    }

    @Data
    @ApiModel(value = "关联单据信息")
    public static class AssociateDocumentsItem {

        @ApiModelProperty(value = "单据类型")
        private DeliverOrderItemType deliverOrderItemType;

        @ApiModelProperty(value = "单据号")
        private String businessNo;

        @ApiModelProperty(value = "状态")
        private String statusName;

        @ApiModelProperty(value = "收货数量")
        private Integer receiptCnt;

        @ApiModelProperty(value = "质检数量")
        private Integer qcAmount;

        @ApiModelProperty(value = "正品数量")
        private Integer qualityGoodsCnt;

        @ApiModelProperty(value = "次品数量")
        private Integer defectiveGoodsCnt;

        @ApiModelProperty(value = "已上架数量")
        private Integer onShelvesAmount;

        @ApiModelProperty(value = "结算数量")
        private Integer settleAmount;

        @ApiModelProperty(value = "退货总数")
        private Integer returnAmount;

        @ApiModelProperty(value = "完成时间")
        private LocalDateTime finishTime;


    }
}
