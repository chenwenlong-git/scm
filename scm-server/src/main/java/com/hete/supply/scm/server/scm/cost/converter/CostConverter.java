package com.hete.supply.scm.server.scm.cost.converter;

import cn.hutool.core.date.DatePattern;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.vo.GoodsOfCostExportVo;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostDetailVo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostVo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPricingOrderInfoByPriceTimeBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseItemByReceiveTimeBo;
import com.hete.support.core.util.TimeZoneId;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/2/27 15:35
 */
public class CostConverter {
    public static GoodsCostDetailVo convertCostOfGoodsPoToVo(@NotNull CostOfGoodsPo costOfGoodsPo) {
        // 返回数据
        final GoodsCostDetailVo goodsCostDetailVo = new GoodsCostDetailVo();
        if (CostTimeType.DAY.equals(costOfGoodsPo.getCostTimeType())) {
            // 获取昨日商品成本信息
            goodsCostDetailVo.setYestInventory(costOfGoodsPo.getInventory());
            goodsCostDetailVo.setYestInventoryPrice(costOfGoodsPo.getInventoryPrice());
            goodsCostDetailVo.setYestWeightingPrice(costOfGoodsPo.getWeightingPrice());
            goodsCostDetailVo.setYestUpdateTime(costOfGoodsPo.getUpdateTime());
        }
        if (CostTimeType.MONTH.equals(costOfGoodsPo.getCostTimeType())) {
            // 获取月初商品成本信息
            goodsCostDetailVo.setMoInventory(costOfGoodsPo.getInventory());
            goodsCostDetailVo.setMoInventoryPrice(costOfGoodsPo.getInventoryPrice());
            goodsCostDetailVo.setMoWeightingPrice(costOfGoodsPo.getWeightingPrice());
            goodsCostDetailVo.setMoUpdateTime(costOfGoodsPo.getUpdateTime());
        }
        return goodsCostDetailVo;
    }

    public static List<GoodsOfCostExportVo> convertSearchPagePoListToExportVo(List<GoodsCostVo> voList,
                                                                              List<DevelopPricingOrderInfoByPriceTimeBo> developPricingOrderInfoPriceTimeBoList,
                                                                              List<PurchaseItemByReceiveTimeBo> purchaseItemByReceiveTimeBoList) {
        return Optional.ofNullable(voList)
                .orElse(new ArrayList<>())
                .stream()
                .map(vo -> {
                    GoodsOfCostExportVo goodsOfCostExportVo = new GoodsOfCostExportVo();
                    goodsOfCostExportVo.setWarehouseCode(vo.getWarehouseCode());
                    goodsOfCostExportVo.setWarehouseName(vo.getWarehouseName());
                    goodsOfCostExportVo.setSku(vo.getSku());
                    goodsOfCostExportVo.setSkuEncode(vo.getSkuEncode());
                    goodsOfCostExportVo.setInventory(vo.getInventory());
                    goodsOfCostExportVo.setInventoryPrice(vo.getInventoryPrice());
                    goodsOfCostExportVo.setWeightingPrice(vo.getWeightingPrice());
                    goodsOfCostExportVo.setCostTime(vo.getCostTime());
                    goodsOfCostExportVo.setUpdateTime(vo.getUpdateTime());
                    goodsOfCostExportVo.setUpdateTimeStr(ScmTimeUtil.localDateTimeToStr(vo.getUpdateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));

                    developPricingOrderInfoPriceTimeBoList.stream()
                            .filter(bo -> vo.getSku().equals(bo.getSku()))
                            .findFirst()
                            .ifPresent(bo -> {
                                goodsOfCostExportVo.setDevelopPricingPurchasePrice(bo.getBulkPrice());
                                goodsOfCostExportVo.setNuclearPriceTime(bo.getNuclearPriceTime());
                                goodsOfCostExportVo.setNuclearPriceTimeStr(ScmTimeUtil.localDateTimeToStr(bo.getNuclearPriceTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                            });
                    purchaseItemByReceiveTimeBoList.stream()
                            .filter(bo -> vo.getSku().equals(bo.getSku()))
                            .findFirst()
                            .ifPresent(bo -> {
                                goodsOfCostExportVo.setPurchaseChildPurchasePrice(bo.getPurchasePrice());
                                goodsOfCostExportVo.setReceiveOrderTime(bo.getReceiveOrderTime());
                                goodsOfCostExportVo.setReceiveOrderTimeStr(ScmTimeUtil.localDateTimeToStr(bo.getReceiveOrderTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                            });

                    return goodsOfCostExportVo;
                }).collect(Collectors.toList());
    }

}
