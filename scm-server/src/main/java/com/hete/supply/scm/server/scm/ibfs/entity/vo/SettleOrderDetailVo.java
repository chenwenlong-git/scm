package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleOrderItemType;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Data
@ApiModel(description = "结算单详情视图对象")
public class SettleOrderDetailVo {

    @ApiModelProperty(value = "结算单号", example = "SETTLE123456")
    private String settleOrderNo;

    @ApiModelProperty(value = "结算状态")
    private FinanceSettleOrderStatus settleOrderStatus;

    @ApiModelProperty(value = "工厂编码")
    private String supplierCode;

    @ApiModelProperty(value = "工厂等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty(value = "当前操作人名称")
    private String ctrlUsername;

    @ApiModelProperty(value = "审批taskId")
    private String taskId;

    @ApiModelProperty(value = "飞书审批单号")
    private String workflowNo;

    @ApiModelProperty(value = "结算总金额")
    private BigDecimal totalSettleAmount;

    @ApiModelProperty(value = "结算金额币种")
    private Currency currency;

    @ApiModelProperty(value = "应收总金额")
    private BigDecimal totalReceiveAmount;

    @ApiModelProperty(value = "应付总金额")
    private BigDecimal totalPayAmount;

    @ApiModelProperty(value = "已付款金额")
    private BigDecimal totalPayedAmount;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "关联结转单")
    private String carryoverOrderNo;

    @ApiModelProperty(value = "待抵扣金额")
    private BigDecimal availableCarryoverAmount;

    @ApiModelProperty(value = "结算单明细列表")
    private List<SettleOrderItemVo> settleOrderItems;

    @ApiModelProperty(value = "结算单收款账户列表")
    private List<SettleOrderAccountVo> settleOrderAccounts;

    @ApiModelProperty(value = "付款记录列表")
    private List<PaymentRecordVo> paymentRecords;

    @ApiModelProperty(value = "是否可添加付款记录")
    private BooleanType validateAddPayment;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @Data
    @ApiModel(description = "结算单明细视图对象")
    public static class SettleOrderItemVo {

        @ApiModelProperty(value = "关联单据号")
        private String businessNo;

        @ApiModelProperty(value = "单据类型")
        private FinanceSettleOrderItemType financeSettleOrderItemType;

        @ApiModelProperty(value = "应付结算汇总")
        private BigDecimal payAmount;

        @ApiModelProperty(value = "应收结算汇总/本次抵扣金额")
        private BigDecimal receiveAmount;

        @ApiModelProperty(value = "总结算金额")
        private BigDecimal settleAmount;

        @JsonProperty("availableCarryoverAmount")
        @ApiModelProperty(value = "待结转金额")
        private BigDecimal carryoverAmount;

        @ApiModelProperty(value = "跳转单据号")
        private String redirectDocumentNo;
    }

    @Data
    @ApiModel(description = "结算单收款账户视图对象")
    public static class SettleOrderAccountVo {

        @NotNull(message = "收款信息主键ID不能为空")
        @ApiModelProperty(value = "收款信息主键ID", required = true, example = "v1")
        private Long financeSettleOrderReceiveId;

        @ApiModelProperty(value = "收款账户类型")
        private SupplierPaymentAccountType supplierPaymentAccountType;

        @ApiModelProperty(value = "账户名称")
        private String accountUsername;

        @ApiModelProperty(value = "银行账号")
        private String account;

        @ApiModelProperty(value = "开户银行")
        private String bankName;

        @ApiModelProperty(value = "支行名称")
        private String bankSubbranchName;

        @ApiModelProperty(value = "账号备注")
        private String accountRemarks;

        @ApiModelProperty(value = "银行所在地区 省份/州")
        private String bankProvince;

        @ApiModelProperty(value = "银行所在地区 城市")
        private String bankCity;

        @ApiModelProperty(value = "银行所在地区 区")
        private String bankArea;

        @ApiModelProperty(value = "币种金额", example = "100.00")
        private BigDecimal currencyAmount;

        @ApiModelProperty(value = "收款金额")
        @JsonProperty("targetPrepaymentMoney")
        private BigDecimal expectReceiveAmount;

        @ApiModelProperty(value = "收款币种")
        private Currency currency;

        @ApiModelProperty(value = "期望收款时间")
        private LocalDateTime expectReceiveDate;

        @ApiModelProperty(value = "收款信息版本号", required = true, example = "v1")
        @NotNull(message = "收款信息版本号不能为空")
        private Integer financeSettleOrderReceiveVersion;
    }

    @Data
    @ApiModel(description = "付款记录视图对象")
    public static class PaymentRecordVo {

        @ApiModelProperty(value = "付款主体")
        private String paymentSubject;

        @ApiModelProperty(value = "付款事由/付款事项")
        private String paymentReason;

        @ApiModelProperty(value = "付款备注说明")
        private String paymentRemark;

        @ApiModelProperty(value = "凭证附件")
        private List<String> fileCodeList;

        @JsonProperty("subject")
        @ApiModelProperty(value = "收方主体")
        private String recipientSubject;

        @ApiModelProperty(value = "收款账户")
        private String account;

        @ApiModelProperty(value = "收款账户名称")
        private String accountUsername;

        @ApiModelProperty(value = "开户银行")
        private String bankName;

        @ApiModelProperty(value = "银行支行")
        private String bankSubbranchName;

        @ApiModelProperty(value = "付款金额")
        private BigDecimal paymentMoney;

        @ApiModelProperty(value = "付款币种")
        private Currency currency;

        @ApiModelProperty(value = "目标汇率")
        private BigDecimal exchangeRate;

        @ApiModelProperty(value = "目标付款金额")
        private BigDecimal targetPaymentMoney;

        @ApiModelProperty(value = "人民币汇率")
        private BigDecimal rmbExchangeRate;

        @ApiModelProperty(value = "汇率转换人民币付款金额")
        private BigDecimal rmbPaymentMoney;

        @ApiModelProperty(value = "付款时间")
        private LocalDateTime paymentDate;

        @ApiModelProperty(value = "银行所在地区 省份/州")
        private String bankProvince;

        @ApiModelProperty(value = "银行所在地区 城市")
        private String bankCity;

        @ApiModelProperty(value = "银行所在地区 区")
        private String bankArea;
    }
}
