package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/12/25 17:21
 */
@Data
@NoArgsConstructor
public class PurchaseBatchCancelDto {
    @NotEmpty(message = "取消列表不能为空")
    @ApiModelProperty(value = "取消列表")
    private List<PurchaseCancelDto> purchaseCancelList;
}
