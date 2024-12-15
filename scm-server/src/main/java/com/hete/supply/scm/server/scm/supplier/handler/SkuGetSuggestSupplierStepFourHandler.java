package com.hete.supply.scm.server.scm.supplier.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.server.scm.dao.ProduceDataItemSupplierDao;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataItemGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataItemGetSuggestSupplierVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuGetSuggestSupplierBo;
import com.hete.support.api.enums.BooleanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/8/7 15:36
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkuGetSuggestSupplierStepFourHandler extends AbstractSkuGetSuggestSupplierHandler {

    private final ProduceDataItemSupplierDao produceDataItemSupplierDao;

    @Override
    protected int sort() {
        return 4;
    }

    @Override
    protected SkuGetSuggestSupplierBo handleData(List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoList,
                                                 SkuGetSuggestSupplierBo resultBo) {
        log.info("推荐供应商第四步开始:入参的Dto={},入参结果Bo={}", JSON.toJSONString(dtoList), JSON.toJSONString(resultBo));
        if (CollectionUtils.isEmpty(dtoList)) {
            resultBo.setIsResult(BooleanType.TRUE);
            return resultBo;
        }
        // 实现第四步批量处理逻辑
        List<ProduceDataItemGetSuggestSupplierBo> boList = new ArrayList<>();
        for (SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto skuAndBusinessIdBatchDto : dtoList) {
            // 获取更新的BO
            SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo skuGetSuggestSupplierListBo = resultBo.getSkuGetSuggestSupplierBoList().stream()
                    .filter(bo -> bo.getBusinessId().equals(skuAndBusinessIdBatchDto.getBusinessId()))
                    .findFirst()
                    .orElse(null);
            if (skuGetSuggestSupplierListBo != null && CollectionUtils.isNotEmpty(skuGetSuggestSupplierListBo.getSkuGetSuggestSupplierItemBoList())) {
                for (SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo skuGetSuggestSupplierItemBo : skuGetSuggestSupplierListBo.getSkuGetSuggestSupplierItemBoList()) {
                    ProduceDataItemGetSuggestSupplierBo produceDataItemGetSuggestSupplierBo = new ProduceDataItemGetSuggestSupplierBo();
                    produceDataItemGetSuggestSupplierBo.setSupplierCode(skuGetSuggestSupplierItemBo.getSupplierCode());
                    produceDataItemGetSuggestSupplierBo.setSku(skuAndBusinessIdBatchDto.getSku());
                    boList.add(produceDataItemGetSuggestSupplierBo);
                }
            }
        }

        // 是否存在原料BOM的sku+供应商
        List<ProduceDataItemGetSuggestSupplierVo> produceDataItemGetSuggestSupplierVoList = produceDataItemSupplierDao.getListBySuggestSupplierBoList(boList);
        Map<String, ProduceDataItemGetSuggestSupplierVo> produceDataItemGetSuggestSupplierVoMap = produceDataItemGetSuggestSupplierVoList.stream()
                .collect(Collectors.toMap(item -> item.getSupplierCode() + item.getSku(), Function.identity(), (existing, replacement) -> existing));


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
            // 计算满足条件
            // 是否满足存在原料BOM的sku+供应商
            List<SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo> skuGetSuggestSupplierItemBoFilters = skuGetSuggestSupplierItemBos.stream()
                    .filter(skuGetSuggestSupplierItemBo -> produceDataItemGetSuggestSupplierVoMap.containsKey(skuGetSuggestSupplierItemBo.getSupplierCode() + skuAndBusinessIdBatchDto.getSku()))
                    .collect(Collectors.toList());

            // 仅有一个则标记默认而且返回结果
            if (CollectionUtils.isNotEmpty(skuGetSuggestSupplierItemBoFilters) && skuGetSuggestSupplierItemBoFilters.size() == 1) {
                skuGetSuggestSupplierItemBoFilters.get(0).setIsDefault(BooleanType.TRUE);
                skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemBoList(skuGetSuggestSupplierItemBoFilters);
                continue;
            }
            // 如果多个更新为满足存在原料BOM的供应商
            if (CollectionUtils.isNotEmpty(skuGetSuggestSupplierItemBoFilters) && skuGetSuggestSupplierItemBoFilters.size() > 1) {
                skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemBoList(skuGetSuggestSupplierItemBoFilters);
                continue;
            }

        }


        log.info("推荐供应商第四步结束:入参结果Bo={}", JSON.toJSONString(resultBo));
        return resultBo;
    }
}