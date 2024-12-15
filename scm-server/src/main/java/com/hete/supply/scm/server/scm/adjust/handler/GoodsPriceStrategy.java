package com.hete.supply.scm.server.scm.adjust.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.server.scm.adjust.dao.AdjustPriceApproveItemDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceItemDao;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApproveItemPo;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPriceItemPo;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceItemStatus;
import com.hete.supply.scm.server.scm.adjust.service.base.GoodsPriceBaseService;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierProductCompareRefService;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/6/21 17:36
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsPriceStrategy implements AdjustPriceStrategy {
    private final AdjustPriceApproveItemDao adjustPriceApproveItemDao;
    private final GoodsPriceDao goodsPriceDao;
    private final GoodsPriceItemDao goodsPriceItemDao;
    private final GoodsPriceBaseService goodsPriceBaseService;
    private final SupplierProductCompareRefService supplierProductCompareRefService;

    @Override
    public void agreeHandle(AdjustPriceApprovePo adjustPriceApprovePo) {
        final List<AdjustPriceApproveItemPo> adjustPriceApproveItemPoList = adjustPriceApproveItemDao.getListByNo(adjustPriceApprovePo.getAdjustPriceApproveNo());
        if (CollectionUtils.isEmpty(adjustPriceApproveItemPoList)) {
            throw new BizException("审批单:{}不存在明细信息，数据处理失败！", adjustPriceApprovePo.getAdjustPriceApproveNo());
        }
        final List<Long> goodsPriceItemIdList = adjustPriceApproveItemPoList.stream()
                .map(AdjustPriceApproveItemPo::getGoodsPriceItemId)
                .collect(Collectors.toList());
        final Map<Long, BigDecimal> priceMap = adjustPriceApproveItemPoList.stream()
                .collect(Collectors.toMap(AdjustPriceApproveItemPo::getGoodsPriceItemId,
                        AdjustPriceApproveItemPo::getAdjustPrice));

        List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByIdListAndStatus(goodsPriceItemIdList, List.of(GoodsPriceItemStatus.TO_BE_APPROVE));
        if (CollectionUtils.isEmpty(goodsPriceItemPoList)) {
            log.info("查询不到对应更新商品调价信息");
            return;
        }

        List<Long> goodsPriceIdList = goodsPriceItemPoList.stream().map(GoodsPriceItemPo::getGoodsPriceId)
                .distinct()
                .collect(Collectors.toList());
        List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListByIdList(goodsPriceIdList);
        Map<Long, GoodsPricePo> goodsPricePoMap = goodsPricePoList.stream()
                .collect(Collectors.toMap(GoodsPricePo::getGoodsPriceId, goodsPricePo -> goodsPricePo));
        List<GoodsPriceItemPo> goodsPriceItemPoEffectiveList = goodsPriceItemDao.getListByGoodsPriceIdsAndEffectiveStatus(goodsPriceIdList, List.of(GoodsPriceEffectiveStatus.EFFECTIVE));


        // 获取当前CN的时间
        LocalDateTime localDateTimeNow = TimeUtil.now(TimeZoneId.CN);
        // 获取今天的日期
        LocalDate today = localDateTimeNow.toLocalDate();

        List<GoodsPricePo> updateGoodsPricePoList = new ArrayList<>();
        for (GoodsPriceItemPo goodsPriceItemPo : goodsPriceItemPoList) {
            GoodsPricePo goodsPricePo = goodsPricePoMap.get(goodsPriceItemPo.getGoodsPriceId());
            if (null == goodsPricePo) {
                throw new BizException("商品调价飞书回调匹配不到商品价格表，与数据中不匹配！");
            }

            BigDecimal channelPrice = Optional.ofNullable(priceMap.get(goodsPriceItemPo.getGoodsPriceItemId()))
                    .orElse(BigDecimal.ZERO);
            if (goodsPriceItemPo.getChannelPrice().compareTo(channelPrice) != 0) {
                throw new BizException("商品调价飞书回调渠道金额数据错误，与数据中不匹配！");
            }

            LocalDateTime effectiveTime = TimeUtil.convertZone(goodsPriceItemPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN);

            // 初始化设置生效状态：待生效
            goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.WAIT_EFFECTIVE);
            // 存在更晚的时间，并已经生效了
            List<GoodsPriceItemPo> goodsPriceItemPoIsAfterList = goodsPriceItemPoEffectiveList.stream()
                    .filter(goodsPriceItemPoEffective -> goodsPriceItemPoEffective.getGoodsPriceId().equals(goodsPriceItemPo.getGoodsPriceId()))
                    .filter(goodsPriceItemPoEffective -> TimeUtil.convertZone(goodsPriceItemPoEffective.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate().isAfter(effectiveTime.toLocalDate()))
                    .collect(Collectors.toList());
            // 已经存在更晚生效，就直接失效了
            if (CollectionUtils.isNotEmpty(goodsPriceItemPoIsAfterList)) {
                goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
            }
            // 判断 effectiveTime 是否小于等于今天就更新商品价格
            if (effectiveTime != null
                    && !effectiveTime.toLocalDate().isAfter(today)
                    && CollectionUtils.isEmpty(goodsPriceItemPoIsAfterList)) {
                GoodsPricePo updateGoodsPricePo = new GoodsPricePo();
                updateGoodsPricePo.setGoodsPriceId(goodsPricePo.getGoodsPriceId());
                updateGoodsPricePo.setChannelPrice(channelPrice);
                updateGoodsPricePo.setSku(goodsPricePo.getSku());
                updateGoodsPricePo.setSupplierCode(goodsPricePo.getSupplierCode());
                updateGoodsPricePo.setEffectiveTime(goodsPriceItemPo.getEffectiveTime());
                updateGoodsPricePo.setEffectiveRemark(goodsPriceItemPo.getEffectiveRemark());
                updateGoodsPricePo.setGoodsPriceUniversal(goodsPriceItemPo.getGoodsPriceUniversal());
                updateGoodsPricePoList.add(updateGoodsPricePo);
                goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.EFFECTIVE);
            }

            goodsPriceItemPo.setGoodsPriceItemStatus(GoodsPriceItemStatus.APPROVE_PASSED);
        }
        if (CollectionUtils.isNotEmpty(updateGoodsPricePoList)) {
            goodsPriceBaseService.updateGoodsPriceUniversal(updateGoodsPricePoList);
            // 更新sku维护关系
            goodsPriceBaseService.updateGoodsPriceMaintain(updateGoodsPricePoList);
        }

        goodsPriceItemDao.updateBatchByIdVersion(goodsPriceItemPoList);
        // 打上失效标签
        goodsPriceBaseService.updateBatchGoodsPriceItemInvalid(goodsPriceItemPoList);

        // 同步绑定供应商对照关系
        if (CollectionUtils.isNotEmpty(goodsPricePoList)) {
            // 发起调价审批时，只发起一个供应商的调价单
            Map<String, List<GoodsPricePo>> goodsPricePoBySupplierCodeMap = goodsPricePoList.stream()
                    .filter(goodsPricePo -> StringUtils.isNotBlank(goodsPricePo.getSupplierCode()))
                    .filter(goodsPricePo -> StringUtils.isNotBlank(goodsPricePo.getSku()))
                    .collect(Collectors.groupingBy(GoodsPricePo::getSupplierCode));
            goodsPricePoBySupplierCodeMap.forEach((String supplierCode, List<GoodsPricePo> goodsPricePos) -> {
                List<String> skuList = goodsPricePos.stream()
                        .map(GoodsPricePo::getSku)
                        .distinct()
                        .collect(Collectors.toList());
                supplierProductCompareRefService.insertSupplierProductCompareAndInventory(supplierCode, skuList);
            });
        }
    }

    @Override
    public void refuseHandle(AdjustPriceApprovePo adjustPriceApprovePo) {
        final List<AdjustPriceApproveItemPo> adjustPriceApproveItemPoList = adjustPriceApproveItemDao.getListByNo(adjustPriceApprovePo.getAdjustPriceApproveNo());
        if (CollectionUtils.isEmpty(adjustPriceApproveItemPoList)) {
            throw new BizException("审批单:{}不存在明细信息，数据处理失败！", adjustPriceApprovePo.getAdjustPriceApproveNo());
        }
        final List<Long> goodsPriceItemIdList = adjustPriceApproveItemPoList.stream()
                .map(AdjustPriceApproveItemPo::getGoodsPriceItemId)
                .collect(Collectors.toList());

        List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByIdListAndStatus(goodsPriceItemIdList, List.of(GoodsPriceItemStatus.TO_BE_APPROVE));
        if (CollectionUtils.isEmpty(goodsPriceItemPoList)) {
            log.info("查询不到对应更新商品调价信息");
            return;
        }
        goodsPriceItemPoList.forEach(po -> {
            po.setGoodsPriceItemStatus(GoodsPriceItemStatus.APPROVE_REJECT);
            po.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
        });
        goodsPriceItemDao.updateBatchByIdVersion(goodsPriceItemPoList);
    }

    @Override
    public void failHandle(AdjustPriceApprovePo adjustPriceApprovePo) {
        final List<AdjustPriceApproveItemPo> adjustPriceApproveItemPoList = adjustPriceApproveItemDao.getListByNo(adjustPriceApprovePo.getAdjustPriceApproveNo());
        if (CollectionUtils.isEmpty(adjustPriceApproveItemPoList)) {
            throw new BizException("审批单:{}不存在明细信息，数据处理失败！", adjustPriceApprovePo.getAdjustPriceApproveNo());
        }
        final List<Long> goodsPriceItemIdList = adjustPriceApproveItemPoList.stream()
                .map(AdjustPriceApproveItemPo::getGoodsPriceItemId)
                .collect(Collectors.toList());

        List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByIdListAndStatus(goodsPriceItemIdList, List.of(GoodsPriceItemStatus.TO_BE_APPROVE));
        if (CollectionUtils.isEmpty(goodsPriceItemPoList)) {
            log.info("查询不到对应更新商品调价信息");
            return;
        }
        goodsPriceItemPoList.forEach(po -> {
            po.setGoodsPriceItemStatus(GoodsPriceItemStatus.APPROVE_REJECT);
            po.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
        });
        goodsPriceItemDao.updateBatchByIdVersion(goodsPriceItemPoList);
    }

    @Override
    public ApproveType getHandlerType() {
        return ApproveType.GOODS_ADJUST;
    }
}
