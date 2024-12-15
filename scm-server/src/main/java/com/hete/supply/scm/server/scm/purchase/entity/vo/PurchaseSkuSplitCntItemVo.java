package com.hete.supply.scm.server.scm.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/6/12 10:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseSkuSplitCntItemVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "总采购数")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "已下单数")
    private Integer placedCnt;

    @ApiModelProperty(value = "可拆分数")
    private Integer splitCnt;
}
