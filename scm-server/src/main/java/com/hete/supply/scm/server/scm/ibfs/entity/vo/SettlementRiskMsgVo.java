package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/5/29.
 */
@Data
@NoArgsConstructor
public class SettlementRiskMsgVo {
    @ApiModelProperty(value = "供应商上月入库金额")
    private BigDecimal supplierWarehousingMoney;
    @ApiModelProperty(value = "供应商上月入库金额风险")
    private BooleanType supplierWarehousingMoneyRisk = BooleanType.FALSE;

    @ApiModelProperty(value = "供应商在途生产金额")
    private BigDecimal supplierInTransitMoney;
    @ApiModelProperty(value = "供应商在途生产金额风险")
    private BooleanType supplierInTransitMoneyRisk = BooleanType.FALSE;

    @ApiModelProperty(value = "待抵扣预付款金额")
    private BigDecimal waitPrePayMoney;
    @ApiModelProperty(value = "待抵扣预付款金额风险")
    private BooleanType waitSettleMoneyRisk = BooleanType.FALSE;

    @ApiModelProperty(value = "月均结算金额")
    private BigDecimal avgMonthSettleAmount;
    @ApiModelProperty(value = "月均结算金额风险")
    private BooleanType avgMonthSettleAmountRisk = BooleanType.FALSE;

    @ApiModelProperty(value = "跟单近1年审批失败次数")
    private Long lastYearRecoFailApproveCnt;
    @ApiModelProperty(value = "近1年对账审批失败次数风险")
    private BooleanType lastYearRecoFailApproveCntRisk = BooleanType.FALSE;

    @ApiModelProperty(value = "供应商近1年审批失败次数")
    private Long lastYearSettleFailApproveCnt;
    @ApiModelProperty(value = "近1年结算审批失败次数风险")
    private BooleanType lastYearSettleFailApproveCntRisk = BooleanType.FALSE;

    @ApiModelProperty(value = "是否存在风险(整体)")
    private BooleanType isRisk = BooleanType.FALSE;

}
