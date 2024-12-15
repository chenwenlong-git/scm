package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderType;
import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/3 11:56
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverVo {
    @ApiModelProperty(value = "id")
    private Long purchaseDeliverOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "发货单状态")
    private DeliverOrderStatus deliverOrderStatus;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;


    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


    @ApiModelProperty(value = "发货人id")
    private String deliverUser;


    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTime;


    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @ApiModelProperty(value = "发货单类型")
    private DeliverOrderType deliverOrderType;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "采购需求数")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "采购收货单号")
    private String purchaseReceiptOrderNo;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "上架数量")
    private Integer onShelvesAmount;

    @ApiModelProperty(value = "收货单状态")
    private WmsEnum.ReceiveOrderState receiveOrderState;

    @ApiModelProperty(value = "跟单确认人")
    private String confirmUser;

    @ApiModelProperty(value = "跟单确认人")
    private String confirmUsername;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "采购数量")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "id")
    private Long purchaseDeliverOrderItemId;

}
