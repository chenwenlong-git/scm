package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/1/31.
 */
@Data
@ApiModel(value = "BatchCostResultBo", description = "批次码总成本结果")
public class BatchCostResultBo {
    @ApiModelProperty(value = "关联单号", example = "ORD12345")
    private String relateOrderNo;

    @ApiModelProperty(value = "批次码", example = "BATCH789")
    private String batchCode;

    @ApiModelProperty(value = "总成本", example = "1250.50")
    private BigDecimal totalCost;

    @ApiModelProperty(value = "单个成品原料成本", example = "500.75")
    private BigDecimal individualRawMaterialCost;

    @ApiModelProperty(value = "单个成品工序人力成本", example = "300.25")
    private BigDecimal individualProcessManpowerCost;

    @ApiModelProperty(value = "单个成品固定成本", example = "200.50")
    private BigDecimal individualFixedCost;

    @ApiModelProperty(value = "单个成品返工成本", example = "50.00")
    private BigDecimal individualReworkCost;
}
