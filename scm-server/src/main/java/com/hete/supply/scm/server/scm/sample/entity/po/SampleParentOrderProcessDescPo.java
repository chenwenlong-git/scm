package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 样品需求母单工序描述
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_parent_order_process_desc")
@ApiModel(value = "SampleParentOrderProcessDescPo对象", description = "样品需求母单工序描述")
public class SampleParentOrderProcessDescPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_parent_order_process_desc_id", type = IdType.ASSIGN_ID)
    private Long sampleParentOrderProcessDescId;


    @ApiModelProperty(value = "样品采购母单id")
    private Long sampleParentOrderId;


    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;


    @ApiModelProperty(value = "名称")
    private String name;


    @ApiModelProperty(value = "描述值")
    private String descValue;


}
