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
 * @date 2022/11/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseSplitDto extends PurchaseParentNoDto {
    @NotEmpty(message = "拆分子单项不能为空")
    @ApiModelProperty(name = "拆分子单项")
    @Valid
    private List<PurchaseSplitItemDto> purchaseSplitItemList;

}
