package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@Data
@ApiModel(value = "采购需求子单")
public class PurchaseChildOrderVo {
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseOrderStatus purchaseOrderStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "总结算金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "采购总数")
    private Integer purchaseTotal;


    @ApiModelProperty(value = "拆分类型")
    private PurchaseBizType purchaseBizType;

    @ApiModelProperty(value = "删除状态（不为0时，则为删除状态）")
    private Long deleteStatus;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "采购产品明细列表")
    private List<PurchaseProductItemVo> purchaseProductItemList;
}
