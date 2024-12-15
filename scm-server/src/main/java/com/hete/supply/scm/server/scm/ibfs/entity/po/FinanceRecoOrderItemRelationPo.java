package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.ibfs.enums.RecoOrderItemRelationType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 财务对账单明细SKU关联使用单据表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_reco_order_item_relation")
@ApiModel(value = "FinanceRecoOrderItemRelationPo对象", description = "财务对账单明细SKU关联使用单据表")
public class FinanceRecoOrderItemRelationPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_reco_order_item_relation_id", type = IdType.ASSIGN_ID)
    private Long financeRecoOrderItemRelationId;


    @ApiModelProperty(value = "关联财务对账单明细ID")
    private Long financeRecoOrderItemId;


    @ApiModelProperty(value = "关联财务对账单明细SKU ID")
    private Long financeRecoOrderItemSkuId;


    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;


    @ApiModelProperty(value = "关联单据类型")
    private RecoOrderItemRelationType recoOrderItemRelationType;


    @ApiModelProperty(value = "关联单据 ID")
    private Long businessId;


    @ApiModelProperty(value = "关联单据号")
    private String businessNo;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "数量")
    private Integer num;


    @ApiModelProperty(value = "金额(总金额)")
    private BigDecimal totalPrice;


}
