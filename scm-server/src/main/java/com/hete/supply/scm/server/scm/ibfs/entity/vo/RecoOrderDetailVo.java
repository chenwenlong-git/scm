package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReconciliationCycle;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/14 13:47
 */
@Data
@NoArgsConstructor
public class RecoOrderDetailVo {

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "对账单状态")
    private FinanceRecoOrderStatus financeRecoOrderStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "对账周期")
    private ReconciliationCycle reconciliationCycle;

    @ApiModelProperty(value = "对账周期开始时间")
    private LocalDateTime reconciliationStartTime;

    @ApiModelProperty(value = "对账周期结束时间")
    private LocalDateTime reconciliationEndTime;

    @ApiModelProperty(value = "结算单号")
    private String financeSettleOrderNo;

    @ApiModelProperty(value = "飞书工作流单号")
    private String workflowNo;

    @ApiModelProperty(value = "对账总金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "应收总金额")
    private BigDecimal receivePrice;

    @ApiModelProperty(value = "应付总金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "对账单按钮显示")
    private RecoOrderButtonVo recoOrderButtonVo;

    @ApiModelProperty(value = "异常条目数量")
    private Integer inspectTotalNum;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty(value = "当前操作人名称")
    private String ctrlUsername;

    @ApiModelProperty(value = "审批taskId")
    private String taskId;

    @ApiModelProperty(value = "工厂确认意见")
    private String comment;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "对账金额币种")
    private Currency currency;

    @ApiModelProperty(value = "结算单状态")
    private FinanceSettleOrderStatus financeSettleOrderStatus;

    @ApiModelProperty(value = "单据类型汇总列表")
    private List<RecoOrderDetailFundTypeVo> recoOrderDetailFundTypeList;

}
