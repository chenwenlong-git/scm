package com.hete.supply.scm.server.scm.settle.entity.dto;

import com.hete.supply.scm.server.scm.settle.enums.PurchaseSettleExamine;
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
public class PurchaseSettleOrderExamineDto {

    @NotNull(message = "采购结算单ID不能为空")
    @ApiModelProperty(value = "采购结算单ID")
    private Long purchaseSettleOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "采购结算单审核状态")
    private PurchaseSettleExamine purchaseSettleExamine;

    @ApiModelProperty(value = "审核拒绝备注")
    private String examineRefuseRemarks;

    @ApiModelProperty(value = "确认拒绝备注")
    private String settleRefuseRemarks;


}
