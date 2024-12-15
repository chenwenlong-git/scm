package com.hete.supply.scm.server.scm.ibfs.entity.po;

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

/**
 * <p>
 * 对账-预付款关联表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_reco_prepayment")
@ApiModel(value = "FinanceRecoPrepaymentPo对象", description = "对账-预付款关联表")
public class FinanceRecoPrepaymentPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_reco_prepayment_id", type = IdType.ASSIGN_ID)
    private Long financeRecoPrepaymentId;


    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;


    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;


    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;


    @ApiModelProperty(value = "抵扣金额(rmb)")
    private BigDecimal deductionMoney;


}
