package com.hete.supply.scm.server.scm.purchase.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品成本详情获取采购的信息BO
 *
 * @author ChenWenLong
 * @date 2024/2/27 11:20
 */
@Data
@NoArgsConstructor
public class PurchaseItemByReceiveTimeBo {

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;

}
