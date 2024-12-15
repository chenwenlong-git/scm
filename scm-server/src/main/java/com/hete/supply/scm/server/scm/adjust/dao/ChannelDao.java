package com.hete.supply.scm.server.scm.adjust.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.adjust.entity.dto.ChannelSearchDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.ChannelPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 渠道表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Component
@Validated
public class ChannelDao extends BaseDao<ChannelMapper, ChannelPo> {

    public List<ChannelPo> getAll() {
        return list(Wrappers.lambdaQuery());
    }

    public List<ChannelPo> getByIdList(List<Long> channelIdList) {
        if (CollectionUtils.isEmpty(channelIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ChannelPo>lambdaQuery()
                .in(ChannelPo::getChannelId, channelIdList));
    }

    public List<ChannelPo> getByNameList(List<String> channelNameList) {
        if (CollectionUtils.isEmpty(channelNameList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ChannelPo>lambdaQuery()
                .in(ChannelPo::getChannelName, channelNameList));
    }

    public ChannelPo getByChannelName(String channelName) {
        if (StringUtils.isBlank(channelName)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<ChannelPo>lambdaQuery()
                .eq(ChannelPo::getChannelName, channelName));
    }

    public Map<Long, String> getNameMapByIdList(List<Long> channelIdList) {
        if (CollectionUtils.isEmpty(channelIdList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<ChannelPo>lambdaQuery()
                        .in(ChannelPo::getChannelId, channelIdList))
                .stream()
                .collect(Collectors.toMap(ChannelPo::getChannelId, ChannelPo::getChannelName));
    }

    public List<ChannelPo> searchChannel(ChannelSearchDto dto) {
        return baseMapper.selectList(Wrappers.<ChannelPo>lambdaQuery()
                .eq(null != dto.getChannelStatus(), ChannelPo::getChannelStatus, dto.getChannelStatus()));
    }
}
