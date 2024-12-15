package com.hete.supply.scm.server.scm.sample.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/3/30 16:24
 */
@Data
@NoArgsConstructor
public class SampleProcessDto {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderProcessId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "二级工序代码不能为空")
    @ApiModelProperty(value = "二级工序代码")
    private String processSecondCode;

    @NotBlank(message = "二级工序名称不能为空")
    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @NotBlank(message = "工序代码不能为空")
    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @NotBlank(message = "工序名称不能为空")
    @ApiModelProperty(value = "工序名称")
    private String processName;

    @NotNull(message = "工序不能为空")
    @ApiModelProperty("工序")
    private ProcessLabel processLabel;
}
