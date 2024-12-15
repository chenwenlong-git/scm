package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/5/17 13:57
 */
@Data
@NoArgsConstructor
public class SampleListBySkuAndDevTypeVo {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderId;

    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "生产标签")
    private SampleProduceLabel sampleProduceLabel;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "打样单价")
    private BigDecimal proofingPrice;

    @ApiModelProperty(value = "样品成本单价")
    private BigDecimal costPrice;

}
