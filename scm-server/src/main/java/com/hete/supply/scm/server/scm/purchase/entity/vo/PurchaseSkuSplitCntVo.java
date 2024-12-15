package com.hete.supply.scm.server.scm.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/12 10:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseSkuSplitCntVo {
    @ApiModelProperty(value = "采购单sku可拆分数列表")
    private List<PurchaseSkuSplitCntItemVo> purchaseSkuSplitCntItemList;
}
