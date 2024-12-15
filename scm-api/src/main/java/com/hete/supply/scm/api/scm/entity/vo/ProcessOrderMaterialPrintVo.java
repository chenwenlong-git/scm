package com.hete.supply.scm.api.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/12 12:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderMaterialPrintVo {

    @ApiModelProperty(value = "原料产品明细 id")
    private Long processOrderMaterialId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "生产信息列表")
    private List<SampleChildOrderInfoVo> sampleChildOrderInfoList;

    @ApiModelProperty(value = "配比")
    private BigDecimal rate;

    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "商品对照关系")
    @JsonProperty("processOrderMaterialCompareList")
    private List<ProcessOrderMaterialCompareVo> processOrderMaterialCompareVoList;
}
