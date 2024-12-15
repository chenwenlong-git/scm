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
 * 工序模板-加工单工序描述
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_template_process_order_description")
@ApiModel(value = "ProcessTemplateProcessOrderDescriptionPo对象", description = "工序模板-加工单工序描述")
public class ProcessTemplateProcessOrderDescriptionPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_template_process_order_description_id", type = IdType.ASSIGN_ID)
    private Long processTemplateProcessOrderDescriptionId;


    @ApiModelProperty(value = "工序模版主键id")
    private Long processTemplateId;


    @ApiModelProperty(value = "加工描述名称")
    private String processDescName;


    @ApiModelProperty(value = "加工描述值")
    private String processDescValue;


}
