package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 样品结算单支付明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_sample_settle_pay")
@ApiModel(value = "DevelopSampleSettlePayPo对象", description = "样品结算单支付明细")
public class DevelopSampleSettlePayPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_sample_settle_pay_id", type = IdType.ASSIGN_ID)
    private Long developSampleSettlePayId;


    @ApiModelProperty(value = "结算单号")
    private String developSampleSettleOrderNo;


    @ApiModelProperty(value = "交易号")
    private String transactionNo;


    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;


    @ApiModelProperty(value = "支付金额")
    private BigDecimal payPrice;


    @ApiModelProperty(value = "备注")
    private String remarks;


}
