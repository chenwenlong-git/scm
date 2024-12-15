package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleSettleStatus;
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
 * 样品结算结算单
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_sample_settle_order")
@ApiModel(value = "DevelopSampleSettleOrderPo对象", description = "样品结算结算单")
public class DevelopSampleSettleOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_sample_settle_order_id", type = IdType.ASSIGN_ID)
    private Long developSampleSettleOrderId;


    @ApiModelProperty(value = "样品结算单号")
    private String developSampleSettleOrderNo;


    @ApiModelProperty(value = "结算单状态")
    private DevelopSampleSettleStatus developSampleSettleStatus;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "月份")
    private String month;


    @ApiModelProperty(value = "对账金额")
    private BigDecimal totalPrice;


    @ApiModelProperty(value = "应扣款金额")
    private BigDecimal deductPrice;


    @ApiModelProperty(value = "应付金额")
    private BigDecimal payPrice;


    @ApiModelProperty(value = "确认拒绝备注")
    private String settleRefuseRemarks;


    @ApiModelProperty(value = "审核拒绝备注")
    private String examineRefuseRemarks;


    @ApiModelProperty(value = "对账人的用户")
    private String confirmUser;


    @ApiModelProperty(value = "对账人的用户名")
    private String confirmUsername;


    @ApiModelProperty(value = "对账时间")
    private LocalDateTime confirmTime;


    @ApiModelProperty(value = "供应商确认人的用户")
    private String examineUser;


    @ApiModelProperty(value = "供应商确认人的用户名")
    private String examineUsername;


    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime examineTime;


    @ApiModelProperty(value = "结算审核人的用户")
    private String settleUser;


    @ApiModelProperty(value = "结算审核人的用户名")
    private String settleUsername;


    @ApiModelProperty(value = "财务审核时间")
    private LocalDateTime settleTime;


    @ApiModelProperty(value = "支付人的用户")
    private String payUser;


    @ApiModelProperty(value = "支付人的用户名")
    private String payUsername;


    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;


}
