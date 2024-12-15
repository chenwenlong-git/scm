package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * plm的产品信息表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("plm_sku")
@ApiModel(value = "PlmSkuPo对象", description = "plm的产品信息表")
public class PlmSkuPo extends BaseSupplyPo {


    @ApiModelProperty(value = "ID")
    @TableId(value = "plm_sku_id", type = IdType.ASSIGN_ID)
    private Long plmSkuId;


    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "是否绑定供应商产品")
    private BindingSupplierProduct bindingSupplierProduct;


    @ApiModelProperty(value = "生产周期")
    private BigDecimal cycle;

}
