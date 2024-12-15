package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentType;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:05
 */
@Data
@NoArgsConstructor
public class PrepaymentSearchVo {
    @ApiModelProperty(value = "id")
    private Long financePrepaymentOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;

    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "预付款单状态")
    private PrepaymentOrderStatus prepaymentOrderStatus;

    @ApiModelProperty(value = "预付款类型：")
    private PrepaymentType prepaymentType;

    @ApiModelProperty(value = "申请日期")
    private LocalDateTime applyDate;

    @ApiModelProperty(value = "预付金额(rmb)")
    private BigDecimal prepaymentMoney;

    @ApiModelProperty(value = "币种")
    private Currency currency;

    @ApiModelProperty(value = "目标付款金额")
    private BigDecimal targetPaymentMoney;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty(value = "审批taskId")
    private String taskId;
}
