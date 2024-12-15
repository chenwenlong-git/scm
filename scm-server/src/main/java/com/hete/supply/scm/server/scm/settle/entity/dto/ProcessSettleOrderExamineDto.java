package com.hete.supply.scm.server.scm.settle.entity.dto;

import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleExamine;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class ProcessSettleOrderExamineDto {

    @NotNull(message = "加工结算单ID不能为空")
    @ApiModelProperty(value = "加工结算单ID")
    private Long processSettleOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "加工结算单审核状态")
    private ProcessSettleExamine processSettleExamine;

    @ApiModelProperty(value = "审核拒绝备注")
    private String examineRefuseRemarks;


}
