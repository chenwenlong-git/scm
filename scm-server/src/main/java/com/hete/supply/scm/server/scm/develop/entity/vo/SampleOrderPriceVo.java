package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/1/25 11:04
 */
@Data
@NoArgsConstructor
public class SampleOrderPriceVo {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "批次码样品价格")
    private BigDecimal skuBatchSamplePrice;

    @ApiModelProperty(value = "渠道大货价格列表")
    private List<DevelopOrderPriceVo> developOrderPriceList;

}
