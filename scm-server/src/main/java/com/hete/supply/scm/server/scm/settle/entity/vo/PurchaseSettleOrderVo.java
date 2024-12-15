package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class PurchaseSettleOrderVo {

    @ApiModelProperty(value = "结算单ID")
    private Long purchaseSettleOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "结算单状态")
    private PurchaseSettleStatus purchaseSettleStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "应付总金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "对账时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "核算审核时间")
    private LocalDateTime settleTime;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "已支付金额")
    private BigDecimal alreadyPayPrice;

    @ApiModelProperty(value = "待支付金额")
    private BigDecimal waitPayPrice;

}
