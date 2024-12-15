package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.supply.scm.api.scm.entity.enums.SampleResultStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 样品子单结果列表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_child_order_result")
@ApiModel(value = "SampleChildOrderResultPo对象", description = "样品子单结果列表")
public class SampleChildOrderResultPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_child_order_result_id", type = IdType.ASSIGN_ID)
    private Long sampleChildOrderResultId;


    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;


    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;


    @ApiModelProperty(value = "选样结果")
    private SampleResult sampleResult;


    @ApiModelProperty(value = "关联单据号")
    private String relateOrderNo;

    @ApiModelProperty(value = "选样数量")
    private Integer sampleCnt;

    @ApiModelProperty(value = "样品结果编号")
    private String sampleResultNo;

    @ApiModelProperty(value = "选样结果说明")
    private String remark;

    @ApiModelProperty(value = "关联单据状态")
    private SampleResultStatus sampleResultStatus;

}
