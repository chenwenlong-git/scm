package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/25 17:39
 */
@Data
@NoArgsConstructor
public class PurchaseModifyItemVo {
    @JsonIgnore
    @ApiModelProperty(value = "降档退货单号", hidden = true)
    private String downReturnOrderNo;

    @ApiModelProperty(value = "原sku")
    private String sku;


    @ApiModelProperty(value = "原sku数量")
    private Integer purchaseCnt;


    @ApiModelProperty(value = "新sku")
    private String newSku;

    @ApiModelProperty(value = "新sku数量")
    private Integer newPurchaseCnt;
}
