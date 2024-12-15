package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/3 10:11
 */
@Data
@NoArgsConstructor
public class PurchaseReturnItemVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "实际退货数量")
    private Integer realityReturnCnt;
}
