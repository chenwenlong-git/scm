package com.hete.supply.scm.server.scm.adjust.converter;

import com.hete.supply.scm.server.scm.adjust.entity.po.ChannelPo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.ChannelVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/6/19 11:23
 */
public class ChannelConverter {


    public static List<ChannelVo> listPoToListVo(List<ChannelPo> channelPoList) {
        return Optional.ofNullable(channelPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(channelPo -> {
                    ChannelVo channelVo = new ChannelVo();
                    channelVo.setChannelId(channelPo.getChannelId());
                    channelVo.setChannelName(channelPo.getChannelName());
                    channelVo.setChannelStatus(channelPo.getChannelStatus());
                    channelVo.setVersion(channelPo.getVersion());
                    return channelVo;
                }).collect(Collectors.toList());

    }


}
