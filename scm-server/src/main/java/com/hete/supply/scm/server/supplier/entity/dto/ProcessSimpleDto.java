package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/7/29 17:40
 */
@Data
@NoArgsConstructor
public class ProcessSimpleDto {
    @NotNull(message = "工序id不能为空")
    @ApiModelProperty(value = "工序id")
    private Long processId;

    @NotBlank(message = "工序名称不能为空")
    @ApiModelProperty(value = "工序名称")
    private String processSecondName;

    @NotNull(message = "工序产能数不能为空")
    @ApiModelProperty(value = "工序产能数")
    @Min(value = 1, message = "产能数必须为正整数")
    private Integer processNum;


}
