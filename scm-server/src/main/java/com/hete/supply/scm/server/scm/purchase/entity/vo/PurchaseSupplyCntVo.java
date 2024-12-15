package com.hete.supply.scm.server.scm.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/5/30 21:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseSupplyCntVo {
    @ApiModelProperty(value = "补交数")
    private Integer supplyPurchaseCnt;
}
