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
 * @date 2022/11/28 17:41
 */
@Data
@NoArgsConstructor
public class PurchaseRawCommitDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long purchaseRawReceiptOrderId;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;

    @ApiModelProperty(value = "采购原料收货单明细")
    @Valid
    @NotEmpty(message = "采购原料收货单明细不能为空")
    private List<PurchaseRawReceiptItemDto> purchaseRawReceiptItemList;

    @Data
    public static class PurchaseRawReceiptItemDto {
        @ApiModelProperty(value = "id")
        @NotNull(message = "id不能为空")
        private Long purchaseRawReceiptOrderItemId;

        @ApiModelProperty(value = "version")
        @NotNull(message = "version 不能为空")
        private Integer version;


        @ApiModelProperty(value = "收货数量")
        @NotNull(message = "收货数量不能为空")
        private Integer receiptCnt;
    }

}
