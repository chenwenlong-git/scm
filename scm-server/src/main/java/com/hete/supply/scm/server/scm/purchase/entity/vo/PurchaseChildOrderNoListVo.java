package com.hete.supply.scm.server.scm.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/9/1 10:20
 */
@Data
@NoArgsConstructor
public class PurchaseChildOrderNoListVo {
    @ApiModelProperty(value = "采购单号")
    private List<String> purchaseChildOrderNoList;
}
