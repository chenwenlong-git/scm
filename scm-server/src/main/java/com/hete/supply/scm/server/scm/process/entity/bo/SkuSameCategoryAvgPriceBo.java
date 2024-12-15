package com.hete.supply.scm.server.scm.process.entity.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/3/19.
 */
@Data
public class SkuSameCategoryAvgPriceBo {
    private String sku;
    private BigDecimal avgPrice = BigDecimal.ZERO;
    private String materialDeliveryWarehouseCode;
}
