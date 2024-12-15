package com.hete.supply.scm.demo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.wms.api.interna.entity.vo.BatchCodeInventoryVo;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/4/16.
 */
public class TestContains {
    public static void main(String[] args) {
        String jsonStr
                = "[{\"batchCode\":\"L5-PV-BW-24-NC-G50\",\"inStockAmount\":998491," +
                "\"skuCode\":\"L5-PV-BW-24-NC-G50\",\"supplierCode\":\"supplier-code\",\"supplierName\":\"物流时效创建\"," +
                "\"warehouseCode\":\"JY01\",\"warehouseName\":\"金塬01仓\"}]";
        List<BatchCodeInventoryVo> skuInventoryVos = JSONUtil.toList(JSONUtil.parseArray(jsonStr),
                BatchCodeInventoryVo.class);

        List<String> materialSkus = JSONUtil.toList(JSONUtil.parseArray("[\"L5-PV-BW-24-NC-G50\"]"), String.class);
        boolean allSkusExistInWms = CollectionUtils.isNotEmpty(skuInventoryVos) && skuInventoryVos.stream()
                .allMatch(item -> materialSkus.contains(item.getSkuCode()));
        for (BatchCodeInventoryVo skuInventoryVo : skuInventoryVos) {
            String skuCode = skuInventoryVo.getSkuCode();
            System.out.println("skuCode:" + skuCode);
            if (!materialSkus.contains(skuCode)) {
                System.out.println(materialSkus);
            }
        }
        System.out.println(allSkusExistInWms);
    }
}
