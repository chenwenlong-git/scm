package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.TreeSet;

/**
 * @author yanjiawei
 * Created on 2023/12/15.
 */
@Data
@ApiModel("扫码提成信息")
public class ScanCommissionBo {
    @ApiModelProperty("扫码记录ID")
    private Long processOrderScanId;

    @ApiModelProperty("工序ID")
    private Long processId;

    @ApiModelProperty("工序编码")
    private String processCode;

    @ApiModelProperty("工序类别")
    private ProcessLabel processLabel;

    @ApiModelProperty("工序名称")
    private String processName;

    @ApiModelProperty("正品数")
    private Integer qualityGoodsCount;

    @ApiModelProperty("基础单价")
    private BigDecimal basePrice;

    @ApiModelProperty("总提成")
    private BigDecimal totalCommission;

    @ApiModelProperty("补款金额")
    private BigDecimal additionalPayment;

    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty("工序提成规则列表")
    private TreeSet<ProcessCommissionRuleBo> commissionRules;

    @ApiModelProperty("工序提成明细")
    private List<CommissionDetailBo> commissionDetails;

}
