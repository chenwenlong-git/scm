package com.hete.supply.scm.server.scm.supplier.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuGetSuggestSupplierBo;
import com.hete.support.api.enums.BooleanType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 责任链模式获取推荐供应商
 *
 * @author ChenWenLong
 * @date 2024/8/5 16:57
 */

public abstract class AbstractSkuGetSuggestSupplierHandler {
    protected AbstractSkuGetSuggestSupplierHandler nextHandler;

    /**
     * 设置下一个处理器
     *
     * @param nextHandler:
     * @author ChenWenLong
     * @date 2024/8/5 18:18
     */
    public void setNextHandler(AbstractSkuGetSuggestSupplierHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 处理器算法逻辑
     *
     * @param dtoList:
     * @param resultBo:
     * @return SkuGetSuggestSupplierBo
     * @author ChenWenLong
     * @date 2024/8/7 10:51
     */
    public SkuGetSuggestSupplierBo handle(List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoList,
                                          SkuGetSuggestSupplierBo resultBo) {
        List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoNewList = new ArrayList<>(dtoList);
        // 每一步的处理数据
        SkuGetSuggestSupplierBo resultData = handleData(dtoNewList, resultBo);
        // 如果直接空或设置返回，直接返回
        if (null == resultData || BooleanType.TRUE.equals(resultData.getIsResult())) {
            return resultData;
        }

        // 如果结果中仅有一个默认供应商或设置直接返回的SKU，将其标记为默认并从输入Dto中移除
        dtoNewList.removeIf(input -> {
            SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo skuGetSuggestSupplierBo = Optional.ofNullable(resultData.getSkuGetSuggestSupplierBoList())
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(result -> result.getBusinessId().equals(input.getBusinessId()))
                    .findFirst()
                    .orElse(null);

            // 设置直接返回数据
            if (skuGetSuggestSupplierBo != null && BooleanType.TRUE.equals(skuGetSuggestSupplierBo.getIsIdResult())) {
                return true;
            }

            // 结果中仅有一个默认供应商
            if (skuGetSuggestSupplierBo != null
                    && CollectionUtils.isNotEmpty(skuGetSuggestSupplierBo.getSkuGetSuggestSupplierItemBoList())
                    && skuGetSuggestSupplierBo.getSkuGetSuggestSupplierItemBoList().size() == 1) {
                return true;
            }
            return false;
        });

        // 继续下一个处理器
        if (nextHandler != null) {
            return nextHandler.handle(dtoNewList, resultData);
        }

        return resultData;
    }


    /**
     * 指定处理顺序(升序)
     *
     * @return int
     * @author ChenWenLong
     * @date 2024/8/6 18:03
     */
    protected abstract int sort();

    /**
     * 处理数据
     *
     * @param dtoList:
     * @param resultBo:
     * @return SkuGetSuggestSupplierBo
     * @author ChenWenLong
     * @date 2024/8/7 10:55
     */
    protected abstract SkuGetSuggestSupplierBo handleData(List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoList, SkuGetSuggestSupplierBo resultBo);

}
