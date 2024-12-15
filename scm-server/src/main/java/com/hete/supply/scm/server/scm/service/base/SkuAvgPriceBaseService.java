package com.hete.supply.scm.server.scm.service.base;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.converter.SkuAvgPriceConverter;
import com.hete.supply.scm.server.scm.dao.SkuAvgPriceDao;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeItemBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAvgPriceBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAvgUpdateBo;
import com.hete.supply.scm.server.scm.entity.po.SkuAvgPricePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/31 14:03
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SkuAvgPriceBaseService {
    private final PlmRemoteService plmRemoteService;
    private final SkuAvgPriceDao skuAvgPriceDao;
    private final WmsRemoteService wmsRemoteService;


    /**
     * 对固定品类下的sku返回sku均价
     *
     * @param skuAndBatchCodeBo
     * @return
     */
    public List<SkuAvgPriceBo> getSkuAvgPrice(@NotNull @Validated SkuAndBatchCodeBo skuAndBatchCodeBo) {
        log.info("均价入参:{}", JSON.toJSONString(skuAndBatchCodeBo));
        final List<SkuAndBatchCodeItemBo> skuAndBatchCodeItemBoList = skuAndBatchCodeBo.getSkuAndBatchCodeItemBoList();
        // 处理入参，对相同sku进行合并
        final Map<String, List<SkuAndBatchCodeItemBo>> skuBatchAndItemBoListMap = skuAndBatchCodeItemBoList.stream()
                .collect(Collectors.groupingBy(bo -> bo.getSku() + bo.getSkuBatchCode()));
        // 合并后的
        List<SkuAndBatchCodeItemBo> combineItemBoList = new ArrayList<>();
        skuBatchAndItemBoListMap.forEach((key, list) -> {
            final SkuAndBatchCodeItemBo skuAndBatchCodeItemBo = list.get(0);
            // 合并后的itemBo
            final SkuAndBatchCodeItemBo combineItemBo = new SkuAndBatchCodeItemBo();
            combineItemBo.setSku(skuAndBatchCodeItemBo.getSku());
            combineItemBo.setSkuBatchCode(skuAndBatchCodeItemBo.getSkuBatchCode());
            combineItemBo.setAccrueCnt(list.stream().mapToInt(SkuAndBatchCodeItemBo::getAccrueCnt).sum());
            combineItemBo.setAccruePrice(list.stream().map(SkuAndBatchCodeItemBo::getAccruePrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            combineItemBoList.add(combineItemBo);
        });
        log.info("处理后，均价入参:{}", JSON.toJSONString(combineItemBoList));


        final List<String> skuList = combineItemBoList.stream()
                .map(SkuAndBatchCodeItemBo::getSku)
                .distinct()
                .collect(Collectors.toList());
        // sku对应二级品类id map
        final Map<String, Long> skuCategoryIdMap = plmRemoteService.getSkuSecondCategoriesIdMapBySkuList(skuList);
        // 配置的固定批次码的sku二级品类id

        final List<Long> batchCodeCategoryIdList = wmsRemoteService.getRegularSkuCategoryIdList();

        // 获取固定品类sku list
        final List<String> regularSkuList = combineItemBoList.stream()
                .map(SkuAndBatchCodeItemBo::getSku)
                .filter(sku -> {
                    final Long categoryId = skuCategoryIdMap.get(sku);
                    return batchCodeCategoryIdList.contains(categoryId);
                }).collect(Collectors.toList());

        final List<SkuAvgPricePo> skuAvgPricePoList = skuAvgPriceDao.getListBySkuList(regularSkuList,
                skuAndBatchCodeBo.getSkuAvgPriceBizType());
        final Map<String, SkuAvgPricePo> skuAvgPoMap = skuAvgPricePoList.stream()
                .collect(Collectors.toMap(SkuAvgPricePo::getSku, Function.identity()));


        final List<SkuAvgPricePo> newSkuAvgPricePoList = new ArrayList<>();
        final List<SkuAvgUpdateBo> skuAvgUpdateBoList = new ArrayList<>();
        final List<SkuAvgPriceBo> skuAvgPriceBoList = new ArrayList<>();

        combineItemBoList.forEach(bo -> {
            // 判断sku是否为固定品类的sku
            if (regularSkuList.contains(bo.getSku())) {
                final SkuAvgPricePo skuAvgPricePo = skuAvgPoMap.get(bo.getSku());
                // 获取该sku的均价信息
                if (null == skuAvgPricePo) {
                    // 获取不到时，新增一条sku均价数据
                    final SkuAvgPricePo newSkuAvgPricePo = new SkuAvgPricePo();
                    newSkuAvgPricePo.setSku(bo.getSku());
                    newSkuAvgPricePo.setSkuBatchCode(bo.getSkuBatchCode());
                    newSkuAvgPricePo.setAccrueCnt(bo.getAccrueCnt());
                    newSkuAvgPricePo.setAccruePrice(bo.getAccruePrice());
                    newSkuAvgPricePo.setSkuAvgPriceBizType(skuAndBatchCodeBo.getSkuAvgPriceBizType());
                    newSkuAvgPricePoList.add(newSkuAvgPricePo);

                    // 出参
                    final SkuAvgPriceBo skuAvgPriceBo = SkuAvgPriceConverter.paramToSkuAvgBo(bo.getSku(),
                            bo.getSkuBatchCode(), bo.getAccrueCnt(), bo.getAccruePrice());
                    skuAvgPriceBoList.add(skuAvgPriceBo);
                } else {
                    // 能获取到sku均价时，组装更新的bo
                    final SkuAvgUpdateBo skuAvgUpdateBo = new SkuAvgUpdateBo();
                    skuAvgUpdateBo.setSku(skuAvgPricePo.getSku());
                    skuAvgUpdateBo.setSkuAvgPriceBizType(skuAvgPricePo.getSkuAvgPriceBizType());
                    skuAvgUpdateBo.setAddAccrueCnt(bo.getAccrueCnt());
                    skuAvgUpdateBo.setAddAccruePrice(bo.getAccruePrice());
                    skuAvgUpdateBoList.add(skuAvgUpdateBo);

                    // 出参
                    final SkuAvgPriceBo skuAvgPriceBo = SkuAvgPriceConverter.paramToSkuAvgBo(bo.getSku(),
                            bo.getSkuBatchCode(), skuAvgPricePo.getAccrueCnt() + bo.getAccrueCnt(),
                            skuAvgPricePo.getAccruePrice().add(bo.getAccruePrice()));
                    skuAvgPriceBoList.add(skuAvgPriceBo);
                }
            } else {
                // 不在固定品类下的sku，直接按照入参返回
                // 出参
                final SkuAvgPriceBo skuAvgPriceBo = SkuAvgPriceConverter.paramToSkuAvgBo(bo.getSku(),
                        bo.getSkuBatchCode(), bo.getAccrueCnt(), bo.getAccruePrice());
                skuAvgPriceBoList.add(skuAvgPriceBo);
            }
        });

        if (CollectionUtils.isNotEmpty(newSkuAvgPricePoList)) {
            skuAvgPriceDao.insertBatch(newSkuAvgPricePoList);
        }
        if (CollectionUtils.isNotEmpty(skuAvgUpdateBoList)) {
            skuAvgPriceDao.updateSkuAvgPrice(skuAvgUpdateBoList);
        }

        return skuAvgPriceBoList;
    }
}
