package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.bo.SkuAvgPriceBo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author weiwenxin
 * @date 2024/1/31 17:15
 */
public class SkuAvgPriceConverter {
    public static SkuAvgPriceBo paramToSkuAvgBo(String sku, String skuBatchCode, Integer accrueCnt,
                                                BigDecimal accruePrice) {
        final SkuAvgPriceBo skuAvgPriceBo = new SkuAvgPriceBo();
        skuAvgPriceBo.setSku(sku);
        skuAvgPriceBo.setSkuBatchCode(skuBatchCode);
        skuAvgPriceBo.setAccrueCnt(accrueCnt);
        skuAvgPriceBo.setAccruePrice(accruePrice);
        skuAvgPriceBo.setAvgPrice(skuAvgPriceBo.getAccruePrice().divide(BigDecimal.valueOf(skuAvgPriceBo.getAccrueCnt()),
                2, RoundingMode.DOWN));

        return skuAvgPriceBo;
    }
}
