package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleCarryoverOrderPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
@Data
public class CarryoverCalculationBo {

    @ApiModelProperty(value = "对账总金额", example = "1000.00")
    private BigDecimal totalReAmount;

    @ApiModelProperty(value = "结转单列表")
    private List<CarryoverOrderBo> carryoverOrders;

    @ApiModelProperty(value = "需要更新结转单")
    private List<FinanceSettleCarryoverOrderPo> updateFinanceSettleCarryoverOrderPos;

    @Data
    @ApiModel(description = "结转单信息")
    public static class CarryoverOrderBo {

        @ApiModelProperty(value = "结转单号", example = "CO123")
        private String carryoverNo;

        @ApiModelProperty(value = "结转金额", example = "500.00")
        private BigDecimal carryoverAmount;
    }
}
