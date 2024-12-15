package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/5/30.
 */
@Data
@NoArgsConstructor
public class FinanceSettleOrderExportVo {
    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

    @ApiModelProperty(value = "结算单状态")
    private String settleOrderStatus;

    @ApiModelProperty(value = "工厂编码")
    private String supplierCode;

    @ApiModelProperty(value = "申请日期")
    private String applyTime;

    @ApiModelProperty(value = "应付总金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "应收总金额")
    private BigDecimal receiveAmount;

    @ApiModelProperty(value = "结算总金额")
    private BigDecimal settleAmount;

    @ApiModelProperty(value = "已付款金额")
    private BigDecimal paidAmount;

    @ApiModelProperty(value = "处理人")
    private String ctrlUsername;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "工厂提交时间")
    private String supplierSubmitTime;

    @ApiModelProperty(value = "跟单确认人")
    private String followConfirmUsername;

    @ApiModelProperty(value = "跟单确认时间")
    private String followerConfirmTime;

    @ApiModelProperty(value = "审批完成时间")
    private String workflowFinishTime;

    @ApiModelProperty(value = "结算完成时间")
    private String settleFinishTime;

    @ApiModelProperty(value = "结转单号")
    private String financeSettleCarryoverOrderNo;

    @ApiModelProperty(value = "结转单状态")
    private String financeSettleCarryoverOrderStatus;

    @ApiModelProperty(value = "待结转金额")
    private BigDecimal availableCarryoverAmount;
}
