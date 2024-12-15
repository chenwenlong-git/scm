package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowApproveDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/5/31 09:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RecoOrderApproveDto extends WorkflowApproveDto {

    @ApiModelProperty(value = "对账单号")
    @NotBlank(message = "对账单号不能为空")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;


}
