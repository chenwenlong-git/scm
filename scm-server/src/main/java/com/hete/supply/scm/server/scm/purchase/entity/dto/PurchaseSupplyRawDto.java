package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.server.scm.entity.dto.RawProductItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/6 15:13
 */
@Data
@NoArgsConstructor
public class PurchaseSupplyRawDto {
    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @NotBlank(message = "原料仓库编码不能为空")
    @ApiModelProperty(value = "原料仓库编码")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料产品明细列表")
    @Valid
    @NotEmpty(message = "原料产品明细列表不能为空")
    private List<RawProductItemDto> rawProductItemList;

}
