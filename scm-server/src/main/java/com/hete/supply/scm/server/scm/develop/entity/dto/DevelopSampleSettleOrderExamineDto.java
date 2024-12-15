package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleSettleExamine;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@Data
@NoArgsConstructor
public class DevelopSampleSettleOrderExamineDto {

    @NotNull(message = "样品结算单ID不能为空")
    @ApiModelProperty(value = "样品结算单ID")
    private Long developSampleSettleOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "样品结算单审核状态")
    private DevelopSampleSettleExamine developSampleSettleExamine;

    @ApiModelProperty(value = "审核拒绝备注")
    private String examineRefuseRemarks;

    @ApiModelProperty(value = "确认拒绝备注")
    private String settleRefuseRemarks;

}
