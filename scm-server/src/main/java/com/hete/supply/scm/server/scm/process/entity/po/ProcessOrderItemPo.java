package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 加工单明细表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_order_item")
@ApiModel(value = "ProcessOrderItemPo对象", description = "加工单明细表")
public class ProcessOrderItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_item_id", type = IdType.ASSIGN_ID)
    private Long processOrderItemId;


    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "变体属性")
    private String variantProperties;


    @ApiModelProperty(value = "采购单价")
    private BigDecimal purchasePrice;


    @ApiModelProperty(value = "加工数量")
    private Integer processNum;


    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;


    @ApiModelProperty(value = "是否是首次创建，是：'true'，次品赋码设置为 \"false\"")
    private BooleanType isFirst;

}
