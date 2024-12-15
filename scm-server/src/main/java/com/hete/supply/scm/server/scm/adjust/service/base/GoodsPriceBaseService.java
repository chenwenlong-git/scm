package com.hete.supply.scm.server.scm.adjust.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.GoodsPriceMaintain;
import com.hete.supply.scm.server.scm.adjust.dao.ChannelDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceItemDao;
import com.hete.supply.scm.server.scm.adjust.entity.bo.GoodsPriceAddBo;
import com.hete.supply.scm.server.scm.adjust.entity.po.ChannelPo;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPriceItemPo;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceItemStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/10 16:46
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsPriceBaseService {

    private final GoodsPriceDao goodsPriceDao;
    private final SkuInfoDao skuInfoDao;
    private final ChannelDao channelDao;
    private final GoodsPriceItemDao goodsPriceItemDao;
    private final IdGenerateService idGenerateService;

    /**
     * 更新sku的维护关系
     *
     * @param goodsPricePoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/28 14:16
     */
    public void updateGoodsPriceMaintain(List<GoodsPricePo> goodsPricePoList) {
        log.info("更新sku的维护关系PO={}", JacksonUtil.parse2Str(goodsPricePoList));
        if (CollectionUtils.isEmpty(goodsPricePoList)) {
            return;
        }

        List<String> skuList = goodsPricePoList.stream()
                .map(GoodsPricePo::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());

        List<GoodsPricePo> goodsPricePoSkuList = goodsPriceDao.getListBySkuList(skuList);
        List<Long> channelIdList = goodsPricePoSkuList.stream()
                .map(GoodsPricePo::getChannelId)
                .distinct()
                .collect(Collectors.toList());

        List<ChannelPo> channelPoList = channelDao.getByIdList(channelIdList);
        Map<Long, ChannelPo> channelPoMap = channelPoList.stream()
                .collect(Collectors.toMap(ChannelPo::getChannelId, channelPo -> channelPo));

        List<SkuInfoPo> skuInfoPoList = skuInfoDao.getListBySkuList(skuList);
        Map<String, SkuInfoPo> skuInfoPoMap = skuInfoPoList.stream()
                .collect(Collectors.toMap(SkuInfoPo::getSku, skuInfoPo -> skuInfoPo));

        List<SkuInfoPo> skuInfoPoInsertList = new ArrayList<>();
        List<SkuInfoPo> skuInfoPoUpdateList = new ArrayList<>();

        Map<String, List<GoodsPricePo>> goodsPricePoMap = goodsPricePoSkuList.stream().collect(Collectors.groupingBy(GoodsPricePo::getSku));

        goodsPricePoMap.forEach((String sku, List<GoodsPricePo> goodsPricePos) -> {
            List<GoodsPricePo> goodsPricePoVerifyList = goodsPricePos.stream()
                    .filter(goodsPricePo -> goodsPricePo.getChannelPrice() != null && goodsPricePo.getChannelPrice().compareTo(BigDecimal.ZERO) > 0)
                    .filter(goodsPricePo -> goodsPricePo.getChannelId() != null
                            && channelPoMap.containsKey(goodsPricePo.getChannelId())
                            && BooleanType.TRUE.equals(channelPoMap.get(goodsPricePo.getChannelId()).getChannelStatus()))
                    .collect(Collectors.toList());
            GoodsPriceMaintain goodsPriceMaintain = GoodsPriceMaintain.WAITING_MAINTAIN;
            if (CollectionUtils.isNotEmpty(goodsPricePoVerifyList)) {
                goodsPriceMaintain = GoodsPriceMaintain.MAINTAIN;
            }

            SkuInfoPo skuInfoPo = skuInfoPoMap.get(sku);
            if (null == skuInfoPo) {
                SkuInfoPo skuInfoInsertPo = new SkuInfoPo();
                skuInfoInsertPo.setSku(sku);
                skuInfoInsertPo.setGoodsPriceMaintain(goodsPriceMaintain);
                skuInfoPoInsertList.add(skuInfoInsertPo);
            } else {
                skuInfoPo.setGoodsPriceMaintain(goodsPriceMaintain);
                skuInfoPoUpdateList.add(skuInfoPo);
            }

        });

        skuInfoDao.insertBatch(skuInfoPoInsertList);

        if (CollectionUtils.isNotEmpty(skuInfoPoUpdateList)) {
            skuInfoDao.updateBatchById(skuInfoPoUpdateList);
        }

    }

    /**
     * 重新计算并打上失效标签
     *
     * @param updateGoodsPriceItemPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/7/24 13:55
     */
    public void updateBatchGoodsPriceItemInvalid(List<GoodsPriceItemPo> updateGoodsPriceItemPoList) {
        log.info("重新计算并打上失效标签PO={}", JacksonUtil.parse2Str(updateGoodsPriceItemPoList));
        if (CollectionUtils.isEmpty(updateGoodsPriceItemPoList)) {
            return;
        }
        List<GoodsPriceItemPo> poEffectiveList = updateGoodsPriceItemPoList.stream()
                .filter(goodsPriceItemPo -> GoodsPriceEffectiveStatus.EFFECTIVE.equals(goodsPriceItemPo.getGoodsPriceEffectiveStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(poEffectiveList)) {
            return;
        }
        List<Long> goodsPriceItemIdList = poEffectiveList.stream()
                .map(GoodsPriceItemPo::getGoodsPriceItemId)
                .collect(Collectors.toList());
        List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByIdList(goodsPriceItemIdList);
        List<Long> goodsPriceIdList = goodsPriceItemPoList.stream()
                .map(GoodsPriceItemPo::getGoodsPriceId)
                .distinct()
                .collect(Collectors.toList());
        // 获取当前其他商品调价日志信息
        List<GoodsPriceItemPo> goodsPriceItemPoOtherList = goodsPriceItemDao.getListByGoodsPriceIdsAndEffectiveStatus(goodsPriceIdList, List.of(GoodsPriceEffectiveStatus.EFFECTIVE, GoodsPriceEffectiveStatus.WAIT_EFFECTIVE));

        // 更新的PO列表
        List<GoodsPriceItemPo> updateGoodsPriceItemPoInvalidList = new ArrayList<>();
        for (Long goodsPriceId : goodsPriceIdList) {
            List<GoodsPriceItemPo> goodsPriceItemPoFilterList = goodsPriceItemPoOtherList.stream()
                    .filter(goodsPriceItemPoOther -> goodsPriceItemPoOther.getGoodsPriceId().equals(goodsPriceId))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(goodsPriceItemPoFilterList)) {
                // 设置失效的
                goodsPriceItemPoFilterList.stream()
                        .filter(goodsPriceItemPo -> GoodsPriceEffectiveStatus.EFFECTIVE.equals(goodsPriceItemPo.getGoodsPriceEffectiveStatus()))
                        .max(Comparator.comparing((GoodsPriceItemPo goodsPriceItemPo) ->
                                        TimeUtil.convertZone(goodsPriceItemPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate())
                                .thenComparing(GoodsPriceItemPo::getGoodsPriceItemId))
                        .ifPresent(goodsPriceItemPoEffective -> goodsPriceItemPoFilterList.stream()
                                .filter(goodsPriceItemPoFilter -> !goodsPriceItemPoFilter.getGoodsPriceItemId().equals(goodsPriceItemPoEffective.getGoodsPriceItemId()))
                                .filter(goodsPriceItemPoFilter -> TimeUtil.convertZone(goodsPriceItemPoFilter.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate().isBefore(TimeUtil.convertZone(goodsPriceItemPoEffective.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate())
                                        || TimeUtil.convertZone(goodsPriceItemPoFilter.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate().isEqual(TimeUtil.convertZone(goodsPriceItemPoEffective.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate()))
                                .forEach(goodsPriceItemPoFilter -> {
                                    goodsPriceItemPoFilter.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
                                    updateGoodsPriceItemPoInvalidList.add(goodsPriceItemPoFilter);
                                }));
            }

        }

        if (CollectionUtils.isNotEmpty(updateGoodsPriceItemPoInvalidList)) {
            goodsPriceItemDao.updateBatchById(updateGoodsPriceItemPoInvalidList);
        }
    }

    /**
     * 更新商品价格的PO并判断是否需要打上通用标签
     *
     * @param updateGoodsPricePoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/7/31 10:23
     */
    public void updateGoodsPriceUniversal(List<GoodsPricePo> updateGoodsPricePoList) {
        if (CollectionUtils.isEmpty(updateGoodsPricePoList)) {
            return;
        }
        List<GoodsPricePo> updateGoodsPricePoUniversalList = updateGoodsPricePoList.stream()
                .filter(updateGoodsPricePo -> GoodsPriceUniversal.UNIVERSAL.equals(updateGoodsPricePo.getGoodsPriceUniversal()))
                .collect(Collectors.toList());
        for (GoodsPricePo goodsPricePo : updateGoodsPricePoUniversalList) {
            List<GoodsPricePo> goodsPricePos = goodsPriceDao.getListBySkuAndSupplierCode(goodsPricePo.getSku(), goodsPricePo.getSupplierCode())
                    .stream()
                    .filter(po -> !goodsPricePo.getGoodsPriceId().equals(po.getGoodsPriceId()))
                    .filter(po -> GoodsPriceUniversal.UNIVERSAL.equals(po.getGoodsPriceUniversal()))
                    .collect(Collectors.toList());
            for (GoodsPricePo pricePo : goodsPricePos) {
                pricePo.setGoodsPriceUniversal(GoodsPriceUniversal.NO_UNIVERSAL);
                updateGoodsPricePoList.add(pricePo);
            }
        }
        goodsPriceDao.updateBatchById(updateGoodsPricePoList);
    }

    /**
     * 开发单的样品单增加商品价格
     *
     * @param goodsPriceAddBoList:
     * @author ChenWenLong
     * @date 2024/8/26 17:22
     */
    public void addGoodsPrice(List<GoodsPriceAddBo> goodsPriceAddBoList) {
        log.info("开发单的样品单增加商品价格，boList={}", JacksonUtil.parse2Str(goodsPriceAddBoList));
        if (CollectionUtils.isEmpty(goodsPriceAddBoList)) {
            return;
        }

        List<String> skuList = goodsPriceAddBoList.stream()
                .map(GoodsPriceAddBo::getSku)
                .distinct()
                .collect(Collectors.toList());
        List<Long> channelIdList = goodsPriceAddBoList.stream()
                .map(GoodsPriceAddBo::getChannelId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> channelNameMap = channelDao.getNameMapByIdList(channelIdList);

        // 查询商品价格信息
        List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListBySkuList(skuList);


        // 默认生效时间
        LocalDateTime localDateTimeNow = TimeUtil.now(TimeZoneId.CN);
        LocalDate today = localDateTimeNow.toLocalDate();
        LocalTime startOfDay = LocalTime.MIDNIGHT;
        LocalDateTime effectiveTime = LocalDateTime.of(today, startOfDay);
        effectiveTime = TimeUtil.convertZone(effectiveTime, TimeZoneId.CN, TimeZoneId.UTC);

        // 新增信息
        List<GoodsPricePo> insertGoodsPricePoList = new ArrayList<>();
        List<GoodsPriceItemPo> insertGoodsPriceItemPoList = new ArrayList<>();

        // 更新信息
        List<GoodsPricePo> updateGoodsPricePoList = new ArrayList<>();

        for (GoodsPriceAddBo goodsPriceAddBo : goodsPriceAddBoList) {

            GoodsPricePo goodsPricePo = goodsPricePoList.stream()
                    .filter(po -> po.getSku().equals(goodsPriceAddBo.getSku()))
                    .filter(po -> po.getChannelId().equals(goodsPriceAddBo.getChannelId()))
                    .filter(po -> po.getSupplierCode().equals(goodsPriceAddBo.getSupplierCode()))
                    .findFirst()
                    .orElse(null);

            BigDecimal originalPrice = BigDecimal.ZERO;

            if (null != goodsPricePo) {
                originalPrice = goodsPricePo.getChannelPrice();
                goodsPricePo.setChannelPrice(goodsPriceAddBo.getChannelPrice());
                goodsPricePo.setEffectiveTime(effectiveTime);
                updateGoodsPricePoList.add(goodsPricePo);
            } else {
                GoodsPricePo insertGoodsPricePo = insertGoodsPricePoList.stream()
                        .filter(po -> po.getSku().equals(goodsPriceAddBo.getSku()))
                        .filter(po -> po.getChannelId().equals(goodsPriceAddBo.getChannelId()))
                        .filter(po -> po.getSupplierCode().equals(goodsPriceAddBo.getSupplierCode()))
                        .findFirst()
                        .orElse(null);
                if (null == insertGoodsPricePo) {
                    goodsPricePo = new GoodsPricePo();
                    // 雪花id
                    long goodsPriceId = idGenerateService.getSnowflakeId();
                    goodsPricePo.setGoodsPriceId(goodsPriceId);
                    goodsPricePo.setSku(goodsPriceAddBo.getSku());
                    goodsPricePo.setSupplierCode(goodsPriceAddBo.getSupplierCode());
                    goodsPricePo.setChannelId(goodsPriceAddBo.getChannelId());
                    goodsPricePo.setChannelName(channelNameMap.get(goodsPriceAddBo.getChannelId()));
                    goodsPricePo.setChannelPrice(goodsPriceAddBo.getChannelPrice());
                    goodsPricePo.setEffectiveTime(effectiveTime);
                    insertGoodsPricePoList.add(goodsPricePo);
                } else {
                    goodsPricePo = insertGoodsPricePo;
                    insertGoodsPricePo.setChannelPrice(goodsPriceAddBo.getChannelPrice());
                    insertGoodsPricePo.setEffectiveTime(effectiveTime);
                }

            }

            GoodsPriceItemPo insertGoodsPriceItemPo = new GoodsPriceItemPo();

            insertGoodsPriceItemPo.setGoodsPriceId(goodsPricePo.getGoodsPriceId());
            insertGoodsPriceItemPo.setSku(goodsPriceAddBo.getSku());
            insertGoodsPriceItemPo.setOriginalPrice(originalPrice);
            insertGoodsPriceItemPo.setChannelPrice(goodsPriceAddBo.getChannelPrice());
            insertGoodsPriceItemPo.setEffectiveTime(effectiveTime);
            insertGoodsPriceItemPo.setGoodsPriceUniversal(GoodsPriceUniversal.NO_UNIVERSAL);
            insertGoodsPriceItemPo.setGoodsPriceItemStatus(GoodsPriceItemStatus.APPROVE_PASSED);
            insertGoodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.EFFECTIVE);
            insertGoodsPriceItemPoList.add(insertGoodsPriceItemPo);
        }

        goodsPriceDao.insertBatch(insertGoodsPricePoList);
        goodsPriceItemDao.insertBatch(insertGoodsPriceItemPoList);
        // 打上失效标签
        this.updateBatchGoodsPriceItemInvalid(insertGoodsPriceItemPoList);


        // 以最后更新为准
        if (CollectionUtils.isNotEmpty(updateGoodsPricePoList)) {
            goodsPriceDao.updateBatchById(updateGoodsPricePoList);
        }

        // 更新sku维护关系
        this.updateGoodsPriceMaintain(insertGoodsPricePoList);
        this.updateGoodsPriceMaintain(updateGoodsPricePoList);

    }

    /**
     * 通过sku获取生效的商品调价信息列表
     *
     * @param skuList:
     * @return List<GoodsPricePo>
     * @author ChenWenLong
     * @date 2024/9/26 11:01
     */
    public List<GoodsPricePo> getEffectiveListBySkuList(List<String> skuList) {
        return goodsPriceDao.getListBySkuList(skuList).stream()
                .filter(goodsPricePo -> null != goodsPricePo.getEffectiveTime())
                .collect(Collectors.toList());
    }
}
