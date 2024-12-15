package com.hete.supply.scm.server.scm.adjust.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品价格表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_price")
@ApiModel(value = "GoodsPricePo对象", description = "商品价格表")
public class GoodsPricePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "goods_price_id", type = IdType.ASSIGN_ID)
    private Long goodsPriceId;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "关联渠道ID")
    private Long channelId;


    @ApiModelProperty(value = "渠道名称")
    private String channelName;


    @ApiModelProperty(value = "渠道价格")
    private BigDecimal channelPrice;


    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;


    @ApiModelProperty(value = "生效备注")
    private String effectiveRemark;


    @ApiModelProperty(value = "设置通用")
    private GoodsPriceUniversal goodsPriceUniversal;


}
