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
 * 工序模版关系表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_template_relation")
@ApiModel(value = "ProcessTemplateRelationPo对象", description = "工序模版关系表")
public class ProcessTemplateRelationPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_template_relation_id", type = IdType.ASSIGN_ID)
    private Long processTemplateRelationId;


    @ApiModelProperty(value = "工序模版 ID")
    private Long processTemplateId;


    @ApiModelProperty(value = "工序 ID")
    private Long processId;


    @ApiModelProperty(value = "序号")
    private Integer sort;


}
