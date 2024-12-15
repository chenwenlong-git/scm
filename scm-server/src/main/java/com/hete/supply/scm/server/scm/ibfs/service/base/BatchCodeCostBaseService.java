package com.hete.supply.scm.server.scm.ibfs.service.base;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.mc.api.msg.entity.dto.DingTalkOtoMsgDto;
import com.hete.supply.mc.api.msg.util.DingTalkMsgUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.handler.DingTalkHandler;
import com.hete.supply.scm.server.scm.ibfs.config.ScmBatchCostPriceProp;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.util.FreemarkerUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 批次码成本公共服务类
 *
 * @author yanjiawei
 * Created on 2024/9/12.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchCodeCostBaseService {
    private final ConsistencySendMqService consistencySendMqService;
    private final ScmBatchCostPriceProp scmBatchCostPriceProp;
    private final UdbRemoteService udbRemoteService;

    public void sendAppTips(String sku) {
        if (StrUtil.isBlank(sku)) {
            log.info("批次码成本价格异常关联sku为空！不发送提醒消息。");
            return;
        }

        List<String> abnormalPriceTipsUsers = scmBatchCostPriceProp.getAbnormalPriceTipsUsers();
        if (CollectionUtils.isEmpty(abnormalPriceTipsUsers)) {
            log.info("未配置批次码成本价格异常提醒用户，不发送提醒消息。");
            log.error("该sku：{}，月初加权单价为空，计算成本信息异常，请相关同事及时维护该sku的月初加权单价", sku);
            return;
        }

        String currentTime = ScmTimeUtil.localDateTimeToStr(LocalDateTime.now(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN);
        for (String tipsUser : abnormalPriceTipsUsers) {
            //模板参数值
            Map<String, String> tempValueMap = new HashMap<>();
            tempValueMap.put("currentTime", currentTime);
            tempValueMap.put("sku", sku);

            UserVo userVo = udbRemoteService.getByUserCode(tipsUser);
            if (Objects.nonNull(userVo)) {
                tempValueMap.put("tipsUserName", userVo.getUsername());
            } else {
                tempValueMap.put("tipsUserName", "");
            }

            //渲染模板
            String file = FreemarkerUtil.processByFile("abnormal_batch_price_tips.ftl", tempValueMap);
            DingTalkOtoMsgDto tipsParam = DingTalkMsgUtil.toO2oMdMsg(Lists.newArrayList(tipsUser), "sku批次价格为0异常提醒", file);
            consistencySendMqService.execSendMq(DingTalkHandler.class, tipsParam);
        }
    }
}
