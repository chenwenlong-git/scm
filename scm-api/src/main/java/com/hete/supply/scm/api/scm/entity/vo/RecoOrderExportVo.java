package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReconciliationCycle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/5/20 11:27
 */
@Data
@NoArgsConstructor
public class RecoOrderExportVo {

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "供应商")
    private String supplierCode;

    @ApiModelProperty(value = "对账单状态")
    private FinanceRecoOrderStatus financeRecoOrderStatus;

    @ApiModelProperty(value = "对账单状态")
    private String financeRecoOrderStatusName;

    @ApiModelProperty(value = "对账周期开始时间")
    private LocalDateTime reconciliationStartTime;

    @ApiModelProperty(value = "对账周期开始时间")
    private String reconciliationStartTimeStr;

    @ApiModelProperty(value = "对账周期结束时间")
    private LocalDateTime reconciliationEndTime;

    @ApiModelProperty(value = "对账周期结束时间")
    private String reconciliationEndTimeStr;

    @ApiModelProperty(value = "对账周期")
    private ReconciliationCycle reconciliationCycle;

    @ApiModelProperty(value = "对账周期")
    private String reconciliationCycleName;

    @ApiModelProperty(value = "对账总金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "应收总金额")
    private BigDecimal receivePrice;

    @ApiModelProperty(value = "应付总金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty(value = "关联结算单号")
    private String financeSettleOrderNo;

    @ApiModelProperty(value = "结算单状态")
    private String financeSettleOrderStatusName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "收单时间")
    private LocalDateTime collectOrderTime;

    @ApiModelProperty(value = "收单时间")
    private String collectOrderTimeStr;

    @ApiModelProperty(value = "跟单确认人")
    private String confirmUsername;

    @ApiModelProperty(value = "跟单确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "跟单确认时间")
    private String confirmTimeStr;

    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime supplierConfirmTime;

    @ApiModelProperty(value = "供应商确认时间")
    private String supplierConfirmTimeStr;

    @ApiModelProperty(value = "对账完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty(value = "对账完成时间")
    private String completeTimeStr;

}
