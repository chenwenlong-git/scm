package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "工序编辑参数", description = "工序编辑参数")
public class ProcessEditDto {

    @ApiModelProperty("工序 ID")
    @NotNull(message = "工序 ID 不能为空")
    private Long processId;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @ApiModelProperty("工序类别")
    @NotNull(message = "工序类别不能为空")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "工序类型")
    @NotNull(message = "工序类型不能为空")
    private ProcessType processType;

    @ApiModelProperty("工序名称")
    @NotBlank(message = "二级工序名称不能为空")
    @Length(max = 32, message = "二级工序字符长度不能超过 32 位")
    private String processSecondName;

    @ApiModelProperty("工序环节")
    @NotNull(message = "工序环节不能为空")
    private ProcessFirst processFirst;

    @ApiModelProperty("工序提成")
    private BigDecimal commission;

    @ApiModelProperty(value = "额外提成单价")
    private BigDecimal extraCommission;

    @ApiModelProperty(value = "复杂系数")
    @NotNull(message = "复杂系数不能为空")
    @DecimalMin(value = "0", message = "复杂系数不能小于0")
    private Integer complexCoefficient;

    @NotNull(message = "工序整备时长（分钟）不能为空")
    @Min(value = 0, message = "工序整备时长（分钟）不能小于0")
    @ApiModelProperty(value = "工序整备时长（分钟）")
    private Integer setupDuration;

    @ApiModelProperty(value = "工序规则")
    @JsonProperty(value = "processCommissionRules")
    private List<@Valid ProcessCommissionRuleDto> processCommissionRuleDtoList;

    @ApiModelProperty(value = "非复合工序信息列表")
    @JsonProperty("independentProcesses")
    private List<CompoundProcessCreateDto.@Valid IndependentProcessDto> independentProcessDtoList;

    @Data
    public static class IndependentProcessDto {
        @ApiModelProperty("工序编码")
        @NotNull(message = "工序编码不能为空")
        private String processCode;
    }
}
