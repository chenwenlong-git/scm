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
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/2/26.
 */
@Data
@ApiModel(value = "复合工序创建参数", description = "工序创建参数")
public class CompoundProcessCreateDto {
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

    @ApiModelProperty(value = "复杂系数")
    @NotNull(message = "复杂系数不能为空")
    @DecimalMin(value = "0", message = "复杂系数不能小于0")
    private Integer complexCoefficient;

    @NotNull(message = "工序整备时长（分钟）不能为空")
    @Min(value = 0, message = "工序整备时长（分钟）不能小于0")
    @ApiModelProperty(value = "工序整备时长（分钟）")
    private Integer setupDuration;

    @NotEmpty(message = "非组合工序信息不能为空")
    @ApiModelProperty(value = "非复合工序信息列表")
    @JsonProperty("independentProcesses")
    private List<@Valid IndependentProcessDto> independentProcessDtoList;

    @Data
    public static class IndependentProcessDto {
        @ApiModelProperty("工序编码")
        @NotNull(message = "工序编码不能为空")
        private String processCode;
    }

}
