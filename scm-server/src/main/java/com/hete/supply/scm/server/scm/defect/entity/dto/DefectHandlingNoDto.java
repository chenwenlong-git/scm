package com.hete.supply.scm.server.scm.defect.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/6/30 16:28
 */
@Data
@NoArgsConstructor
public class DefectHandlingNoDto {
    @NotBlank(message = "次品处理单号不能为空")
    @ApiModelProperty(value = "次品处理单号")
    private String defectHandlingNo;
}
