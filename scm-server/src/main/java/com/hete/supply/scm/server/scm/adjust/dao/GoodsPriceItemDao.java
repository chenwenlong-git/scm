package com.hete.supply.scm.server.scm.adjust.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.adjust.entity.dto.GoodsPriceItemSearchListDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPriceItemPo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceItemSearchListVo;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceItemStatus;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品价格操作明细记录表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Component
@Validated
public class GoodsPriceItemDao extends BaseDao<GoodsPriceItemMapper, GoodsPriceItemPo> {
    public List<GoodsPriceItemPo> getListByGoodsPriceIdList(List<Long> goodsPriceIdList) {
        if (CollectionUtils.isEmpty(goodsPriceIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPriceItemPo>lambdaQuery()
                .in(GoodsPriceItemPo::getGoodsPriceId, goodsPriceIdList));
    }

    public List<GoodsPriceItemPo> getListByGoodsPriceIdListOrStatus(List<Long> goodsPriceIdList,
                                                                    List<GoodsPriceItemStatus> goodsPriceItemStatusList) {
        if (CollectionUtils.isEmpty(goodsPriceIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPriceItemPo>lambdaQuery()
                .in(GoodsPriceItemPo::getGoodsPriceId, goodsPriceIdList)
                .in(CollectionUtils.isNotEmpty(goodsPriceItemStatusList), GoodsPriceItemPo::getGoodsPriceItemStatus, goodsPriceItemStatusList)
                .orderByDesc(GoodsPriceItemPo::getGoodsPriceItemId));
    }

    public List<GoodsPriceItemPo> getListByEffectiveTimeAndStatusList(LocalDateTime effectiveTimeStart,
                                                                      LocalDateTime effectiveTimeEnd,
                                                                      List<GoodsPriceItemStatus> goodsPriceItemStatusList,
                                                                      List<GoodsPriceEffectiveStatus> goodsPriceEffectiveStatusList) {
        if (CollectionUtils.isEmpty(goodsPriceItemStatusList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPriceItemPo>lambdaQuery()
                .ge(null != effectiveTimeStart, GoodsPriceItemPo::getEffectiveTime, effectiveTimeStart)
                .le(null != effectiveTimeEnd, GoodsPriceItemPo::getEffectiveTime, effectiveTimeEnd)
                .in(CollectionUtils.isNotEmpty(goodsPriceItemStatusList), GoodsPriceItemPo::getGoodsPriceItemStatus, goodsPriceItemStatusList)
                .in(CollectionUtils.isNotEmpty(goodsPriceEffectiveStatusList), GoodsPriceItemPo::getGoodsPriceEffectiveStatus, goodsPriceEffectiveStatusList));
    }

    public List<GoodsPriceItemPo> getListByIdListAndStatus(List<Long> idList, List<GoodsPriceItemStatus> goodsPriceItemStatusList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPriceItemPo>lambdaQuery()
                .in(GoodsPriceItemPo::getGoodsPriceItemId, idList)
                .in(CollectionUtils.isNotEmpty(goodsPriceItemStatusList), GoodsPriceItemPo::getGoodsPriceItemStatus, goodsPriceItemStatusList));
    }

    public List<GoodsPriceItemPo> getListByStatusList(List<GoodsPriceItemStatus> goodsPriceItemStatusList) {
        if (CollectionUtils.isEmpty(goodsPriceItemStatusList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPriceItemPo>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(goodsPriceItemStatusList), GoodsPriceItemPo::getGoodsPriceItemStatus, goodsPriceItemStatusList));
    }

    public List<GoodsPriceItemSearchListVo> getGoodsPriceItemListByDto(GoodsPriceItemSearchListDto dto) {
        return baseMapper.getGoodsPriceItemListByDto(dto);
    }

    public List<GoodsPriceItemPo> getListByIdList(List<Long> goodsPriceItemIdList) {
        if (CollectionUtils.isEmpty(goodsPriceItemIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPriceItemPo>lambdaQuery()
                .in(GoodsPriceItemPo::getGoodsPriceItemId, goodsPriceItemIdList));
    }

    public List<GoodsPriceItemPo> getListByGoodsPriceId(Long goodsPriceId) {
        if (null == goodsPriceId) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPriceItemPo>lambdaQuery()
                .eq(GoodsPriceItemPo::getGoodsPriceId, goodsPriceId));
    }

    public List<GoodsPriceItemPo> getListByGoodsPriceIdsAndEffectiveStatus(List<Long> goodsPriceIdList,
                                                                           List<GoodsPriceEffectiveStatus> goodsPriceEffectiveStatusList) {
        if (CollectionUtils.isEmpty(goodsPriceIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPriceItemPo>lambdaQuery()
                .in(GoodsPriceItemPo::getGoodsPriceId, goodsPriceIdList)
                .in(CollectionUtils.isNotEmpty(goodsPriceEffectiveStatusList), GoodsPriceItemPo::getGoodsPriceEffectiveStatus, goodsPriceEffectiveStatusList)
                .orderByDesc(GoodsPriceItemPo::getGoodsPriceItemId));
    }
}
