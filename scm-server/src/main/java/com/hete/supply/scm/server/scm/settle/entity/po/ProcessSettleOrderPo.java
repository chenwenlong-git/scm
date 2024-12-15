package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ProcessSettleStatus;
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
 * 加工结算单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_settle_order")
@ApiModel(value = "ProcessSettleOrderPo对象", description = "加工结算单")
public class ProcessSettleOrderPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_settle_order_id", type = IdType.ASSIGN_ID)
    private Long processSettleOrderId;


    @ApiModelProperty(value = "加工结算单号")
    private String processSettleOrderNo;


    @ApiModelProperty(value = "加工结算单状态：待核算(WAIT_SETTLE)、结算待审核(SETTLE_WAIT_EXAMINE)、已审核(AUDITED)")
    private ProcessSettleStatus processSettleStatus;


    @ApiModelProperty(value = "总付款金额")
    private BigDecimal totalPrice;


    @ApiModelProperty(value = "应扣款金额")
    private BigDecimal deductPrice;


    @ApiModelProperty(value = "应付总金额")
    private BigDecimal payPrice;


    @ApiModelProperty(value = "月份")
    private String month;


    @ApiModelProperty(value = "审核拒绝原因")
    private String examineRefuseRemarks;


    @ApiModelProperty(value = "核算人的用户")
    private String examineUser;


    @ApiModelProperty(value = "核算人的用户名")
    private String examineUsername;


    @ApiModelProperty(value = "核算时间")
    private LocalDateTime examineTime;


    @ApiModelProperty(value = "结算审核人的用户")
    private String settleUser;


    @ApiModelProperty(value = "结算审核人的用户名")
    private String settleUsername;


    @ApiModelProperty(value = "结算审核人时间")
    private LocalDateTime settleTime;


    @ApiModelProperty(value = "创建人名称")
    private String createUsername;


    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;


}
