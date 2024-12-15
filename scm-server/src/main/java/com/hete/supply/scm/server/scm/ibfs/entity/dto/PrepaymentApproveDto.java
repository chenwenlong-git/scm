package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowApproveDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/5/30 19:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PrepaymentApproveDto extends WorkflowApproveDto {
    @NotBlank(message = "预付款单号不能为空")
    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;
}
