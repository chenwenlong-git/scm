package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierSimpleVo;
import com.hete.supply.scm.server.scm.entity.vo.PurchaseDeliverSimpleVo;
import com.hete.supply.scm.server.scm.entity.vo.RawProductItemVo;
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
public class PurchaseProductSearchVo extends SupplierSimpleVo {
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "商品图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty("sku")
    private String sku;

    @ApiModelProperty(value = "需求对象")
    private SkuType skuType;

    @ApiModelProperty(value = "采购母单单号(采购需求单号)")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "拆分类型")
    private PurchaseBizType purchaseBizType;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "是否首单")
    private PurchaseOrderType purchaseOrderType;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseOrderStatus purchaseOrderStatus;

    @ApiModelProperty(value = "结算金额（单价）")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "采购价（原价）")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "可发货数")
    private Integer shippableCnt;

    @ApiModelProperty(value = "总采购数量(下单数)")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "采购发货单列表")
    private List<PurchaseDeliverSimpleVo> purchaseDeliverOrderList;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "平台名称")
    private String platformName;


    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "业务约定交期(要求发货时间)")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "确认人（跟单人）")
    private String confirmUser;

    @ApiModelProperty(value = "确认人（跟单人）")
    private String confirmUsername;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;

    @ApiModelProperty(value = "发货时间（首次发货时间）")
    private LocalDateTime firstDeliverTime;

    @ApiModelProperty(value = "最后准交时间")
    private LocalDateTime timelyDeliveryTime;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTime;

    @ApiModelProperty(value = "sku")
    private List<PurchaseSkuCntVo> skuList;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;

    @ApiModelProperty(value = "原料明细列表")
    private List<RawProductItemVo> rawProductItemList;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;


    @ApiModelProperty(value = "计划确认人")
    private String planConfirmUser;

    @ApiModelProperty(value = "计划确认人")
    private String planConfirmUsername;

    @ApiModelProperty(value = "计划确认时间")
    private LocalDateTime planConfirmTime;

    @ApiModelProperty(value = "退货信息")
    private List<PurchaseReturnSimpleVo> purchaseReturnSimpleList;

    @ApiModelProperty(value = "跟单确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "编辑后延期天数")
    private Integer delayDays;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDate;

    @ApiModelProperty(value = "sku是否需管理")
    private BooleanType rawManage;

    @ApiModelProperty(value = "是否存在供应商类型的原料")
    private BooleanType isExistSupplierRaw;

    @ApiModelProperty(value = "投产时间")
    private LocalDateTime commissioningTime;

    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;
}
