package com.hete.supply.scm.server.scm.production.entity.po;

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
 * 供应商商品工艺属性表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_sku_craft_attribute")
@ApiModel(value = "SupplierSkuCraftAttributePo对象", description = "供应商商品工艺属性表")
public class SupplierSkuCraftAttributePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_sku_craft_attribute_id", type = IdType.ASSIGN_ID)
    private Long supplierSkuCraftAttributeId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "sku编码")
    private String sku;


    @ApiModelProperty(value = "缠管")
    private String tubeWrapping;


    @ApiModelProperty(value = "根数")
    private Integer rootsCnt;


    @ApiModelProperty(value = "层数")
    private Integer layersCnt;


    @ApiModelProperty(value = "特殊处理")
    private String specialHandling;
}
