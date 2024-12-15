package com.hete.supply.scm.server.scm.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/8 20:16
 */
@Data
@NoArgsConstructor
public class SearchInventoryVo {
    @ApiModelProperty(value = "id")
    private Long supplierInventoryId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "分类名(一级或二级名称)")
    private String categoryName;


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
}
