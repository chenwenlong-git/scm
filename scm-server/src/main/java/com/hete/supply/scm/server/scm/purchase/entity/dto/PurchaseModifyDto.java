package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/25 16:33
 */
@Data
@NoArgsConstructor
public class PurchaseModifyDto {
    @ApiModelProperty(value = "降档退货单号")
    @NotBlank(message = "降档退货单号不能为空")
    private String downReturnOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    @NotBlank(message = "采购子单单号不能为空")
    private String purchaseChildOrderNo;

    @NotBlank(message = "发货单号不能为空")
    @ApiModelProperty(value = "发货单号")
    private String deliverOrderNo;

    @NotNull(message = "是否全部退货不能为空")
    @ApiModelProperty(value = "是否全部退货")
    private BooleanType isAllReturn;

    @ApiModelProperty(value = "是否校验sku")
    private BooleanType isValidSku;

    @ApiModelProperty(value = "sku列表")
    @NotEmpty(message = "sku列表不能为空")
    @Valid
    private List<PurchaseModifySkuDto> purchaseModifySkuList;
}
