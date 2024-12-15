package com.hete.supply.scm.server.scm.adjust.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.adjust.converter.ChannelConverter;
import com.hete.supply.scm.server.scm.adjust.dao.ChannelDao;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceDao;
import com.hete.supply.scm.server.scm.adjust.entity.dto.ChannelSaveDto;
import com.hete.supply.scm.server.scm.adjust.entity.dto.ChannelSearchDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.ChannelPo;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.ChannelVo;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
import com.hete.supply.scm.server.scm.adjust.service.base.GoodsPriceBaseService;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/6/19 10:01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelBizService {

    private final ChannelDao channelDao;
    private final GoodsPriceDao goodsPriceDao;
    private final GoodsPriceBaseService goodsPriceBaseService;


    /**
     * 获取渠道全部列表
     *
     * @param dto:
     * @return List<ChannelVo>
     * @author ChenWenLong
     * @date 2024/8/28 11:30
     */
    public List<ChannelVo> searchChannel(ChannelSearchDto dto) {
        List<ChannelPo> channelPoList = channelDao.searchChannel(dto);
        return ChannelConverter.listPoToListVo(channelPoList);
    }

    /**
     * 渠道确认提交
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/19 15:56
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveChannel(ChannelSaveDto dto) {
        // 验证数据
        Set<String> channelNameSet = new HashSet<>();
        for (ChannelSaveDto.ChannelSaveItemDto channelSaveItemDto : dto.getChannelSaveItemList()) {
            if (!channelNameSet.add(channelSaveItemDto.getChannelName())) {
                throw new ParamIllegalException("渠道名称:{}存在重复，请先修改后再提交！", channelSaveItemDto.getChannelName());
            }
        }

        List<Long> channelIdList = dto.getChannelSaveItemList().stream()
                .map(ChannelSaveDto.ChannelSaveItemDto::getChannelId)
                .filter(Objects::nonNull).collect(Collectors.toList());

        List<ChannelPo> channelPoList = channelDao.getByIdList(channelIdList);
        // 验证版本号
        for (ChannelSaveDto.ChannelSaveItemDto channelSaveItemDto : dto.getChannelSaveItemList()) {
            channelPoList.stream()
                    .filter(po -> po.getChannelId().equals(channelSaveItemDto.getChannelId())).findFirst()
                    .ifPresent(po -> {
                        Assert.isTrue(po.getVersion().equals(channelSaveItemDto.getVersion()), () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
                        Assert.isTrue(po.getChannelName().equals(channelSaveItemDto.getChannelName()), () -> new ParamIllegalException("已生成的渠道名称:{}不允许编辑，请刷新页面后在重新编辑！", channelSaveItemDto.getChannelName()));
                    });
        }


        // 创建渠道
        List<ChannelPo> insertChannelPoList = new ArrayList<>();
        dto.getChannelSaveItemList().stream()
                .filter(item -> null == item.getChannelId())
                .forEach(item -> {
                    ChannelPo insertChannelPo = new ChannelPo();
                    insertChannelPo.setChannelName(item.getChannelName());
                    insertChannelPo.setChannelStatus(item.getChannelStatus());
                    insertChannelPoList.add(insertChannelPo);
                });
        // 验证新增渠道名称是否已存在
        List<String> channelNameList = insertChannelPoList.stream().map(ChannelPo::getChannelName)
                .distinct()
                .collect(Collectors.toList());
        List<ChannelPo> channelPoExistNameList = channelDao.getByNameList(channelNameList);
        if (CollectionUtils.isNotEmpty(channelPoExistNameList)) {
            throw new BizException("渠道名称:{}已存在，请先修改后再提交！", channelPoExistNameList.stream().map(ChannelPo::getChannelName).collect(Collectors.joining(",")));
        }

        if (CollectionUtils.isNotEmpty(insertChannelPoList)) {
            channelDao.insertBatch(insertChannelPoList);
        }


        // 更新渠道状态
        List<ChannelPo> updateChannelPoList = new ArrayList<>();
        for (ChannelPo channelPo : channelPoList) {
            dto.getChannelSaveItemList()
                    .stream()
                    .filter(item -> channelPo.getChannelId().equals(item.getChannelId()))
                    .filter(item -> !channelPo.getChannelStatus().equals(item.getChannelStatus()))
                    .findFirst()
                    .ifPresent(item -> {
                        ChannelPo updateChannelPo = new ChannelPo();
                        updateChannelPo.setChannelId(channelPo.getChannelId());
                        updateChannelPo.setVersion(channelPo.getVersion());
                        updateChannelPo.setChannelStatus(item.getChannelStatus());
                        updateChannelPoList.add(updateChannelPo);
                    });
        }

        // 更新是否通用
        List<GoodsPricePo> updateGoodsPricePoList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(updateChannelPoList)) {
            channelDao.updateBatchByIdVersion(updateChannelPoList);
            List<Long> channelIds = updateChannelPoList.stream().map(ChannelPo::getChannelId).collect(Collectors.toList());
            List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListByChannelIdList(channelIds);
            // 更新sku是否维护关系
            goodsPriceBaseService.updateGoodsPriceMaintain(goodsPricePoList);

            // 关闭渠道后去掉通用标签
            for (GoodsPricePo goodsPricePo : goodsPricePoList) {
                ChannelPo channelPoFirst = updateChannelPoList.stream()
                        .filter(channelPo -> channelPo.getChannelId().equals(goodsPricePo.getChannelId()))
                        .findFirst()
                        .orElse(null);
                if (GoodsPriceUniversal.UNIVERSAL.equals(goodsPricePo.getGoodsPriceUniversal())
                        && null != channelPoFirst
                        && BooleanType.FALSE.equals(channelPoFirst.getChannelStatus())) {
                    GoodsPricePo updateGoodsPricePo = new GoodsPricePo();
                    updateGoodsPricePo.setGoodsPriceId(goodsPricePo.getGoodsPriceId());
                    updateGoodsPricePo.setGoodsPriceUniversal(GoodsPriceUniversal.NO_UNIVERSAL);
                    updateGoodsPricePoList.add(updateGoodsPricePo);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(updateGoodsPricePoList)) {
            goodsPriceDao.updateBatchById(updateGoodsPricePoList);
        }

    }
}
