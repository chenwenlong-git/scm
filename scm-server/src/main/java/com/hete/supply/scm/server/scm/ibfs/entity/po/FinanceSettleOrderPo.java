package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 财务结算单表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_settle_order")
@ApiModel(value = "FinanceSettleOrderPo对象", description = "财务结算单表")
public class FinanceSettleOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_settle_order_id", type = IdType.ASSIGN_ID)
    private Long financeSettleOrderId;


    @ApiModelProperty(value = "结算单号")
    private String financeSettleOrderNo;


    @ApiModelProperty(value = "结算单状态")
    private FinanceSettleOrderStatus financeSettleOrderStatus;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "总结算金额=应付结算汇总-应收结算汇总")
    private BigDecimal settleAmount;


    @ApiModelProperty(value = "应收结算汇总")
    private BigDecimal receiveAmount;


    @ApiModelProperty(value = "应付结算汇总")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "跟单采购员")
    private String followUser;

    @ApiModelProperty(value = "工厂提交时间")
    private LocalDateTime supplierSubmitTime;


    @ApiModelProperty(value = "跟单确认人")
    private String followerConfirmUser;


    @ApiModelProperty(value = "跟单确认人名称")
    private String followerConfirmUsername;


    @ApiModelProperty(value = "跟单确认时间")
    private LocalDateTime followerConfirmTime;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "飞书审批单号")
    private String workflowNo;


    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;


    @ApiModelProperty(value = "当前操作人名称")
    private String ctrlUsername;


    @ApiModelProperty(value = "审批taskId")
    private String taskId;


    @ApiModelProperty(value = "审核完成时间")
    private LocalDateTime workflowFinishTime;


    @ApiModelProperty(value = "结算完成时间")
    private LocalDateTime settleFinishTime;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
