package com.hete.supply.scm.api.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.QcExportType;
import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.api.scm.entity.enums.QcType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanjiawei
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QcDetailSearchExportDto extends ComPageDto {
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "质检单号列表")
    @JsonProperty(value = "qcOrderNoList")
    private List<String> qcOrderNos;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "发货单号")
    private String deliverOrderNo;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "质检类型")
    private List<QcType> qcType;

    @ApiModelProperty(value = "收货类型")
    private String receiveType;

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

    @ApiModelProperty(value = "sku")
    private List<String> skuList;

    @ApiModelProperty(value = "导出类型")
    @NotNull(message = "导出类型不能为空")
    @JsonProperty("qcDetailExportStrategy")
    private QcExportType qcExportType;
}
