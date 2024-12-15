package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.supply.scm.server.scm.settle.enums.SupplementDeductType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 加工结算单明细补款/扣款
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_settle_order_bill")
@ApiModel(value = "ProcessSettleOrderBillPo对象", description = "加工结算单明细补款/扣款")
public class ProcessSettleOrderBillPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_settle_order_bill_id", type = IdType.ASSIGN_ID)
    private Long processSettleOrderBillId;


    @ApiModelProperty(value = "关联结算单详情ID")
    private Long processSettleOrderItemId;


    @ApiModelProperty(value = "关联单据类型：补款单(REPLENISH)、扣款单(DEDUCT)")
    private ProcessSettleOrderBillType processSettleOrderBillType;


    @ApiModelProperty(value = "关联单据号")
    private String businessNo;


    @ApiModelProperty(value = "单据类型：价差扣款(PRICE)、加工扣款(PROCESS)、品质扣款(QUALITY)")
    private SupplementDeductType supplementDeductType;


    @ApiModelProperty(value = "审核时间")
    private LocalDateTime examineTime;


    @ApiModelProperty(value = "补款/扣款金额")
    private BigDecimal price;


    @ApiModelProperty(value = "单据状态")
    private String statusName;


}
