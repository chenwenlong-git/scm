package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
@Data
public class CreateFinanceSettleItemBo {
    private String supplierCode;
    private List<FinanceRecoOrderBo> financeRecoOrderBos;
    private List<FinanceSettleCarryoverOrderBo> financeSettleCarryoverOrderBos;

    @Data
    public static class FinanceRecoOrderBo {
        @ApiModelProperty(value = "对账单号")
        private String financeRecoOrderNo;

        @ApiModelProperty(value = "对账应收金额")
        private BigDecimal receivePrice;

        @ApiModelProperty(value = "对账应付金额")
        private BigDecimal payPrice;

        @ApiModelProperty(value = "总结算金额")
        private BigDecimal settleAmount;
    }

    @Data
    public static class FinanceSettleCarryoverOrderBo {
        @ApiModelProperty(value = "结转单号")
        private String financeSettleCarryoverOrderNo;

        @ApiModelProperty(value = "结转金额")
        private BigDecimal carryoverAmount;
    }

}
