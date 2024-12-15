package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/24 15:04
 */
@Data
@NoArgsConstructor
public class RecoOrderApproveBo {

    @ApiModelProperty(value = "供应商")
    private String supplierCode;

    @ApiModelProperty(value = "贷款金额")
    private String settlePrice;

    @ApiModelProperty(value = "账单开始时间-账单结束时间")
    private String reconciliationCycleTime;

    @ApiModelProperty(value = "时长(天)")
    private String reconciliationCycleDay;

    @ApiModelProperty(value = "贷款周期")
    private String reconciliationCycleValue;

    @ApiModelProperty(value = "对账事由")
    private String reason;

    @ApiModelProperty(value = "货款申请部门")
    private String dept;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "费用明细")
    private List<RecoOrderDetailApproveBo> recoOrderDetailApproveList;


}
