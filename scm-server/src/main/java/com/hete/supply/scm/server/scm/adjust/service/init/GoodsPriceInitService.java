package com.hete.supply.scm.server.scm.adjust.service.init;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceItemDao;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPriceItemPo;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceItemStatus;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/7/23 18:24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsPriceInitService {

    public final GoodsPriceDao goodsPriceDao;
    public final GoodsPriceItemDao goodsPriceItemDao;
    /**
     * 页码
     */
    private final static Integer LOOP_SIZE = 50;

    @Transactional(rollbackFor = Exception.class)
    public void initGoodsPriceTask(String param) {
        final Long totalsCnt = goodsPriceDao.getTotalsCnt(param);
        for (int i = 1; i <= totalsCnt / LOOP_SIZE + 1; i++) {
            List<GoodsPriceItemPo> updateGoodsPriceItemPoList = new ArrayList<>();
            final IPage<GoodsPricePo> pageResult = goodsPriceDao.getByPage(PageDTO.of(i, LOOP_SIZE), param);
            final List<GoodsPricePo> records = pageResult.getRecords();
            List<Long> goodsPriceIdList = records.stream().map(GoodsPricePo::getGoodsPriceId).collect(Collectors.toList());
            List<GoodsPriceItemPo> goodsPriceItemPoList = goodsPriceItemDao.getListByGoodsPriceIdList(goodsPriceIdList);
            for (GoodsPricePo goodsPricePo : records) {
                if (null != goodsPricePo.getEffectiveTime()) {
                    LocalDate effectiveTime = TimeUtil.convertZone(goodsPricePo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
                    // 当前生效的或失效
                    List<GoodsPriceItemPo> goodsPriceItemPoEffectiveList = goodsPriceItemPoList.stream()
                            .filter(goodsPriceItemPo -> GoodsPriceItemStatus.APPROVE_PASSED.equals(goodsPriceItemPo.getGoodsPriceItemStatus()))
                            .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                            .filter(goodsPriceItemPo -> TimeUtil.convertZone(goodsPriceItemPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate().isEqual(effectiveTime))
                            .collect(Collectors.toList());
                    // 当前生效的
                    GoodsPriceItemPo goodsPriceItemPoEffective = goodsPriceItemPoEffectiveList.stream()
                            .max(Comparator.comparing(GoodsPriceItemPo::getGoodsPriceItemId))
                            .orElse(null);
                    if (null != goodsPriceItemPoEffective) {
                        goodsPriceItemPoEffective.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.EFFECTIVE);
                        updateGoodsPriceItemPoList.add(goodsPriceItemPoEffective);
                        // 其他的失效(处理相同生效时间的数据)
                        goodsPriceItemPoEffectiveList.stream()
                                .filter(goodsPriceItemPo -> !goodsPriceItemPo.getGoodsPriceItemId().equals(goodsPriceItemPoEffective.getGoodsPriceItemId()))
                                .forEach(goodsPriceItemPo -> {
                                    goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
                                    updateGoodsPriceItemPoList.add(goodsPriceItemPo);
                                });
                    }

                    // 失效的 审核通过而且生效时间后面
                    List<GoodsPriceItemPo> goodsPriceItemPoListInvalid = goodsPriceItemPoList.stream()
                            .filter(goodsPriceItemPo -> GoodsPriceItemStatus.APPROVE_PASSED.equals(goodsPriceItemPo.getGoodsPriceItemStatus()))
                            .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                            .filter(goodsPriceItemPo -> TimeUtil.convertZone(goodsPriceItemPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate().isBefore(TimeUtil.convertZone(goodsPricePo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate()))
                            .collect(Collectors.toList());
                    for (GoodsPriceItemPo goodsPriceItemPo : goodsPriceItemPoListInvalid) {
                        goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
                        updateGoodsPriceItemPoList.add(goodsPriceItemPo);
                    }

                    // 失效的 审核不通过
                    goodsPriceItemPoList.stream()
                            .filter(goodsPriceItemPo -> GoodsPriceItemStatus.APPROVE_REJECT.equals(goodsPriceItemPo.getGoodsPriceItemStatus()))
                            .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                            .forEach(goodsPriceItemPo -> {
                                goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
                                updateGoodsPriceItemPoList.add(goodsPriceItemPo);
                            });

                    // 待审核的
                    goodsPriceItemPoList.stream()
                            .filter(goodsPriceItemPo -> GoodsPriceItemStatus.TO_BE_APPROVE.equals(goodsPriceItemPo.getGoodsPriceItemStatus()))
                            .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                            .forEach(goodsPriceItemPo -> {
                                goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.WAIT_EXAMINE);
                                updateGoodsPriceItemPoList.add(goodsPriceItemPo);
                            });

                    // 待生效的
                    List<GoodsPriceItemPo> goodsPriceItemPoListWaitEffective = goodsPriceItemPoList.stream()
                            .filter(goodsPriceItemPo -> GoodsPriceItemStatus.APPROVE_PASSED.equals(goodsPriceItemPo.getGoodsPriceItemStatus()))
                            .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                            .filter(goodsPriceItemPo -> TimeUtil.convertZone(goodsPriceItemPo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate().isAfter(TimeUtil.convertZone(goodsPricePo.getEffectiveTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate()))
                            .collect(Collectors.toList());
                    for (GoodsPriceItemPo goodsPriceItemPo : goodsPriceItemPoListWaitEffective) {
                        goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.WAIT_EFFECTIVE);
                        updateGoodsPriceItemPoList.add(goodsPriceItemPo);
                    }

                } else {
                    // 待生效的（还没审批）
                    goodsPriceItemPoList.stream()
                            .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                            .filter(goodsPriceItemPo -> GoodsPriceItemStatus.TO_BE_APPROVE.equals(goodsPriceItemPo.getGoodsPriceItemStatus()))
                            .forEach(goodsPriceItemPo -> {
                                goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.WAIT_EXAMINE);
                                updateGoodsPriceItemPoList.add(goodsPriceItemPo);
                            });
                    // 待生效的（审批通过）
                    goodsPriceItemPoList.stream()
                            .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                            .filter(goodsPriceItemPo -> GoodsPriceItemStatus.APPROVE_PASSED.equals(goodsPriceItemPo.getGoodsPriceItemStatus()))
                            .forEach(goodsPriceItemPo -> {
                                goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.WAIT_EFFECTIVE);
                                updateGoodsPriceItemPoList.add(goodsPriceItemPo);
                            });
                    // （审批拒绝）
                    goodsPriceItemPoList.stream()
                            .filter(goodsPriceItemPo -> goodsPriceItemPo.getGoodsPriceId().equals(goodsPricePo.getGoodsPriceId()))
                            .filter(goodsPriceItemPo -> GoodsPriceItemStatus.APPROVE_REJECT.equals(goodsPriceItemPo.getGoodsPriceItemStatus()))
                            .forEach(goodsPriceItemPo -> {
                                goodsPriceItemPo.setGoodsPriceEffectiveStatus(GoodsPriceEffectiveStatus.INVALID);
                                updateGoodsPriceItemPoList.add(goodsPriceItemPo);
                            });
                }
            }

            if (CollectionUtils.isNotEmpty(updateGoodsPriceItemPoList)) {
                goodsPriceItemDao.updateBatchById(updateGoodsPriceItemPoList);
            }
        }
    }
}
