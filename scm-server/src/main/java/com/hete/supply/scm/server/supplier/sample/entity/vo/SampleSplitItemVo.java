package com.hete.supply.scm.server.supplier.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/11 16:33
 */
@Data
@NoArgsConstructor
public class SampleSplitItemVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;


    @ApiModelProperty(value = "采购预估价")
    private BigDecimal purchasePredictPrice;


    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "需求描述")
    private String demandDescribe;

    @ApiModelProperty(value = "供应商生产信息")
    private String supplierProduction;

    @ApiModelProperty(value = "主键id")
    private Long sampleChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;
}
