package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowApproveDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/5/31.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SettleOrderApproveDto extends WorkflowApproveDto {

    @ApiModelProperty(value = "对账单号")
    @NotBlank(message = "对账单号不能为空")
    private String settleOrderNo;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;
}
