package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/16.
 */
@Data
public class ProcessBo {

    @ApiModelProperty(value = "主键id")
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
