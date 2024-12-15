package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商库存表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_inventory")
@ApiModel(value = "SupplierInventoryPo对象", description = "供应商库存表")
public class SupplierInventoryPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_inventory_id", type = IdType.ASSIGN_ID)
    private Long supplierInventoryId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

    @ApiModelProperty(value = "分类名(一级或二级名称)")
    private String categoryName;


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
