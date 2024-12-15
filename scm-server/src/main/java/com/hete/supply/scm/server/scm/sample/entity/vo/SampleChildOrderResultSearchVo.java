package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.supply.scm.api.scm.entity.enums.SampleResultStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/4/14 14:17
 */
@Data
@NoArgsConstructor
public class SampleChildOrderResultSearchVo {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderResultId;

    @ApiModelProperty(value = "样品结果id")
    private String sampleResultNo;

    @ApiModelProperty(value = "选样数量")
    private Integer sampleCnt;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;

    @ApiModelProperty(value = "参照图片")
    private String contrastFileUrl;

    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "关联单据")
    private String relateOrderNo;

    @ApiModelProperty(value = "关联数量")
    private Integer relateOrderAmount;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handlesTime;

    @ApiModelProperty(value = "选样结果说明")
    private String remark;

    @ApiModelProperty(value = "选样结果")
    private SampleResult sampleResult;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "处理状态")
    private SampleResultStatus sampleResultStatus;

    @ApiModelProperty(value = "供应商类型")
    private SupplierType supplierType;

}
