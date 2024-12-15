package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/17 23:53
 */
@Data
@NoArgsConstructor
public class ProductPurchaseConfirmDto {
    @ApiModelProperty(name = "大货采购拆分子项")
    @NotEmpty(message = "大货采购拆分子项不能为空")
    @Valid
    private List<ProductPurchaseConfirmItemDto> productPurchaseConfirmItemList;
}
