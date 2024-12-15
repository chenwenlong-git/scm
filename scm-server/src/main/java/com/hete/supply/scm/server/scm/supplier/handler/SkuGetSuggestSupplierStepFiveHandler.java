package com.hete.supply.scm.server.scm.supplier.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.support.api.enums.BooleanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author ChenWenLong
 * @date 2024/8/7 15:36
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkuGetSuggestSupplierStepFiveHandler extends AbstractSkuGetSuggestSupplierHandler {


    @Override
    protected int sort() {
        return 5;
    }

    @Override
    protected SkuGetSuggestSupplierBo handleData(List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoList,
                                                 SkuGetSuggestSupplierBo resultBo) {
        log.info("推荐供应商第五步开始:入参的Dto={},入参结果Bo={}", JSON.toJSONString(dtoList), JSON.toJSONString(resultBo));
        if (CollectionUtils.isEmpty(dtoList)) {
            resultBo.setIsResult(BooleanType.TRUE);
            return resultBo;
        }

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
            // 对供应商列表根据供应商等级进行排序
            skuGetSuggestSupplierItemBos.sort(Comparator.comparing(
                    (SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo item) ->
                            Optional.ofNullable(item.getSupplierPo())
                                    .map(SupplierPo::getSupplierGrade)
                                    .map(SupplierGrade::getSort)
                                    .orElse(Integer.MAX_VALUE), Comparator.nullsLast(Integer::compareTo)).thenComparing(
                    (SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo item) ->
                            Optional.ofNullable(item.getPlanConfirmTime())
                                    .orElse(LocalDateTime.MIN),
                    Comparator.reverseOrder()));
            // 设置第一个元素的 isDefault 为 BooleanType.TRUE
            if (CollectionUtils.isNotEmpty(skuGetSuggestSupplierItemBos)) {
                skuGetSuggestSupplierItemBos.get(0).setIsDefault(BooleanType.TRUE);
            }

        }

        log.info("推荐供应商第五步结束:入参结果Bo={}", JSON.toJSONString(resultBo));
        return resultBo;
    }
}