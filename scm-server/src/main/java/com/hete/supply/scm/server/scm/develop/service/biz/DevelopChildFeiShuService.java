package com.hete.supply.scm.server.scm.develop.service.biz;

import cn.hutool.core.date.DatePattern;
import com.google.common.collect.Lists;
import com.hete.supply.mc.api.msg.entity.dto.DingTalkOtoMsgDto;
import com.hete.supply.mc.api.msg.util.DingTalkMsgUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderPo;
import com.hete.supply.scm.server.scm.handler.DingTalkHandler;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.util.FreemarkerUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 开发子单飞书推送
 *
 * @author ChenWenLong
 * @date 2024/3/29 13:38
 */
@Service
@RequiredArgsConstructor
public class DevelopChildFeiShuService {
    private final ConsistencySendMqService consistencySendMqService;

    /**
     * 开发子单完成时推送飞书消息
     *
     * @param developChildOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/29 14:00
     */
    public void sendDevelopChildStateFeiShuMsg(DevelopChildOrderPo developChildOrderPo) {
        HashMap<String, String> hashMap = new HashMap<>();
        String updateTime = ScmTimeUtil.localDateTimeToStr(developChildOrderPo.getUpdateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN);
        hashMap.put("updateTime", updateTime);
        hashMap.put("updateUsername", developChildOrderPo.getUpdateUsername());
        hashMap.put("developParentOrderNo", developChildOrderPo.getDevelopParentOrderNo());
        hashMap.put("developChildOrderNo", developChildOrderPo.getDevelopChildOrderNo());
        hashMap.put("parentCreateUsername", developChildOrderPo.getParentCreateUsername());

        String file = FreemarkerUtil.processByFile("feishu_develop_state.ftl", hashMap);

        DingTalkOtoMsgDto msgDto = DingTalkMsgUtil.toO2oMdMsg(Lists.newArrayList(developChildOrderPo.getParentCreateUser()), "【开发子单】-开发子单状态变更提醒", file);
        consistencySendMqService.execSendMq(DingTalkHandler.class, msgDto);

    }

}
