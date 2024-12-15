package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ChenWenLong
 * @date 2024/3/28 14:01
 */
@Data
public class DefectHandingExportVo {

    @ApiModelProperty(value = "次品记录")
    private String defectHandlingNo;

    @ApiModelProperty(value = "次品类型")
    private String defectHandlingTypeName;

    @ApiModelProperty(value = "状态")
    private String defectHandlingStatusName;

    @ApiModelProperty(value = "来源单据")
    private String defectBizNo;

    @ApiModelProperty(value = "起始单据")
    private String originOrderNo;

    @ApiModelProperty(value = "处理方案")
    private String defectHandlingProgrammeName;

    @ApiModelProperty(value = "关联单号")
    private String relatedOrderNo;

    @ApiModelProperty(value = " 收货仓库（关联单号）")
    private String relatedWarehouseName;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "数量")
    private String notPassCnt;

    @ApiModelProperty(value = "不良原因")
    private String adverseReason;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "确认人")
    private String confirmUsername;

    @ApiModelProperty(value = "确认时间")
    private String confirmTimeStr;


}
