package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class ProcessVo {

    @ApiModelProperty(value = "工序 ID")
    private Long processId;

    @ApiModelProperty(value = "版本")
    private int version;

    @ApiModelProperty(value = "工序类别")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "工序类型")
    private ProcessType processType;

    @ApiModelProperty(value = "工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "工序环节")
    private ProcessFirst processFirst;

    @ApiModelProperty(value = "状态，ENABLED：启用、DISABLED：禁用")
    private ProcessStatus processStatus;

    @ApiModelProperty(value = "基础单价")
    private BigDecimal commission;

    @ApiModelProperty(value = "额外提成单价")
    private BigDecimal extraCommission;

    @ApiModelProperty(value = "复杂系数")
    private Integer complexCoefficient;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "二级工序代码")
    private String processSecondCode;

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "工序整备时长（分钟）")
    private Integer setupDuration;

    @JsonProperty(value = "processCommissionRules")
    @ApiModelProperty(value = "工序规则提成列表")
    private List<ProcessCommissionRuleVo> processCommissionRuleVoList;

    @JsonProperty(value = "independentProcesses")
    @ApiModelProperty(value = "关联非组合工序信息")
    private List<IndependentProcessVo> independentProcessVoList;

    @Data
    public static class IndependentProcessVo {
        @ApiModelProperty(value = "工序代码")
        private String processCode;

        @ApiModelProperty(value = "工序类别")
        private ProcessLabel processLabel;

        @ApiModelProperty(value = "工序名称")
        private String processSecondName;
    }

}
