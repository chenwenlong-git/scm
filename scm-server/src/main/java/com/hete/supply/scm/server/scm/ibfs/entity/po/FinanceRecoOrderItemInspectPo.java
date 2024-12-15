package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.RecoOrderInspectType;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 财务对账单明细校验表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_reco_order_item_inspect")
@ApiModel(value = "FinanceRecoOrderItemInspectPo对象", description = "财务对账单明细校验表")
public class FinanceRecoOrderItemInspectPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_reco_order_item_inspect_id", type = IdType.ASSIGN_ID)
    private Long financeRecoOrderItemInspectId;


    @ApiModelProperty(value = "关联财务对账单明细ID")
    private Long financeRecoOrderItemId;


    @ApiModelProperty(value = "关联财务对账单明细SKU ID")
    private Long financeRecoOrderItemSkuId;


    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;


    @ApiModelProperty(value = "检验类型")
    private RecoOrderInspectType recoOrderInspectType;

    @ApiModelProperty(value = "收单数据")
    private BigDecimal originalValue;

    @ApiModelProperty(value = "检验数据")
    private BigDecimal inspectValue;

    @ApiModelProperty(value = "检验结果")
    private BooleanType inspectResult;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "检验结果规则")
    private String inspectResultRule;


}
