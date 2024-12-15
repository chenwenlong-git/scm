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

import java.time.LocalDateTime;

/**
 * <p>
 * 返修单结果表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("repair_order_result")
@ApiModel(value = "RepairOrderResultPo对象", description = "返修单结果表")
public class RepairOrderResultPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "repair_order_result_id", type = IdType.ASSIGN_ID)
    private Long repairOrderResultId;


    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;


    @ApiModelProperty(value = "返修明细id")
    private Long repairOrderItemId;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "完成数量")
    private Integer completedQuantity;

    @ApiModelProperty(value = "原料批次码")
    private String materialBatchCode;

    @ApiModelProperty(value = "原料使用数量")
    private Integer materialUsageQuantity;

    @ApiModelProperty(value = "返修人id")
    private String repairUser;


    @ApiModelProperty(value = "返修人名称")
    private String repairUsername;


    @ApiModelProperty(value = "返修时间")
    private LocalDateTime repairTime;

    @ApiModelProperty(value = "质检通过数量")
    private Integer qcPassQuantity;

    @ApiModelProperty(value = "质检不通过数量")
    private Integer qcFailQuantity;
}
