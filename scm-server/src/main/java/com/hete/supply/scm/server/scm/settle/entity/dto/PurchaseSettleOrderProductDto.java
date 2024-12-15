package com.hete.supply.scm.server.scm.settle.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleItemType;
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
public class PurchaseSettleOrderProductDto {

    @NotNull(message = "结算单ID不能为空")
    @ApiModelProperty(value = "结算单ID")
    private Long purchaseSettleOrderId;

    @ApiModelProperty(value = "单据类型")
    private PurchaseSettleItemType purchaseSettleItemType;

    @ApiModelProperty(value = "单据号")
    private String businessNo;

}
