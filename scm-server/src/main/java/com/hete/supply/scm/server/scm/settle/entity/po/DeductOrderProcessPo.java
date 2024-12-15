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
 * 扣款单加工明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("deduct_order_process")
@ApiModel(value = "DeductOrderProcessPo对象", description = "扣款单加工明细")
public class DeductOrderProcessPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "deduct_order_process_id", type = IdType.ASSIGN_ID)
    private Long deductOrderProcessId;


    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;


    @ApiModelProperty(value = "关联加工单ID")
    private Long processOrderId;


    @ApiModelProperty(value = "关联加工单号")
    private String processOrderNo;


    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;


    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;


    @ApiModelProperty(value = "扣款备注")
    private String deductRemarks;


}
