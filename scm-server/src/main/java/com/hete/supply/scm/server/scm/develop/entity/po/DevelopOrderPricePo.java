package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 开发单相关单据大货价格
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_order_price")
@ApiModel(value = "DevelopOrderPricePo对象", description = "开发单相关单据大货价格")
public class DevelopOrderPricePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_order_price_id", type = IdType.ASSIGN_ID)
    private Long developOrderPriceId;


    @ApiModelProperty(value = "相关单据单号")
    private String developOrderNo;


    @ApiModelProperty(value = "单据价格类型")
    private DevelopOrderPriceType developOrderPriceType;


    @ApiModelProperty(value = "关联渠道ID（旧数据时为空）")
    private Long channelId;


    @ApiModelProperty(value = "渠道名称（旧数据时为空）")
    private String channelName;


    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "默认设置样品价格")
    private BooleanType isDefaultPrice;


}
