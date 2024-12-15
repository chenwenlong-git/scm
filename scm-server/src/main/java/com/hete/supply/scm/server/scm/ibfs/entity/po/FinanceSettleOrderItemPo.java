package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleOrderItemType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 财务结算单明细表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_settle_order_item")
@ApiModel(value = "FinanceSettleOrderItemPo对象", description = "财务结算单明细表")
public class FinanceSettleOrderItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_settle_order_item_id", type = IdType.ASSIGN_ID)
    private Long financeSettleOrderItemId;


    @ApiModelProperty(value = "结算单号")
    private String financeSettleOrderNo;


    @ApiModelProperty(value = "明细类型")
    private FinanceSettleOrderItemType financeSettleOrderItemType;


    @ApiModelProperty(value = "关联单据")
    private String businessNo;


    @ApiModelProperty(value = "总结算金额")
    private BigDecimal settleAmount;


    @ApiModelProperty(value = "应收结算汇总")
    private BigDecimal receiveAmount;


    @ApiModelProperty(value = "应付结算汇总")
    private BigDecimal payAmount;
}
