package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 核价单表详情机制信息
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_pricing_order_mechanism")
@ApiModel(value = "DevelopPricingOrderMechanismPo对象", description = "核价单表详情机制信息")
public class DevelopPricingOrderMechanismPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_pricing_order_mechanism_id", type = IdType.ASSIGN_ID)
    private Long developPricingOrderMechanismId;


    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;


    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;


    @ApiModelProperty(value = "机制毛尺寸")
    private String hairSize;


    @ApiModelProperty(value = "克重")
    private BigDecimal gramWeight;


    @ApiModelProperty(value = "单价")
    private BigDecimal price;


    @ApiModelProperty(value = "机制毛金额")
    private BigDecimal hairPrice;


}
