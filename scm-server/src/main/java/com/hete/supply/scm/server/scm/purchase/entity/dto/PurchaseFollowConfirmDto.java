package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 17:05
 */
@Data
@NoArgsConstructor
public class PurchaseFollowConfirmDto {
    @NotEmpty(message = "采购拆分子项不能为空")
    @Valid
    @ApiModelProperty(name = "采购拆分子项")
    private List<PurchaseFollowConfirmItemDto> purchaseFollowConfirmItemList;
}
