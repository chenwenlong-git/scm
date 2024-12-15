package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/2/21.
 */
@Data
public class SkuPriceResultBo {
    @ApiModelProperty(value = "SKU", example = "ABC123")
    private String sku;

    @ApiModelProperty(value = "批次码", example = "123456")
    private String batchCode;

    @ApiModelProperty(value = "最终价格", example = "10.50")
    private BigDecimal price = BigDecimal.ZERO;

    @ApiModelProperty(value = "仓库编码", example = "WH001")
    private String materialDeliveryWarehouseCode;
}
