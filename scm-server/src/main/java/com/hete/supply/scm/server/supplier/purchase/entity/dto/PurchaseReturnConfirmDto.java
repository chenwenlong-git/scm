package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/20 17:15
 */
@Data
@NoArgsConstructor
public class PurchaseReturnConfirmDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long purchaseReturnOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotEmpty(message = "退货单明细列表不能为空")
    @ApiModelProperty(value = "退货单明细列表")
    @Valid
    private List<PurchaseReturnItemDto> purchaseReturnItemList;

}
