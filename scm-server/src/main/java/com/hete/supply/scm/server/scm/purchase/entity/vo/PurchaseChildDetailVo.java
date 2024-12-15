package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseStatusModelVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleProductVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierSimpleVo;
import com.hete.supply.scm.server.scm.entity.vo.RawProductItemVo;
import com.hete.supply.scm.server.scm.entity.vo.RawRemainItemVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleSimpleVo;
import com.hete.supply.scm.server.supplier.entity.vo.OverseasWarehouseMsgVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseChildDetailVo extends SupplierSimpleVo {
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "sku数量")
    private Integer parentSkuCnt;

    @ApiModelProperty(value = "采购数量(母单)")
    private Integer parentPurchaseTotal;

    @ApiModelProperty(value = "采购单状态(母单)")
    private PurchaseParentOrderStatus purchaseParentOrderStatus;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;

    @ApiModelProperty(value = "采购数量")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "采购单状态(子单)")
    private PurchaseOrderStatus purchaseOrderStatus;

    @ApiModelProperty(value = "上一个采购单状态(子单)")
    private PurchaseOrderStatus prePurchaseOrderStatus;

    @ApiModelProperty(value = "下一个采购单状态(子单)")
    private PurchaseOrderStatus afterPurchaseOrderStatus;

    @ApiModelProperty(value = "前面的采购单状态(子单)")
    private List<PurchaseStatusModelVo> prePurchaseOrderStatusList;

    @ApiModelProperty(value = "后面的采购单状态(子单)")
    private List<PurchaseStatusModelVo> afterPurchaseOrderStatusList;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;

    @ApiModelProperty(value = "拆分类型")
    private PurchaseBizType purchaseBizType;

    @ApiModelProperty(value = "原料明细列表")
    private List<RawProductItemVo> rawProductItemList;

    @ApiModelProperty(value = "采购子单明细项列表")
    private List<ChildOrderPurchaseItem> purchaseProductItemList;

    @ApiModelProperty(value = "采购母单明细项列表")
    private List<PurchaseParentItemVo> purchaseParentItemList;

    @ApiModelProperty(value = "采购需求子单列表")
    private List<PurchaseChildOrderVo> purchaseChildOrderList;

    @ApiModelProperty(value = "生产信息")
    private SampleProductVo sampleProductVo;

    @ApiModelProperty(value = "发货信息")
    private List<PurchaseDeliverVo> purchaseDeliverList;

    @ApiModelProperty(value = "结算信息")
    private List<PurchaseSettleSimpleVo> purchaseSettleSimpleList;

    @ApiModelProperty(value = "退货信息")
    private List<PurchaseReturnSimpleVo> purchaseReturnSimpleList;

    @ApiModelProperty(value = "需求变更单")
    private List<PurchaseModifyVo> purchaseModifyList;

    @ApiModelProperty(value = "海外仓文件信息")
    private OverseasWarehouseMsgVo overseasWarehouseMsgVo;

    @ApiModelProperty(value = "是否上传海外仓文件信息")
    private BooleanType isUploadOverseasMsg;

    @ApiModelProperty(value = "是否直发")
    private BooleanType isDirectSend;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;

    @ApiModelProperty(value = "可发货数")
    private Integer shippableCnt;

    @ApiModelProperty(value = "原料bom表")
    private List<RawProductItemVo> purchaseRawList;

    @ApiModelProperty(value = "剩余原料")
    private List<RawRemainItemVo> rawRemainItemList;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "终止来货备注")
    private String finishRemark;

    @ApiModelProperty(value = "sku类型")
    private SkuType skuType;

    @ApiModelProperty(value = "订单类型")
    private PurchaseOrderType purchaseOrderType;

    @ApiModelProperty(value = "要求发货时间")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDate;

    @ApiModelProperty(value = "sku是否需管理")
    private BooleanType rawManage;

    @ApiModelProperty(value = "最新调价审批单号")
    private String adjustPriceApproveNo;

    @ApiModelProperty(value = "能否归还备货仓")
    private BooleanType returnStockUp;

    @ApiModelProperty(value = "采购退货单号")
    private String returnOrderNo;

    @Data
    public static class ChildOrderPurchaseItem {

        @ApiModelProperty(value = "id")
        private Long purchaseChildOrderItemId;

        @ApiModelProperty(value = "version")
        private Integer version;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "产品名称")
        private String skuEncode;

        @ApiModelProperty(value = "批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "sku变体属性")
        private String variantProperties;

        @ApiModelProperty(value = "采购价")
        private BigDecimal purchasePrice;

        @ApiModelProperty(value = "优惠类型")
        private DiscountType discountType;


        @ApiModelProperty(value = "我司原料单价")
        private BigDecimal substractPrice;


        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "降档数")
        private Integer modifyCnt;

        @ApiModelProperty(value = "初始采购数")
        private Integer initPurchaseCnt;

        @ApiModelProperty(value = "采购数")
        private Integer purchaseCnt;

        @ApiModelProperty(value = "发货数")
        private Integer deliverCnt;


        @ApiModelProperty(value = "正品数")
        private Integer qualityGoodsCnt;


        @ApiModelProperty(value = "次品数")
        private Integer defectiveGoodsCnt;

        @ApiModelProperty(value = "供应商产品名称")
        private String supplierProductName;
    }

}
