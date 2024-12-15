package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@Data
@ApiModel(value = "样品需求子单")
public class SampleChildOrderVo {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "总结算金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "打样单价")
    private BigDecimal proofingPrice;

    @ApiModelProperty(value = "样品成本单价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "供应商生产信息")
    private String supplierProduction;
}
