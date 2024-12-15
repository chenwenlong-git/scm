package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:39
 */
@Data
@NoArgsConstructor
public class RecoOrderSearchVo {
    @ApiModelProperty(value = "id")
    private Long financeRecoOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "状态")
    private FinanceRecoOrderStatus financeRecoOrderStatus;

    @ApiModelProperty(value = "对账周期开始时间")
    private LocalDateTime reconciliationStartTime;

    @ApiModelProperty(value = "对账周期结束时间")
    private LocalDateTime reconciliationEndTime;

    @ApiModelProperty(value = "对账结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "对账应收金额")
    private BigDecimal receivePrice;

    @ApiModelProperty(value = "对账应付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty(value = "审批taskId")
    private String taskId;

}
