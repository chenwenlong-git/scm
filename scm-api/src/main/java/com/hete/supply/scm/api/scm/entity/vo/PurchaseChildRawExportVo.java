package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/1/12 14:44
 */
@Data
@NoArgsConstructor
public class PurchaseChildRawExportVo {
    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购单状态")
    private String purchaseOrderStatus;


    @ApiModelProperty(value = "原料仓库编码")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "原料提供方")
    private RawSupplier rawSupplier;

    @ApiModelProperty(value = "分配数量")
    private Integer dispenseCnt;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "采购类型")
    private PurchaseBizType purchaseBizType;


    @ApiModelProperty(value = "消耗库存")
    private Integer actualConsumeCnt;


    @ApiModelProperty(value = "额外消耗数")
    private Integer extraCnt;


    @ApiModelProperty(value = "出库单号和分配数")
    private String deliveryMsg;
}
