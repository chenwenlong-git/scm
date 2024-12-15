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
 * 开发需求样品单描述
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_sample_order_process_desc")
@ApiModel(value = "DevelopSampleOrderProcessDescPo对象", description = "开发需求样品单描述")
public class DevelopSampleOrderProcessDescPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_sample_order_process_desc_id", type = IdType.ASSIGN_ID)
    private Long developSampleOrderProcessDescId;

    @ApiModelProperty(value = "开发需求母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;


    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;


    @ApiModelProperty(value = "名称")
    private String name;


    @ApiModelProperty(value = "描述值，多个以英文逗号分开")
    private String descValue;


}
