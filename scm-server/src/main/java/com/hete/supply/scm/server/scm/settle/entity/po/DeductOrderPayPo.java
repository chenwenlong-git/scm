package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 扣款单预付款明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("deduct_order_pay")
@ApiModel(value = "DeductOrderPayPo对象", description = "扣款单预付款明细")
public class DeductOrderPayPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "deduct_order_pay_id", type = IdType.ASSIGN_ID)
    private Long deductOrderPayId;


    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;


    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;


    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;


    @ApiModelProperty(value = "扣款备注")
    private String deductRemarks;


}
