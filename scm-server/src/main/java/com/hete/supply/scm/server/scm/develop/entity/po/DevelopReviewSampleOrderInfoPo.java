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

/**
 * <p>
 * 开发审版关联样品单属性表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_review_sample_order_info")
@ApiModel(value = "DevelopReviewSampleOrderInfoPo对象", description = "开发审版关联样品单属性表")
public class DevelopReviewSampleOrderInfoPo extends BaseSupplyPo {


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


    @ApiModelProperty(value = "属性key")
    private String sampleInfoKey;


    @ApiModelProperty(value = "属性值")
    private String sampleInfoValue;


    @ApiModelProperty(value = "评估意见")
    private String evaluationOpinion;

    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

}
