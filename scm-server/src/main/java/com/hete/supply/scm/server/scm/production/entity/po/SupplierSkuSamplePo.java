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
 * 供应商商品样品表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_sku_sample")
@ApiModel(value = "SupplierSkuSamplePo对象", description = "供应商商品样品表")
public class SupplierSkuSamplePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_sku_sample_id", type = IdType.ASSIGN_ID)
    private Long supplierSkuSampleId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "sku编码")
    private String sku;


    @ApiModelProperty(value = "来源单据号")
    private String sourceOrderNo;


}
