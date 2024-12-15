package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.process.enums.CostRelateOrderIdType;
import com.hete.supply.scm.server.scm.process.enums.CostRelatedOrderType;
import com.hete.supply.scm.server.scm.process.enums.CostType;
import com.hete.support.mybatis.plus.entity.po.BasePo;

import java.math.BigDecimal;

import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * SKU批次成本信息
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("batch_code_cost")
@ApiModel(value = "BatchCodeCostPo对象", description = "SKU批次成本信息")
public class BatchCodeCostPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "batch_code_cost_id", type = IdType.ASSIGN_ID)
    private Long batchCodeCostId;


    @ApiModelProperty(value = "关联单号")
    private String relateOrderNo;


    @ApiModelProperty(value = "关联单号类型（加工/返修）")
    private CostRelatedOrderType relateOrderNoType;


    @ApiModelProperty(value = "关联主键id")
    private Long relateOrderId;


    @ApiModelProperty(value = "关联主键id类型（加工/返修）")
    private CostRelateOrderIdType relateOrderIdType;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "sku 批次码")
    private String batchCode;


    @ApiModelProperty(value = "成本类型（加工原料成本/加工工序扫码人力成本/加工固定损耗成本）")
    private CostType costType;


    @ApiModelProperty(value = "成本总金额")
    private BigDecimal totalAmount;
}
