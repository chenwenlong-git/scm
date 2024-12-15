package com.hete.supply.scm.api.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/10 09:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QcSearchDto extends ComPageDto {
    @ApiModelProperty(value = "质检单号列表")
    private List<String> qcOrderNoList;

    @ApiModelProperty(value = "容器编码")
    private List<String> containerCodeList;

    @ApiModelProperty(value = "批次码")
    private List<String> skuBatchCodeList;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "供应链单据号")
    private String scmBizNo;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "质检类型")
    private List<QcType> qcType;

    @ApiModelProperty(value = "收货类型")
    private ReceiveType receiveType;

    @ApiModelProperty(value = "质检结果")
    private QcResult qcResult;

    @ApiModelProperty(value = "商品类目")
    private String goodsCategory;

    @ApiModelProperty(value = "辅料类目")
    private String smGoodsCategory;

    @ApiModelProperty(value = "质检人")
    private String operator;

    @ApiModelProperty(value = "质检人")
    private String operatorName;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "确认交接时间")
    private LocalDateTime handOverTimeStart;

    @ApiModelProperty(value = "确认交接时间")
    private LocalDateTime handOverTimeEnd;

    @ApiModelProperty(value = "质检任务完成时间")
    private LocalDateTime taskFinishTimeStart;

    @ApiModelProperty(value = "质检任务完成时间")
    private LocalDateTime taskFinishTimeEnd;

    @ApiModelProperty(value = "质检状态")
    private List<QcState> qcStateList;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "是否操作打印")
    @JsonIgnore
    private boolean isPrint;

    @ApiModelProperty(value = "导出类型")
    @JsonProperty("qcDetailExportStrategy")
    private QcExportType qcExportType;

    @ApiModelProperty(value = "审核人")
    private String auditor;

    @ApiModelProperty(value = "交接人")
    private String handOverUser;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTimeStart;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTimeEnd;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "商品类目名称")
    private String categoryName;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "详情质检单号列表")
    private List<String> qcDetailQcOrderNoList;

    @ApiModelProperty(value = "收货单质检单号列表")
    private List<String> qcReceiveQcOrderNoList;

    @ApiModelProperty(value = "质检来源(质检类型)")
    private QcOrigin qcOrigin;

    @ApiModelProperty(value = "质检来源属性(质检标识)")
    private QcOriginProperty qcOriginProperty;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "采购订单号/采购子单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "来源单号")
    private String qcSourceOrderNo;

    @ApiModelProperty(value = "来源类型")
    private QcSourceOrderType qcSourceOrderType;
}
