package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/25.
 */
@Data
@ApiModel(description = "结算单审批业务对象")
public class FinanceSettleOrderApproveBo {

    @ApiModelProperty(value = "主体")
    private String recipientSubject;

    @ApiModelProperty(value = "货物公司")
    private String cargoCompanyName;

    @ApiModelProperty(value = "费用汇总")
    private String settleAmount;

    @ApiModelProperty(value = "费用明细汇总-金额")
    private String settlePriceUnit;

    @ApiModelProperty(value = "付款类型")
    private String settlementType;

    @ApiModelProperty(value = "日期")
    private String applyDate;

    @ApiModelProperty(value = "货款申请部门")
    private String dept;

    @ApiModelProperty(value = "报销事由")
    private String reason;

    @ApiModelProperty(value = "费用明细")
    private List<SettlementDetailApproveBo> detail;

    @ApiModelProperty(value = "结算审批单")
    private List<String> instanceCodes;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @Data
    public static class SettlementDetailApproveBo {
        private String detailType;
        private String rmbText;
        private BigDecimal rmb;
    }

}
