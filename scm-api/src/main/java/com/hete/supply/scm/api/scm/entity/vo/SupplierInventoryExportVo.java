package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.InventoryStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/22 20:38
 */
@Data
@NoArgsConstructor
public class SupplierInventoryExportVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "商品二级类目")
    private String categoryName;

    @ApiModelProperty(value = "库存状态")
    private InventoryStatus inventoryStatus;

    @ApiModelProperty(value = "库存状态")
    private String inventoryStatusStr;

    @ApiModelProperty(value = "在库-备货库存")
    private Integer insStockUpInventory;

    @ApiModelProperty(value = "在库-自备库存")
    private Integer insSelfProvideInventory;

    @ApiModelProperty(value = "在库-不良库存")
    private Integer insDefectiveInventory;

    @ApiModelProperty(value = "可用-备货库存")
    private Integer stockUpInventory;

    @ApiModelProperty(value = "可用-自备库存")
    private Integer selfProvideInventory;

    @ApiModelProperty(value = "可用-不良库存")
    private Integer defectiveInventory;


    @ApiModelProperty(value = "冻结-备货库存")
    private Integer frzStockUpInventory;

    @ApiModelProperty(value = "冻结-自备库存")
    private Integer frzSelfProvideInventory;

    @ApiModelProperty(value = "冻结-不良库存")
    private Integer frzDefectiveInventory;

    @ApiModelProperty(value = "已发未回")
    private Integer deliveryNotReturnCnt;

    @ApiModelProperty(value = "蕾丝面积")
    private String laceArea;

    @ApiModelProperty(value = "完成长尺寸")
    private String completeLongSize;

    @ApiModelProperty(value = "材料")
    private String material;
}
