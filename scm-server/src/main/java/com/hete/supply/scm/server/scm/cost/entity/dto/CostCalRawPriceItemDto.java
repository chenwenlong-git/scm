package com.hete.supply.scm.server.scm.cost.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/2/28 11:19
 */
@Data
@NoArgsConstructor
public class CostCalRawPriceItemDto {
    @NotBlank(message = "采购子单号不能为空")
    @ApiModelProperty(value = "采购子单号")
    private String purchaseChildOrderNo;

    @NotEmpty(message = "原料列表不能为空")
    @Valid
    @ApiModelProperty(value = "原料列表")
    private List<RawItemDto> rawItemList;
}
