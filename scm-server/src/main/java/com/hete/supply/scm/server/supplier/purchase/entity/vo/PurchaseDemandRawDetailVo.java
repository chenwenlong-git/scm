package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/25 17:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDemandRawDetailVo {


    @ApiModelProperty(value = "原料列表")
    private List<PurchaseDemandRawVo> purchaseDemandRawList;
}
