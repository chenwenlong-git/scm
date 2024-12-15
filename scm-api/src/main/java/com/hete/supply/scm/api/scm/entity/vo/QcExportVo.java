package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 质检明细导出
 *
 * @author yanjiawei
 * Created on 2023/10/31.
 */
@Data
@NoArgsConstructor
public class QcExportVo {
    @ApiModelProperty("仓库")
    private String warehouse;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "发货单号")
    private String scmBizNo;

    @ApiModelProperty("质检单号")
    private String qcOrderNo;

    @ApiModelProperty("收货类型")
    private String receivingType;

    @ApiModelProperty("质检方式")
    private String qcType;

    @ApiModelProperty("质检单状态")
    private String qcStatus;

    @ApiModelProperty("SKU")
    private String sku;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("交接数量")
    private Integer handoverQuantity;

    @ApiModelProperty("质检数量")
    private Integer qcQuantity;

    @ApiModelProperty("正品数")
    private int goodQuantity;

    @ApiModelProperty("次品数")
    private int defectiveQuantity;

    @ApiModelProperty("质检合格率/检验合格率")
    private String qcPassRate;

    @ApiModelProperty("质检结果")
    private String qcResult;

    @ApiModelProperty("计划上架数")
    private Integer planAmount;

    @ApiModelProperty("退货数")
    private Integer returnCnt;

    @ApiModelProperty("特采数")
    private Integer compromiseCnt;

    @ApiModelProperty("报废数")
    private Integer scrapCnt;

    @ApiModelProperty("换货数")
    private Integer exchangeCnt;

    @ApiModelProperty("批次码")
    private String batchCode;

    @ApiModelProperty("质检员")
    private String qcInspector;

    @ApiModelProperty("交接时间")
    private String handOverTime;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("质检时间")
    private String qcTime;

    @ApiModelProperty("次品原因")
    private String defectiveReason;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "上架单号")
    private String onShelvesOrderNos;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderNos;

    @ApiModelProperty(value = "去向收货单号")
    private String receiveOrderNos;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "质检时间")
    private String taskFinishTime;

    @ApiModelProperty(value = "质检月份")
    private String taskFinishTimeMonth;

    @ApiModelProperty(value = "交接人")
    private String handOverUser;

    @ApiModelProperty(value = "审核员")
    private String auditor;

    @ApiModelProperty(value = "审核时间")
    private String auditTime;

    @ApiModelProperty(value = "供应商类型")
    private String supplierType;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "供应商等级")
    private String supplierGrade;

    @ApiModelProperty(value = "入库类型")
    private String receiveType;

    @ApiModelProperty(value = "质检来源(质检类型)")
    private String qcOrigin;

    @ApiModelProperty(value = "质检来源属性(质检标识)")
    private String qcOriginProperty;

    @ApiModelProperty(value = "采购订单号/采购子单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "待质检数量")
    private Integer waitAmount;

    @ApiModelProperty(value = "是否加急")
    private String isUrgentOrder;

    @ApiModelProperty(value = "质检耗时")
    private BigDecimal qcUseHours;

    @ApiModelProperty(value = "需质检数量")
    private Integer amount;

    @ApiModelProperty(value = "质检数量", notes = "批次维度已质检数量")
    private Integer completeQcAmount;

    @ApiModelProperty(value = "明细质检数量", notes = "次品原因维度已质检数量")
    private Integer goodAndDefectAmount;

    @ApiModelProperty(value = "来源单号")
    private String qcSourceOrderNo;

    @ApiModelProperty(value = "来源类型")
    private String qcSourceOrderType;
}
