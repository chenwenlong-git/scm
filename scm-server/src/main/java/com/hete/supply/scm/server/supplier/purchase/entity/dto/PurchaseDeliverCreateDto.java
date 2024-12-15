package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/25 10:32
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverCreateDto {
    @ApiModelProperty(value = "采购子单单号")
    @NotBlank(message = "采购子单单号不能为空")
    private String purchaseChildOrderNo;

    @NotBlank(message = "采购母单单号不能为空")
    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty("发货明细")
    @NotEmpty(message = "发货明细不能为空")
    @Valid
    private List<PurchaseDeliverCreateItemDto> purchaseDeliverItemDtoList;

    @ApiModelProperty(value = "发货单原料列表（额外消耗）")
    @Valid
    private List<PurchaseDeliverRawDto> purchaseDeliverRawList;
}
