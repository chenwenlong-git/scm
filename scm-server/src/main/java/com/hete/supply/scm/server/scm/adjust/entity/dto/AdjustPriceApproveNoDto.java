package com.hete.supply.scm.server.scm.adjust.entity.dto;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowApproveDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/6/17 18:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AdjustPriceApproveNoDto extends WorkflowApproveDto {
    @NotBlank(message = "审批单号不能为空")
    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;
}
