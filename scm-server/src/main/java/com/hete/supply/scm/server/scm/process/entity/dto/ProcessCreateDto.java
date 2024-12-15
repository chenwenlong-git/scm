package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "工序创建参数", description = "工序创建参数")
public class ProcessCreateDto {

    @ApiModelProperty("工序类别")
    @NotNull(message = "工序类别不能为空")
    private ProcessLabel processLabel;

    @ApiModelProperty("工序名称")
    @NotBlank(message = "工序名称不能为空")
    @Length(max = 32, message = "工序名称长度不能超过 32 位")
    private String processSecondName;

    @ApiModelProperty("工序环节")
    @NotNull(message = "工序环节不能为空")
    private ProcessFirst processFirst;

    @ApiModelProperty("工序提成")
    @NotNull(message = "工序提成不能为空")
    @DecimalMin(value = "0", message = "工序提成不能小于0")
    private BigDecimal commission;

    @ApiModelProperty(value = "额外提成单价")
    @NotNull(message = "额外提成单价不能为空")
    @DecimalMin(value = "0", message = "额外提成单价不能小于0")
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
    @NotEmpty(message = "工序规则不能为空")
    private List<@Valid ProcessCommissionRuleDto> processCommissionRuleDtoList;
}
