package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 核价单表列表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_pricing_order")
@ApiModel(value = "DevelopPricingOrderPo对象", description = "核价单表列表")
public class DevelopPricingOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_pricing_order_id", type = IdType.ASSIGN_ID)
    private Long developPricingOrderId;


    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;


    @ApiModelProperty(value = "开发需求母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;


    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;


    @ApiModelProperty(value = "状态")
    private DevelopPricingOrderStatus developPricingOrderStatus;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "提交人")
    private String submitUser;


    @ApiModelProperty(value = "提交人的名称")
    private String submitUsername;


    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;


    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUser;


    @ApiModelProperty(value = "核价人的名称")
    private String nuclearPriceUsername;


    @ApiModelProperty(value = "核价时间")
    private LocalDateTime nuclearPriceTime;


    @ApiModelProperty(value = "母单创建人")
    private String parentCreateUser;

    @ApiModelProperty(value = "母单创建人")
    private String parentCreateUsername;

    @ApiModelProperty(value = "母单创建时间")
    private LocalDateTime parentCreateTime;


}
