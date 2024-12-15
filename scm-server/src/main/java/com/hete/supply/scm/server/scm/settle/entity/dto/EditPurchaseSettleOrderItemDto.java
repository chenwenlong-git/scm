package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class EditPurchaseSettleOrderItemDto {

    @NotNull(message = "采购结算单ID不能为空")
    @ApiModelProperty(value = "采购结算单ID")
    private Long purchaseSettleOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "采购结算单明细ID不能为空")
    @ApiModelProperty(value = "采购结算单明细ID")
    private List<Long> purchaseSettleOrderItemIds;

    @NotNull(message = "约定结算时间不能为空")
    @ApiModelProperty(value = "约定结算时间")
    private LocalDateTime aboutSettleTime;

}
