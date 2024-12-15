package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseSettleOrderSearchDto extends ComPageDto {

    @ApiModelProperty(value = "结算单ID")
    private Long purchaseSettleOrderId;

    @ApiModelProperty(value = "结算单IDS")
    private List<Long> purchaseSettleOrderIds;

    @ApiModelProperty(value = "结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "结算单状态")
    private List<PurchaseSettleStatus> purchaseSettleStatusList;

    @ApiModelProperty(value = "扣款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "补款单号")
    private String deductOrderNo;

    @ApiModelProperty(value = "对账人")
    private String confirmUser;

    @ApiModelProperty(value = "对账人用户名")
    private String confirmUsername;

    @ApiModelProperty(value = "供应商确认人")
    private String examineUser;

    @ApiModelProperty(value = "供应商确认人用户名")
    private String examineUsername;

    @ApiModelProperty(value = "财务审核人")
    private String settleUser;

    @ApiModelProperty(value = "财务审核人用户名")
    private String settleUsername;

    @ApiModelProperty(value = "支付人")
    private String payUser;

    @ApiModelProperty(value = "支付人用户名")
    private String payUsername;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "下单时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "下单时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "对账时间开始")
    private LocalDateTime confirmTimeStart;

    @ApiModelProperty(value = "对账时间结束")
    private LocalDateTime confirmTimeEnd;

    @ApiModelProperty(value = "供应商确认时间开始")
    private LocalDateTime examineTimeStart;

    @ApiModelProperty(value = "供应商确认时间结束")
    private LocalDateTime examineTimeEnd;

    @ApiModelProperty(value = "财务审核时间开始")
    private LocalDateTime settleTimeStart;

    @ApiModelProperty(value = "财务审核时间结束")
    private LocalDateTime settleTimeEnd;

    @ApiModelProperty(value = "支付完成时间开始")
    private LocalDateTime payTimeStart;

    @ApiModelProperty(value = "支付完成时间结束")
    private LocalDateTime payTimeEnd;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "排除结算单状态")
    private List<PurchaseSettleStatus> notPurchaseSettleStatusList;

    @ApiModelProperty(value = "大货采购子单/加工采购子单/样品子单/补扣款单编号")
    private String businessNo;


}
