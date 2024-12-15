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
 * 扣款单表次品退供明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("deduct_order_defective")
@ApiModel(value = "DeductOrderDefectivePo对象", description = "扣款单表次品退供明细")
public class DeductOrderDefectivePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "deduct_order_defective_id", type = IdType.ASSIGN_ID)
    private Long deductOrderDefectiveId;


    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;


    @ApiModelProperty(value = "单据号")
    private String businessNo;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "扣款数量")
    private Integer deductNum;


    @ApiModelProperty(value = "原结算单价")
    private BigDecimal settlePrice;


    @ApiModelProperty(value = "需扣款单价")
    private BigDecimal deductUnitPrice;


    @ApiModelProperty(value = "扣款总价")
    private BigDecimal deductPrice;


    @ApiModelProperty(value = "扣款原因")
    private String deductRemarks;


}
