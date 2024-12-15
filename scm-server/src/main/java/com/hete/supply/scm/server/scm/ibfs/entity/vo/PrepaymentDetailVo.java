package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.ibfs.enums.DeductionStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:17
 */
@Data
@NoArgsConstructor
public class PrepaymentDetailVo {
    @ApiModelProperty(value = "id")
    private Long financePrepaymentOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;

    @ApiModelProperty(value = "预付款单状态")
    private PrepaymentOrderStatus prepaymentOrderStatus;

    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "抵扣状态")
    private DeductionStatus deductionStatus;

    @ApiModelProperty(value = "预付款类型：")
    private PrepaymentType prepaymentType;

    @ApiModelProperty(value = "预付款事由")
    private String prepaymentReason;

    @ApiModelProperty(value = "附件")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "预付金额")
    private BigDecimal prepaymentMoney;

    @ApiModelProperty(value = "币种")
    private Currency currency;

    @ApiModelProperty(value = "付款金额(rmb)")
    private BigDecimal paymentMoney;

    @ApiModelProperty(value = "目标付款金额")
    private BigDecimal targetPaymentMoney;

    @ApiModelProperty(value = "预付款备注")
    private String prepaymentRemark;

    @ApiModelProperty(value = "收款明细")
    private List<FinancePrepaymentOrderItemVo> financePrepaymentOrderItemList;

    @ApiModelProperty(value = "付款明细")
    private List<FinancePaymentOrderItemVo> financePaymentOrderItemList;

    @ApiModelProperty(value = "本月货款")
    private BigDecimal monthGoodsPayment;

    @ApiModelProperty(value = "工作流单号")
    private String workflowNo;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUsername;

    @ApiModelProperty(value = "审批taskId")
    private String taskId;

    @ApiModelProperty(value = "单据创建人")
    private String createUser;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "对账单状态")
    private FinanceRecoOrderStatus financeRecoOrderStatus;

    @ApiModelProperty(value = "新增收款账户操作权限")
    private BooleanType receiveAdd;
}
