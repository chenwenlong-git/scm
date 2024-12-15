package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 工序管理表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process")
@ApiModel(value = "ProcessPo对象", description = "工序管理表")
public class ProcessPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_id", type = IdType.ASSIGN_ID)
    private Long processId;


    @ApiModelProperty(value = "一级工序")
    private ProcessFirst processFirst;


    @ApiModelProperty(value = "工序")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "工序类型")
    private ProcessType processType;

    @ApiModelProperty(value = "二级工序代码")
    private String processSecondCode;


    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;


    @ApiModelProperty(value = "工序代码")
    private String processCode;


    @ApiModelProperty(value = "工序名称")
    private String processName;


    @ApiModelProperty(value = "工序提成")
    private BigDecimal commission;

    @ApiModelProperty(value = "额外提成单价")
    private BigDecimal extraCommission;

    @ApiModelProperty(value = "工序整备时长（分钟）")
    private Integer setupDuration;

    @ApiModelProperty(value = "状态，ENABLED：启用、DISABLED：禁用")
    private ProcessStatus processStatus;

    @ApiModelProperty(value = "复杂系数")
    private Integer complexCoefficient;
}
