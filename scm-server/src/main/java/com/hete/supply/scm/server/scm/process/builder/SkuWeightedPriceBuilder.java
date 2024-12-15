package com.hete.supply.scm.server.scm.process.builder;

import com.hete.supply.scm.server.scm.process.entity.bo.SkuPriceQueryBo;
import com.hete.supply.scm.server.scm.process.entity.bo.SkuPriceResultBo;
import com.hete.supply.scm.server.scm.process.entity.bo.SkuWeightedPriceQueryBo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/2/21.
 */
public class SkuWeightedPriceBuilder {

    public static List<SkuPriceResultBo> buildSkuPriceResults(List<SkuPriceQueryBo> skuPriceQueryList) {
        return skuPriceQueryList.stream()
                .map(skuPriceQuery -> {
                    SkuPriceResultBo priceResult = new SkuPriceResultBo();
                    priceResult.setSku(skuPriceQuery.getSku());
                    priceResult.setBatchCode(skuPriceQuery.getBatchCode());
                    priceResult.setMaterialDeliveryWarehouseCode(skuPriceQuery.getMaterialDeliveryWarehouseCode());
                    // set price based on logic
                    return priceResult;
                })
                .collect(Collectors.toList());
    }

    public static List<SkuWeightedPriceQueryBo> buildWeightedPriceQueries(List<SkuPriceQueryBo> skuPriceQueryList) {
        return skuPriceQueryList.stream()
                .map(skuPriceQuery -> {
                    SkuWeightedPriceQueryBo weightedPriceQuery = new SkuWeightedPriceQueryBo();
                    weightedPriceQuery.setSku(skuPriceQuery.getSku());
                    weightedPriceQuery.setWarehouseCode(skuPriceQuery.getMaterialDeliveryWarehouseCode());
                    return weightedPriceQuery;
                })
                .collect(Collectors.toList());
    }

    public static List<SkuWeightedPriceQueryBo> buildWeightedPriceQueriesBySkuCodes(List<String> sameLaceAreaAndLengthSkuCodes) {
        return sameLaceAreaAndLengthSkuCodes.stream()
                .map(sameLaceAreaAndLengthSkuCode -> {
                    SkuWeightedPriceQueryBo weightedPriceQuery = new SkuWeightedPriceQueryBo();
                    weightedPriceQuery.setSku(sameLaceAreaAndLengthSkuCode);
                    return weightedPriceQuery;
                })
                .collect(Collectors.toList());
    }
}
