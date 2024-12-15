package com.hete.supply.scm.server.scm.adjust.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 调价审批明细表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("adjust_price_approve_item")
@ApiModel(value = "AdjustPriceApproveItemPo对象", description = "调价审批明细表")
public class AdjustPriceApproveItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "adjust_price_approve_item_id", type = IdType.ASSIGN_ID)
    private Long adjustPriceApproveItemId;


    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;


    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "采购订单号")
    private String purchaseChildOrderNo;


    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "原价（调整前）")
    private BigDecimal originalPrice;


    @ApiModelProperty(value = "调整价格（调整后）")
    private BigDecimal adjustPrice;


    @ApiModelProperty(value = "调价备注")
    private String adjustRemark;


    @ApiModelProperty(value = "调价事由")
    private String adjustReason;


    @ApiModelProperty(value = "渠道名称")
    private String channelName;


    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;


    @ApiModelProperty(value = "生效备注")
    private String effectiveRemark;


    @ApiModelProperty(value = "设置通用：是(TRUE)、否(FALSE)")
    private String universal;

    @ApiModelProperty(value = "商品调价明细的ID")
    private Long goodsPriceItemId;


}
