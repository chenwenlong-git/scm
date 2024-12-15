package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 开发审版关联样品单表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_review_sample_order")
@ApiModel(value = "DevelopReviewSampleOrderPo对象", description = "开发审版关联样品单表")
public class DevelopReviewSampleOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_review_sample_order_id", type = IdType.ASSIGN_ID)
    private Long developReviewSampleOrderId;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;


    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;


    @ApiModelProperty(value = "克重")
    private BigDecimal gramWeight;


    @ApiModelProperty(value = "样品需求")
    private DevelopSampleDemand developSampleDemand;


    @ApiModelProperty(value = "样品质量")
    private DevelopSampleQuality developSampleQuality;


    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;


    @ApiModelProperty(value = "样品新旧程度")
    private DevelopSampleNewness developSampleNewness;


    @ApiModelProperty(value = "样品阶段")
    private DevelopSampleStage developSampleStage;


    @ApiModelProperty(value = "样品开发意见")
    private String developSampleDevOpinion;


    @ApiModelProperty(value = "样品质量意见")
    private String developSampleQltyOpinion;


    @ApiModelProperty(value = "毛发异常类型：脱发、断发")
    private String abnormalHair;


    @ApiModelProperty(value = "浮发情况")
    private String floatingHair;


    @ApiModelProperty(value = "网帽服帖度")
    private String meshCapFit;


    @ApiModelProperty(value = "毛发手感")
    private String hairFeel;

    @ApiModelProperty(value = "样品需求来源")
    private DevelopReviewSampleSource developReviewSampleSource;


}
