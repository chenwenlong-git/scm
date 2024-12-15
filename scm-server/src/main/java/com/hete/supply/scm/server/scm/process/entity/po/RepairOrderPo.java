package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.plm.api.repair.entity.enums.ProcessType;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
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
 * 返修单
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("repair_order")
@ApiModel(value = "RepairOrderPo对象", description = "返修单")
public class RepairOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "repair_order_id", type = IdType.ASSIGN_ID)
    private Long repairOrderId;


    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;


    @ApiModelProperty(value = "预计加工数")
    private Integer expectProcessNum;


    @ApiModelProperty(value = "预计加工完成时间")
    private LocalDateTime expectCompleteProcessTime;


    @ApiModelProperty(value = "是否回料（是/否）")
    private IsReceiveMaterial isReceiveMaterial;


    @ApiModelProperty(value = "缺失信息（无库存）")
    private String missingInformation;


    @ApiModelProperty(value = "返修单状态")
    private RepairOrderStatus repairOrderStatus;


    @ApiModelProperty(value = "确认返修单完成人id")
    private String confirmCompleteUser;


    @ApiModelProperty(value = "确认返修单完成人名称")
    private String confirmCompleteUsername;


    @ApiModelProperty(value = "确认返修单完成时间")
    private LocalDateTime confirmCompleteTime;


    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;


    @ApiModelProperty(value = "预期入库仓库编码")
    private String expectWarehouseCode;


    @ApiModelProperty(value = "预期入库仓库名称")
    private String expectWarehouseName;


    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;


    @ApiModelProperty(value = "计划单号")
    private String planNo;


    @ApiModelProperty(value = "计划单类型（单品单件/单品多件）")
    private ProcessType planType;


    @ApiModelProperty(value = "计划单号标题")
    private String planTitle;


    @ApiModelProperty(value = "需求平台编码")
    private String platform;


    @ApiModelProperty(value = "计划单创建人id")
    private String planCreateUser;


    @ApiModelProperty(value = "计划单创建人名称")
    private String planCreateUsername;


    @ApiModelProperty(value = "计划单创建时间")
    private LocalDateTime planCreateTime;


    @ApiModelProperty(value = "销售价格")
    private BigDecimal salePrice;

    @ApiModelProperty(value = "计划备注")
    private String planRemark;
}
