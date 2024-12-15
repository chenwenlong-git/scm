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
 * 核价单表详情信息
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_pricing_order_info")
@ApiModel(value = "DevelopPricingOrderInfoPo对象", description = "核价单表详情信息")
public class DevelopPricingOrderInfoPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_pricing_order_info_id", type = IdType.ASSIGN_ID)
    private Long developPricingOrderInfoId;


    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "开发需求母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;


    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;


    @ApiModelProperty(value = "普通")
    private String ordinary;


    @ApiModelProperty(value = "前头发块尺寸")
    private String frontSize;


    @ApiModelProperty(value = "手织毛尺寸")
    private String handWeavingSize;


    @ApiModelProperty(value = "单价")
    private BigDecimal price;


    @ApiModelProperty(value = "手织克重")
    private BigDecimal gramWeight;


    @ApiModelProperty(value = "手织毛金额")
    private BigDecimal handWeavingPrice;


    @ApiModelProperty(value = "手勾费")
    private BigDecimal handHookPrice;


    @ApiModelProperty(value = "生产费用")
    private BigDecimal productionPrice;


    @ApiModelProperty(value = "网帽")
    private BigDecimal meshCap;


    @ApiModelProperty(value = "曲度费")
    private BigDecimal curvaturePrice;


    @ApiModelProperty(value = "染色费")
    private BigDecimal stainPrice;


    @ApiModelProperty(value = "工厂利润")
    private BigDecimal factoryProfit;


    @ApiModelProperty(value = "合计成本")
    private BigDecimal cost;


    @ApiModelProperty(value = "报价")
    private BigDecimal quotedPrice;


    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "大货价格")
    private BigDecimal bulkPrice;


    @ApiModelProperty(value = "备注")
    private String remarks;


    @ApiModelProperty(value = "半成品加权总价")
    private BigDecimal weightedPrice;


    @ApiModelProperty(value = "加工工序费")
    private BigDecimal secondPrice;


    @ApiModelProperty(value = "管理费")
    private BigDecimal managePrice;


    @ApiModelProperty(value = "总成本价")
    private BigDecimal costTotalPrice;


}
