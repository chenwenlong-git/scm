package com.hete.supply.scm.server.scm.production.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.CrotchLength;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 供应商商品原料属性表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_sku_material_attribute")
@ApiModel(value = "SupplierSkuMaterialAttributePo对象", description = "供应商商品原料属性表")
public class SupplierSkuMaterialAttributePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_sku_material_attribute_id", type = IdType.ASSIGN_ID)
    private Long supplierSkuMaterialAttributeId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "裆长尺寸")
    private CrotchLength crotchLength;

    @ApiModelProperty(value = "裆长部位")
    private String crotchPosition;

    @ApiModelProperty(value = "深色克重")
    private BigDecimal darkWeight;


    @ApiModelProperty(value = "浅色克重")
    private BigDecimal lightWeight;


    @ApiModelProperty(value = "裆长配比")
    private String crotchLengthRatio;


    @ApiModelProperty(value = "克重")
    private BigDecimal weight;
}
