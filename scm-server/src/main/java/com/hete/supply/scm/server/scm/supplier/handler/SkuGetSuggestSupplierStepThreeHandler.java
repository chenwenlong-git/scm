package com.hete.supply.scm.server.scm.supplier.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityResBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierCapacityRefService;
import com.hete.support.api.enums.BooleanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/8/7 15:36
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkuGetSuggestSupplierStepThreeHandler extends AbstractSkuGetSuggestSupplierHandler {

    private final SkuInfoDao skuInfoDao;
    private final SupplierCapacityRefService supplierCapacityRefService;
    /**
     * 计算供应商剩余产能的系数
     */
    private static final int SUPPLIER_CAPACITY_COEFFICIENT = 2;

    @Override
    protected int sort() {
        return 3;
    }

    @Override
    protected SkuGetSuggestSupplierBo handleData(List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoList,
                                                 SkuGetSuggestSupplierBo resultBo) {
        log.info("推荐供应商第三步开始:入参的Dto={},入参结果Bo={}", JSON.toJSONString(dtoList), JSON.toJSONString(resultBo));
        if (CollectionUtils.isEmpty(dtoList)) {
            resultBo.setIsResult(BooleanType.TRUE);
            return resultBo;
        }
        // 实现第三步批量处理逻辑
        List<String> skuList = dtoList.stream()
                .map(SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        // 获取sku的单件产能
        Map<String, SkuInfoPo> skuInfoPoMap = skuInfoDao.getMapBySkuList(skuList);
        // 查询不到对应数据，就进行下一步算法
        if (CollectionUtils.isEmpty(skuInfoPoMap)) {
            return resultBo;
        }

        // 获取供应商剩余产能
        List<SupplierCapacityQueryBo> supplierCapacityQueryBoList = new ArrayList<>();
        // 用于是否已经添加的SupplierCode + 时间组合查询条件
        Set<String> supplierCodeAndTimeSet = new HashSet<>();
        dtoList.forEach(dto -> {
            SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo skuGetSuggestSupplierListBo = resultBo.getSkuGetSuggestSupplierBoList().stream()
                    .filter(bo -> bo.getBusinessId().equals(dto.getBusinessId()))
                    .findFirst()
                    .orElse(null);
            if (skuGetSuggestSupplierListBo != null && CollectionUtils.isNotEmpty(skuGetSuggestSupplierListBo.getSkuGetSuggestSupplierItemBoList())) {
                skuGetSuggestSupplierListBo.getSkuGetSuggestSupplierItemBoList()
                        .forEach(bo -> {
                            SupplierCapacityQueryBo supplierCapacityQueryBo = new SupplierCapacityQueryBo();
                            supplierCapacityQueryBo.setSupplierCode(bo.getSupplierCode());
                            // 供应商（最晚上架时间-2-供应商物流时效）的剩余产能
                            LocalDate resultDate = this.getSupplierCapacityDay(dto.getLatestOnShelfTime(), bo.getSupplierPo());
                            supplierCapacityQueryBo.setCapacityDate(resultDate);
                            // 组合 key，形式是 SupplierCode + resultDate
                            String combinationKey = supplierCapacityQueryBo.getSupplierCode() + supplierCapacityQueryBo.getCapacityDate();
                            // 判断这个组合是否已经存在，如果不存在则加入列表
                            if (!supplierCodeAndTimeSet.contains(combinationKey)) {
                                supplierCodeAndTimeSet.add(combinationKey);
                                supplierCapacityQueryBoList.add(supplierCapacityQueryBo);
                            }
                        });
            }
        });
        log.info("推荐供应商第三步开始:查询供应商剩余产能入参Dto={}", JSON.toJSONString(supplierCapacityQueryBoList));
        List<SupplierCapacityResBo> supplierCapacityResBos = supplierCapacityRefService.querySupplierCapacityBatch(supplierCapacityQueryBoList);
        log.info("推荐供应商第三步开始:查询供应商剩余产能结果Bo={}", JSON.toJSONString(supplierCapacityResBos));
        Map<String, SupplierCapacityResBo> supplierCapacityResBoMap = supplierCapacityResBos.stream()
                .collect(Collectors.toMap(item -> item.getSupplierCode() + item.getCapacityDate().toString(), Function.identity(), (existing, replacement) -> existing));

        // 组装返回数据
        for (SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto skuAndBusinessIdBatchDto : dtoList) {
            // 获取更新的BO
            SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo skuGetSuggestSupplierListBo = resultBo.getSkuGetSuggestSupplierBoList().stream()
                    .filter(bo -> bo.getBusinessId().equals(skuAndBusinessIdBatchDto.getBusinessId()))
                    .findFirst()
                    .orElse(null);
            if (skuGetSuggestSupplierListBo == null) {
                continue;
            }

            List<SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo> skuGetSuggestSupplierItemBos = Optional.ofNullable(skuGetSuggestSupplierListBo.getSkuGetSuggestSupplierItemBoList())
                    .orElse(new ArrayList<>());
            // 计算满足条件供应商剩余产能大于等于订单产能的供应商
            List<SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo> skuGetSuggestSupplierItemBoFilters = skuGetSuggestSupplierItemBos.stream()
                    .filter(skuGetSuggestSupplierItemBo -> {
                        LocalDate resultDate = this.getSupplierCapacityDay(skuAndBusinessIdBatchDto.getLatestOnShelfTime(), skuGetSuggestSupplierItemBo.getSupplierPo());
                        // 获取供应商的剩余产能
                        SupplierCapacityResBo supplierCapacityResBo = supplierCapacityResBoMap.get(skuGetSuggestSupplierItemBo.getSupplierCode() + resultDate);
                        log.info("推荐供应商第三步开始:获取业务ID:{}供应商:{}时间:{}的剩余产能Bo=>{}", skuGetSuggestSupplierListBo.getBusinessId(),
                                skuGetSuggestSupplierItemBo.getSupplierCode(),
                                resultDate,
                                JSON.toJSONString(supplierCapacityResBo));
                        if (null == supplierCapacityResBo) {
                            return false;
                        }
                        BigDecimal supplierCapacity = supplierCapacityResBo.getNormalAvailableCapacity();
                        if (null == supplierCapacity) {
                            return false;
                        }
                        // 获取sku的单件产能
                        if (!skuInfoPoMap.containsKey(skuAndBusinessIdBatchDto.getSku())) {
                            return false;
                        }
                        log.info("推荐供应商第三步开始:获取业务ID:{}sku:{}的单件产能Po=>{}", skuGetSuggestSupplierListBo.getBusinessId(),
                                skuAndBusinessIdBatchDto.getSku(),
                                JSON.toJSONString(skuInfoPoMap.get(skuAndBusinessIdBatchDto.getSku())));
                        BigDecimal singleCapacity = skuInfoPoMap.get(skuAndBusinessIdBatchDto.getSku()).getSingleCapacity();
                        if (null == singleCapacity) {
                            return false;
                        }
                        // 获取订单所需产能
                        BigDecimal orderCapacity = singleCapacity.multiply(BigDecimal.valueOf(skuAndBusinessIdBatchDto.getPlaceOrderCnt()));
                        log.info("推荐供应商第三步开始:获取业务ID:{}供应商:{}的剩余产能:{}订单所需要的产能:{}", skuGetSuggestSupplierListBo.getBusinessId(),
                                skuGetSuggestSupplierItemBo.getSupplierCode(),
                                supplierCapacity,
                                orderCapacity);
                        // 供应商的剩余产能大于等于订单产能
                        if (supplierCapacity.compareTo(orderCapacity) >= 0) {
                            supplierCapacityResBo.setNormalAvailableCapacity(supplierCapacity.subtract(orderCapacity));
                            return true;
                        } else {
                            return false;
                        }

                    }).collect(Collectors.toList());

            // 仅有一个则标记默认而且返回结果
            if (CollectionUtils.isNotEmpty(skuGetSuggestSupplierItemBoFilters) && skuGetSuggestSupplierItemBoFilters.size() == 1) {
                skuGetSuggestSupplierItemBoFilters.get(0).setIsDefault(BooleanType.TRUE);
                skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemBoList(skuGetSuggestSupplierItemBoFilters);
                continue;
            }
            // 如果多个更新为满足产能的供应商
            if (CollectionUtils.isNotEmpty(skuGetSuggestSupplierItemBoFilters) && skuGetSuggestSupplierItemBoFilters.size() > 1) {
                skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemBoList(skuGetSuggestSupplierItemBoFilters);
                continue;
            }

        }


        log.info("推荐供应商第三步结束:入参结果Bo={}", JSON.toJSONString(resultBo));
        return resultBo;
    }

    /**
     * 获取供应商（最晚上架时间-2-供应商物流时效）的剩余产能
     *
     * @param latestOnShelfTime:
     * @param supplierPo:
     * @return LocalDate
     * @author ChenWenLong
     * @date 2024/8/8 14:26
     */
    private LocalDate getSupplierCapacityDay(@NotNull LocalDate latestOnShelfTime,
                                             SupplierPo supplierPo) {
        int logisticsAging = 0;
        if (null != supplierPo && null != supplierPo.getLogisticsAging()) {
            logisticsAging = supplierPo.getLogisticsAging();
        }
        // 供应商（最晚上架时间-2-供应商物流时效）的剩余产能
        return latestOnShelfTime.minusDays(SUPPLIER_CAPACITY_COEFFICIENT)
                .minusDays(logisticsAging);
    }
}