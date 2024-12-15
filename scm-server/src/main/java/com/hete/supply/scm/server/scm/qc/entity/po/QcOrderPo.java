package com.hete.supply.scm.server.scm.qc.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 质检单
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qc_order")
@ApiModel(value = "QcOrderPo对象", description = "质检单")
public class QcOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "质检单id")
    @TableId(value = "qc_order_id", type = IdType.ASSIGN_ID)
    private Long qcOrderId;


    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;


    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "来源单号")
    private String qcSourceOrderNo;

    @ApiModelProperty(value = "来源类型")
    private QcSourceOrderType qcSourceOrderType;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "质检类型")
    private QcType qcType;

    @ApiModelProperty(value = "质检来源")
    private QcOrigin qcOrigin;

    @ApiModelProperty(value = "质检来源属性")
    private QcOriginProperty qcOriginProperty;

    @ApiModelProperty(value = "质检数量")
    private Integer qcAmount;


    @ApiModelProperty(value = "质检状态")
    private QcState qcState;


    @ApiModelProperty(value = "质检结果")
    private QcResult qcResult;


    @ApiModelProperty(value = "确认交接时间")
    private LocalDateTime handOverTime;


    @ApiModelProperty(value = "质检任务完成时间")
    private LocalDateTime taskFinishTime;


    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;


    @ApiModelProperty(value = "交接人")
    private String handOverUser;


    @ApiModelProperty(value = "质检人")
    private String operator;


    @ApiModelProperty(value = "质检人")
    private String operatorName;


    @ApiModelProperty(value = "审核人")
    private String auditor;


    @ApiModelProperty(value = "商品开发类型：NORMAL(常规),LIMITED(limited)")
    private String skuDevType;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "供应商代码(收货单)")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称(收货单)")
    private String supplierName;

    @ApiModelProperty(value = "采购订单号/采购子单号")
    private String purchaseChildOrderNo;

}
