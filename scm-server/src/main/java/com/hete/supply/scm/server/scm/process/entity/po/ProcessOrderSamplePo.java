package com.hete.supply.scm.server.scm.process.entity.po;

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
 * 加工单生产信息表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_order_sample")
@ApiModel(value = "ProcessOrderSamplePo对象", description = "加工单生产信息表")
public class ProcessOrderSamplePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_sample_id", type = IdType.ASSIGN_ID)
    private Long processOrderSampleId;


    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "来源单据号（PLM生产属性主键）")
    private String sourceDocumentNumber;

    @ApiModelProperty(value = "属性key")
    private String sampleInfoKey;

    @ApiModelProperty(value = "属性值")
    private String sampleInfoValue;


}
