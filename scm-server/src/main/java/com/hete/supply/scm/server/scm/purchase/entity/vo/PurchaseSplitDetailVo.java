package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.server.scm.entity.vo.RawProductItemVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/15 20:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseSplitDetailVo {
    @ApiModelProperty(name = "采购子单回显明细")
    private List<PurchaseChildOrderVo> purchaseChildOrderList;

    @ApiModelProperty(name = "采购子单原料回显明细")
    private List<RawProductItemVo> rawProductItemList;
}
