package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/17 23:38
 */
@Data
@NoArgsConstructor
public class PurchasePlanConfirmDto {
    @ApiModelProperty(name = "拆分子项")
    @NotEmpty(message = "拆分子项不能为空")
    @Valid
    private List<PurchasePlanConfirmItemDto> purchasePlanConfirmItemList;

}
