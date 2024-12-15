package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReconciliationCycle;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 财务对账单表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_reco_order")
@ApiModel(value = "FinanceRecoOrderPo对象", description = "财务对账单表")
public class FinanceRecoOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_reco_order_id", type = IdType.ASSIGN_ID)
    private Long financeRecoOrderId;


    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;


    @ApiModelProperty(value = "对账单状态")
    private FinanceRecoOrderStatus financeRecoOrderStatus;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "对账周期")
    private ReconciliationCycle reconciliationCycle;


    @ApiModelProperty(value = "对账周期开始时间")
    private LocalDateTime reconciliationStartTime;


    @ApiModelProperty(value = "对账周期结束时间")
    private LocalDateTime reconciliationEndTime;


    @ApiModelProperty(value = "对账结算金额(有可能负数)")
    private BigDecimal settlePrice;


    @ApiModelProperty(value = "对账应收金额")
    private BigDecimal receivePrice;


    @ApiModelProperty(value = "对账应付金额")
    private BigDecimal payPrice;


    @ApiModelProperty(value = "关联结算单号")
    private String financeSettleOrderNo;


    @ApiModelProperty(value = "飞书工作流单号")
    private String workflowNo;


    @ApiModelProperty(value = "提交人")
    private String submitUser;


    @ApiModelProperty(value = "提交人的名称")
    private String submitUsername;


    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;


    @ApiModelProperty(value = "确认人")
    private String confirmUser;


    @ApiModelProperty(value = "确认人的名称")
    private String confirmUsername;


    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime supplierConfirmTime;

    @ApiModelProperty(value = "收单时间")
    private LocalDateTime collectOrderTime;

    @ApiModelProperty(value = "对账完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty(value = "审批taskId")
    private String taskId;

    @ApiModelProperty(value = "跟单人")
    private String followUser;

    @ApiModelProperty(value = "跟单人的名称")
    private String followUsername;

    @ApiModelProperty(value = "工厂确认意见")
    private String comment;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
