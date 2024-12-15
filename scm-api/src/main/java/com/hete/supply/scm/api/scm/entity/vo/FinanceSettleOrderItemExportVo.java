package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/5/26.
 */
@Data
@NoArgsConstructor
public class FinanceSettleOrderItemExportVo {

    /**
     * 结算单号
     */
    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

    /**
     * 结算单状态
     */
    @ApiModelProperty(value = "结算单状态备注")
    private String settleOrderStatusRemark;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    private String supplierCode;

    @ApiModelProperty(value = "申请时间")
    private String applyTime;

    /**
     * 处理人名称
     */
    @ApiModelProperty(value = "处理人名称")
    private String ctrlUserName;

    /**
     * 关联单号：对账单号或者结算单号
     */
    @ApiModelProperty(value = "关联单号")
    private String businessNo;

    /**
     * 对账应收：如果是对账单就是应收金额，否则为空
     */
    @ApiModelProperty(value = "对账应收")
    private BigDecimal receiveAmount;

    /**
     * 对账应付：如果是对账单就是应付金额，否则为空
     */
    @ApiModelProperty(value = "对账应付")
    private BigDecimal payAmount;

    /**
     * 对账结算：如果是对账单就是结算金额，如果是结转单就是结转金额，显示正数
     */
    @ApiModelProperty(value = "对账结算")
    private BigDecimal settleAmount;
}