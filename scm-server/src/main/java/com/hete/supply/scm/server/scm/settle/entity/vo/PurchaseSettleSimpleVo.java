package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/21 15:49
 */
@Data
@NoArgsConstructor
public class PurchaseSettleSimpleVo {
    @ApiModelProperty(value = "采购结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "采购结算单状态")
    private PurchaseSettleStatus purchaseSettleStatus;

    @ApiModelProperty(value = "应付总金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;
}
