package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/4/10 09:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseProductSplitDto extends PurchaseParentNoDto {


    @ApiModelProperty(name = "大货采购拆分子项")
    @NotEmpty(message = "大货采购拆分子项不能为空")
    @Valid
    private List<PurchaseProductSplitItemDto> purchaseProductSplitItemList;
}
