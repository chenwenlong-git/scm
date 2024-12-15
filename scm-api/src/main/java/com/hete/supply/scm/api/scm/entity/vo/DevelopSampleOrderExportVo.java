package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/8/2 18:15
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderExportVo {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "开发需求单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "处理方式名称")
    private String developSampleMethodName;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "大货价格")
    private String developOrderPrice;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;

    @ApiModelProperty(value = "处理时间")
    private String handleTimeStr;

    @ApiModelProperty(value = "退样签收时间")
    private LocalDateTime signTime;

    @ApiModelProperty(value = "退样签收时间")
    private String signTimeStr;

    @ApiModelProperty(value = "关联单据号")
    private String receiptOrderNo;

    @ApiModelProperty(value = "上架时间")
    private LocalDateTime shelvesTime;

    @ApiModelProperty(value = "上架时间")
    private String shelvesTimeStr;

    @ApiModelProperty(value = "样品结算单")
    private String developSampleSettleOrderNo;

    @ApiModelProperty(value = "批次码样品价格")
    private BigDecimal skuBatchSamplePrice;

    @ApiModelProperty(value = "批次码大货价格")
    private BigDecimal skuBatchPurchasePrice;
}
