package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返修单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("repair_order_item")
@ApiModel(value = "RepairOrderItemPo对象", description = "返修单明细")
public class RepairOrderItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "repair_order_item_id", type = IdType.ASSIGN_ID)
    private Long repairOrderItemId;


    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;


    @ApiModelProperty(value = "预计加工数")
    private Integer expectProcessNum;


    @ApiModelProperty(value = "实际加工完成数")
    private Integer actProcessedCompleteCnt;


    @ApiModelProperty(value = "实际加工报废数")
    private Integer actProcessScrapCnt;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;
}
