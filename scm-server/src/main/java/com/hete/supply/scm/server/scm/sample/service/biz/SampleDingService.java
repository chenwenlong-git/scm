package com.hete.supply.scm.server.scm.sample.service.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.google.common.collect.Lists;
import com.hete.supply.mc.api.msg.entity.dto.DingTalkOtoMsgDto;
import com.hete.supply.mc.api.msg.util.DingTalkMsgUtil;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.server.scm.handler.DingTalkHandler;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverItemDto;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FreemarkerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @author weiwenxin
 * @date 2023/1/29 10:52
 */
@Service
@RequiredArgsConstructor
public class SampleDingService {
    private final ConsistencySendMqService consistencySendMqService;

    public void sendChildDingTalkMsg(SampleChildOrderPo sampleChildOrderPo, String refuseReason) {
        HashMap<String, String> hashMap = this.getBaseHashMap(sampleChildOrderPo);

        hashMap.put("param1", sampleChildOrderPo.getSupplierName());
        hashMap.put("param7", refuseReason);

        String file = FreemarkerUtil.processByFile("ding_talk_sample_child.ftl", hashMap);

        DingTalkOtoMsgDto msgDto = DingTalkMsgUtil.toO2oMdMsg(Lists.newArrayList(sampleChildOrderPo.getCreateUser()), "processOrderStatus", file);
        consistencySendMqService.execSendMq(DingTalkHandler.class, msgDto);

    }

    public void sendChildReturnDingTalkMsg(SampleChildOrderPo sampleChildOrderPo, SampleReturnOrderPo sampleReturnOrderPo) {
        HashMap<String, String> hashMap = this.getBaseHashMap(sampleChildOrderPo);

        hashMap.put("param8", sampleReturnOrderPo.getSampleReturnOrderNo());
        hashMap.put("param9", sampleReturnOrderPo.getLogistics());
        hashMap.put("param10", sampleReturnOrderPo.getTrackingNo());

        String file = FreemarkerUtil.processByFile("ding_talk_sample_child.ftl", hashMap);

        DingTalkOtoMsgDto msgDto = DingTalkMsgUtil.toO2oMdMsg(Lists.newArrayList(sampleChildOrderPo.getCreateUser()), "processOrderStatus", file);
        consistencySendMqService.execSendMq(DingTalkHandler.class, msgDto);

    }

    private HashMap<String, String> getBaseHashMap(SampleChildOrderPo sampleChildOrderPo) {
        final SampleOrderStatus sampleOrderStatus = sampleChildOrderPo.getSampleOrderStatus();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNo", sampleChildOrderPo.getSampleChildOrderNo());
        hashMap.put("status", sampleOrderStatus.getRemark());
        hashMap.put("operatorUsername", GlobalContext.getUsername());
        String formatTime = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN);
        hashMap.put("operatorTime", formatTime);
        return hashMap;
    }

    public void sendChildDeliverDingTalkMsg(SampleChildOrderPo sampleChildOrderPo, SampleDeliverDto dto) {
        final int totalDeliverCnt = dto.getSampleDeliverItemList()
                .stream()
                .mapToInt(SampleDeliverItemDto::getDeliverCnt)
                .sum();
        HashMap<String, String> hashMap = this.getBaseHashMap(sampleChildOrderPo);

        hashMap.put("param3", "" + totalDeliverCnt);


        String file = FreemarkerUtil.processByFile("ding_talk_sample_child.ftl", hashMap);

        DingTalkOtoMsgDto msgDto = DingTalkMsgUtil.toO2oMdMsg(Lists.newArrayList(sampleChildOrderPo.getCreateUser()), "processOrderStatus", file);
        consistencySendMqService.execSendMq(DingTalkHandler.class, msgDto);

    }

    public void sendChildSuccessDingTalkMsg(SampleChildOrderPo sampleChildOrderPo, String sku) {
        HashMap<String, String> hashMap = this.getBaseHashMap(sampleChildOrderPo);

        hashMap.put("param7", sku);

        String file = FreemarkerUtil.processByFile("ding_talk_sample_child.ftl", hashMap);

        DingTalkOtoMsgDto msgDto = DingTalkMsgUtil.toO2oMdMsg(Lists.newArrayList(sampleChildOrderPo.getCreateUser()), "processOrderStatus", file);
        consistencySendMqService.execSendMq(DingTalkHandler.class, msgDto);

    }
}
