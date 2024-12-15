package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.supply.scm.server.supplier.entity.dto.RawReturnProductItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/6 16:00
 */
@Data
@NoArgsConstructor
public class PurchaseReturnRawDto {
    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "原料产品明细列表")
    @Valid
    @NotEmpty(message = "原料产品明细列表不能为空")
    private List<RawReturnProductItemDto> rawProductItemList;
}
