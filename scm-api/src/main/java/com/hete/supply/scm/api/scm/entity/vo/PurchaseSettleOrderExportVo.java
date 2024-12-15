package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleItemType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class PurchaseSettleOrderExportVo {

    @ApiModelProperty(value = "结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "结算单状态")
    private PurchaseSettleStatus purchaseSettleStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "单据类型")
    private PurchaseSettleItemType purchaseSettleItemType;

    @ApiModelProperty(value = "单据时间")
    private LocalDateTime itemSettleTime;

    @ApiModelProperty(value = "单据状态")
    private String statusName;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "对账人")
    private String confirmUsername;

    @ApiModelProperty(value = "对账时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "财务审核时间")
    private LocalDateTime settleTime;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "对账金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "应扣款金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal payPrice;


}
