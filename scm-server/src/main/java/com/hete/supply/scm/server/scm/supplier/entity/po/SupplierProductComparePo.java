package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商产品对照表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_product_compare")
@ApiModel(value = "SupplierProductComparePo对象", description = "供应商产品对照表")
public class SupplierProductComparePo extends BaseSupplyPo {


    @ApiModelProperty(value = "ID")
    @TableId(value = "supplier_product_compare_id", type = IdType.ASSIGN_ID)
    private Long supplierProductCompareId;


    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;


    @ApiModelProperty(value = "plm的产品信息表ID（不维护）")
    @Deprecated
    private Long plmSkuId;


    @ApiModelProperty(value = "SKU")
    private String sku;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "是否启用状态:启用(TRUE)、禁用(FALSE)")
    private BooleanType supplierProductCompareStatus;


}
