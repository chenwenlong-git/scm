package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/1/24.
 */
@Data
@ApiModel(description = "批次码成本信息")
public class MaterialBatchCostBo {
    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "成本价格")
    private BigDecimal costPrice;
}
