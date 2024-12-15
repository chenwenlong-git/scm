package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/10 09:34
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverDetailListVo {
    @ApiModelProperty(value = "详情列表")
    private List<PurchaseDeliverDetailVo> purchaseDeliverDetailList;
}
