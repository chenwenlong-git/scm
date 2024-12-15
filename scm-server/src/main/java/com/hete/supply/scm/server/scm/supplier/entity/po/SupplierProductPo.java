package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商产品
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_product")
@ApiModel(value = "SupplierProductPo对象", description = "供应商产品")
@Deprecated
public class SupplierProductPo extends BasePo {


    @ApiModelProperty(value = "主键ID")
    @TableId(value = "supplier_product_id", type = IdType.ASSIGN_ID)
    private Long supplierProductId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "SPU")
    private String spu;


    @ApiModelProperty(value = "变体属性")
    private String variantProperties;


    @ApiModelProperty(value = "创建人名称")
    private String createUsername;


    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;


}
