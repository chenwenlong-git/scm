package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SampleRawBizType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 样品需求子单原料
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_child_order_raw")
@ApiModel(value = "SampleChildOrderRawPo对象", description = "样品需求子单原料")
public class SampleChildOrderRawPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_child_order_raw_id", type = IdType.ASSIGN_ID)
    private Long sampleChildOrderRawId;


    @ApiModelProperty(value = "样品采购子单id")
    private Long sampleChildOrderId;


    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;


    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "数量")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "原料类型")
    private SampleRawBizType sampleRawBizType;


}
