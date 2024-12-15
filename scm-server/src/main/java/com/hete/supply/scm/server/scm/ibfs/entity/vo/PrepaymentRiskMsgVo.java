package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/5/13 10:02
 */
@Data
@NoArgsConstructor
public class PrepaymentRiskMsgVo {
    @ApiModelProperty(value = "供应商上月入库金额")
    private BigDecimal supplierWarehousingMoney;

    @ApiModelProperty(value = "供应商在途生产金额")
    private BigDecimal supplierInTransitMoney;

    @ApiModelProperty(value = "待预付款预付款金额")
    private BigDecimal waitPrePayMoney;

    @ApiModelProperty(value = "近3月预付款金额")
    private BigDecimal lastThreeMonPrePayMoney;

    @ApiModelProperty(value = "近1年对账审批失败次数")
    private Long lastYearRecoFailApproveCnt;

    @ApiModelProperty(value = "近1年预付款审批失败次数")
    private Long lastYearPrePayFailApproveCnt;

    @ApiModelProperty(value = "是否存在风险(整体)")
    private BooleanType isRisk;

    @ApiModelProperty(value = "供应商上月入库金额风险")
    private BooleanType supplierWarehousingMoneyRisk;

    @ApiModelProperty(value = "供应商在途生产金额风险")
    private BooleanType supplierInTransitMoneyRisk;

    @ApiModelProperty(value = "待预付款预付款金额风险")
    private BooleanType waitPrePayMoneyRisk;

    @ApiModelProperty(value = "近3月预付款金额风险")
    private BooleanType lastThreeMonPrePayMoneyRisk;

    @ApiModelProperty(value = "近1年对账审批失败次数风险")
    private BooleanType lastYearRecoFailApproveCntRisk;

    @ApiModelProperty(value = "近1年预付款审批失败次数风险")
    private BooleanType lastYearPrePayFailApproveCntRisk;


}
