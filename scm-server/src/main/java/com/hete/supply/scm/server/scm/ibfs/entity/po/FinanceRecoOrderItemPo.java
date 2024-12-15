package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.CollectOrderType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoPayType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 财务对账单明细表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_reco_order_item")
@ApiModel(value = "FinanceRecoOrderItemPo对象", description = "财务对账单明细表")
public class FinanceRecoOrderItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_reco_order_item_id", type = IdType.ASSIGN_ID)
    private Long financeRecoOrderItemId;


    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "款项类型")
    private FinanceRecoFundType financeRecoFundType;


    @ApiModelProperty(value = "收单类型")
    private CollectOrderType collectOrderType;


    @ApiModelProperty(value = "收单单据")
    private String collectOrderNo;


    @ApiModelProperty(value = "收付类型")
    private FinanceRecoPayType financeRecoPayType;


    @ApiModelProperty(value = "收单总数量")
    private Integer num;


    @ApiModelProperty(value = "单据总金额（对账金额）")
    private BigDecimal totalPrice;


}
