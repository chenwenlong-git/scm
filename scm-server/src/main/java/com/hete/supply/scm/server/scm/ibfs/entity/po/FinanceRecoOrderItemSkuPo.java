package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.CollectOrderType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoPayType;
import com.hete.supply.scm.api.scm.entity.enums.RecoOrderItemSkuStatus;
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
 * 财务对账单明细sku详情表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_reco_order_item_sku")
@ApiModel(value = "FinanceRecoOrderItemSkuPo对象", description = "财务对账单明细sku详情表")
public class FinanceRecoOrderItemSkuPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_reco_order_item_sku_id", type = IdType.ASSIGN_ID)
    private Long financeRecoOrderItemSkuId;


    @ApiModelProperty(value = "关联财务对账单明细ID")
    private Long financeRecoOrderItemId;


    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;


    @ApiModelProperty(value = "收单单据")
    private String collectOrderNo;


    @ApiModelProperty(value = "款项类型")
    private FinanceRecoFundType financeRecoFundType;


    @ApiModelProperty(value = "收单类型")
    private CollectOrderType collectOrderType;


    @ApiModelProperty(value = "收付类型")
    private FinanceRecoPayType financeRecoPayType;


    @ApiModelProperty(value = "状态")
    private RecoOrderItemSkuStatus recoOrderItemSkuStatus;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "数量")
    private Integer num;


    @ApiModelProperty(value = "单价")
    private BigDecimal price;


    @ApiModelProperty(value = "收单金额（单价*数量）")
    private BigDecimal totalPrice;


    @ApiModelProperty(value = "SKU")
    private String sku;


    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "收单关联时间")
    private LocalDateTime associationTime;

    @ApiModelProperty(value = "关联单明细的ID")
    private Long collectOrderItemId;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

}
