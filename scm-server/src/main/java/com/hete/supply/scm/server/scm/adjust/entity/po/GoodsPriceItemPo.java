package com.hete.supply.scm.server.scm.adjust.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceItemStatus;
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
 * 商品价格操作明细记录表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_price_item")
@ApiModel(value = "GoodsPriceItemPo对象", description = "商品价格操作明细记录表")
public class GoodsPriceItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "goods_price_item_id", type = IdType.ASSIGN_ID)
    private Long goodsPriceItemId;

    @ApiModelProperty(value = "关联商品价格表 ID")
    private Long goodsPriceId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "渠道价格（前）")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "渠道价格(调价后的价格)")
    private BigDecimal channelPrice;


    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;


    @ApiModelProperty(value = "生效备注")
    private String effectiveRemark;

    @ApiModelProperty(value = "设置通用")
    private GoodsPriceUniversal goodsPriceUniversal;

    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;


    @ApiModelProperty(value = "审批状态")
    private GoodsPriceItemStatus goodsPriceItemStatus;

    @ApiModelProperty(value = "生效状态")
    private GoodsPriceEffectiveStatus goodsPriceEffectiveStatus;

}
