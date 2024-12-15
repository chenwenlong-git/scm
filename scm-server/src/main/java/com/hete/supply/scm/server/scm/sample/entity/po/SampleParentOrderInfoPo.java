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
 * 样品需求母单详细信息
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_parent_order_info")
@ApiModel(value = "SampleParentOrderInfoPo对象", description = "样品需求母单详细信息")
public class SampleParentOrderInfoPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_parent_order_info_id", type = IdType.ASSIGN_ID)
    private Long sampleParentOrderInfoId;


    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;


    @ApiModelProperty(value = "属性key")
    private String sampleInfoKey;


    @ApiModelProperty(value = "属性值")
    private String sampleInfoValue;


}
