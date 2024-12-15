package com.hete.supply.scm.server.scm.feishu.service.base;

import cn.hutool.core.collection.CollectionUtil;
import com.hete.supply.mc.api.msg.entity.dto.FeiShuGroupMsgDto;
import com.hete.supply.mc.api.msg.util.FeiShuMsgUtil;
import com.hete.supply.mc.api.workflow.enums.WorkflowResult;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.feishu.config.BfMsgConfig;
import com.hete.supply.scm.server.scm.feishu.config.FeiShuConfig;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.entity.bo.BfExcDataBo;
import com.hete.supply.scm.server.scm.feishu.handler.FeiShuGroupHandler;
import com.hete.supply.sda.api.scm.bf.entity.vo.ScmPurchaseExcDataVo;
import com.hete.supply.sda.api.scm.bf.entity.vo.ScmQcExcDataVo;
import com.hete.supply.sda.api.scm.bf.entity.vo.ScmReceiveExcDataVo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.util.FreemarkerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/5/21 19:23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FeiShuBaseService {
    private final FeishuAuditOrderDao feishuAuditOrderDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final FeiShuConfig feiShuConfig;
    private final SdaRemoteService sdaRemoteService;
    private final UdbRemoteService udbRemoteService;
    private final BfMsgConfig bfMsgConfig;


    /**
     * 返回审批失败次数
     *
     * @param feishuAuditOrderType
     * @return
     */
    public Long getApproveFailTimes(FeishuAuditOrderType feishuAuditOrderType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneYearAgo = now.minusYears(1);
        return feishuAuditOrderDao.getApproveFailTimes(oneYearAgo, now, feishuAuditOrderType, WorkflowResult.REFUSE);
    }

    @Transactional(rollbackFor = Exception.class)
    public void test(String msg) {
        final List<String> qcGoodList = new ArrayList<>();

        BfExcDataBo bfExcDataBo = new BfExcDataBo();
        bfExcDataBo.setImgCode("F1633435905817264128mKixk0eo");
        bfExcDataBo.setTitle("标题");
        bfExcDataBo.setContent(this.randomGetStr(qcGoodList));
        String msgContent = FreemarkerUtil.processByFile("bf_exc_data_card.ftl", bfExcDataBo);

        FeiShuGroupMsgDto dto = FeiShuMsgUtil.toFeiShuGroupMsgDto("7f9887dc-3cbf-4a53-b2cf-ca8f6eaef2fd", null, msgContent, List.of(bfExcDataBo.getImgCode()), false);
        consistencySendMqService.execSendMq(FeiShuGroupHandler.class, dto);
    }

    public void pushFeiShuMsg(BfExcDataBo bfExcDataBo) {

        String msgContent = FreemarkerUtil.processByFile("bf_exc_data_card.ftl", bfExcDataBo);

        FeiShuGroupMsgDto dto = FeiShuMsgUtil.toFeiShuGroupMsgDto(feiShuConfig.getBfMsgRobotToken(), null,
                msgContent, Collections.singletonList(bfExcDataBo.getImgCode()), false);
        consistencySendMqService.execSendMq(FeiShuGroupHandler.class, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bfQcExcDataJob() {
        LocalDate localDate = LocalDate.now();
        final List<ScmQcExcDataVo> scmQcExcDataVoList = sdaRemoteService.getBfQcExcData(localDate);
        String title = "质检异常推送";
        if (CollectionUtil.isEmpty(scmQcExcDataVoList)) {
            BfExcDataBo bfExcDataBo = new BfExcDataBo();
            bfExcDataBo.setImgCode(this.randomGetStr(bfMsgConfig.getGoodImgList()));
            bfExcDataBo.setTitle(title);
            bfExcDataBo.setContent(this.randomGetStr(bfMsgConfig.getQcGoodList()));
            this.pushFeiShuMsg(bfExcDataBo);
        } else {
            // 获取跟单飞书openId
            final List<String> confirmUserList = scmQcExcDataVoList.stream()
                    .map(ScmQcExcDataVo::getConfirmUser)
                    .distinct()
                    .collect(Collectors.toList());
            final List<UserVo> userVoList = udbRemoteService.getListByUserCodeList(confirmUserList);
            final Map<String, String> userCodeOpenIdMap = userVoList.stream()
                    .collect(Collectors.toMap(UserVo::getUserCode, UserVo::getFeiShuOpenId));

            // 按照跟单人排序
            final List<ScmQcExcDataVo> sortedList = scmQcExcDataVoList.stream()
                    .sorted(Comparator.comparing(ScmQcExcDataVo::getConfirmUser))
                    .collect(Collectors.toList());

            for (int i = 0; i < sortedList.size(); i += bfMsgConfig.getBatchSize()) {
                int endIndex = Math.min(i + bfMsgConfig.getBatchSize(), sortedList.size());
                List<ScmQcExcDataVo> batchList = sortedList.subList(i, endIndex);

                StringBuilder msg = new StringBuilder();
                for (ScmQcExcDataVo scmQcExcDataVo : batchList) {
                    final String feiShuOpenId = userCodeOpenIdMap.get(scmQcExcDataVo.getConfirmUser());
                    if (StringUtils.isBlank(feiShuOpenId)) {
                        log.error("用户:{}飞书id为空", scmQcExcDataVo.getConfirmUsername());
                        continue;
                    }

                    final String randomBadMsg = this.randomGetStr(bfMsgConfig.getQcBadList());
                    msg.append(String.format(randomBadMsg, scmQcExcDataVo.getSupplierCode(),
                            scmQcExcDataVo.getReceiveOrderNo(), feiShuOpenId));
                }

                if (StringUtils.isNotBlank(msg.toString())) {
                    BfExcDataBo bfExcDataBo = new BfExcDataBo();
                    bfExcDataBo.setImgCode(this.randomGetStr(bfMsgConfig.getBadImgList()));
                    bfExcDataBo.setTitle(title);
                    bfExcDataBo.setContent(msg.toString());
                    this.pushFeiShuMsg(bfExcDataBo);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void bfReceiveExcDataJob() {
        LocalDate localDate = LocalDate.now();
        final List<ScmReceiveExcDataVo> scmReceiveExcDataVoList = sdaRemoteService.getBfReceiveExcData(localDate);
        String title = "发货时效异常推送";

        if (CollectionUtil.isEmpty(scmReceiveExcDataVoList)) {
            BfExcDataBo bfExcDataBo = new BfExcDataBo();
            bfExcDataBo.setImgCode(this.randomGetStr(bfMsgConfig.getGoodImgList()));
            bfExcDataBo.setTitle(title);
            bfExcDataBo.setContent(this.randomGetStr(bfMsgConfig.getReceiveGoodList()));
            this.pushFeiShuMsg(bfExcDataBo);
        } else {
            // 获取跟单飞书openId
            final List<String> confirmUserList = scmReceiveExcDataVoList.stream()
                    .map(ScmReceiveExcDataVo::getConfirmUser)
                    .distinct()
                    .collect(Collectors.toList());
            final List<UserVo> userVoList = udbRemoteService.getListByUserCodeList(confirmUserList);
            final Map<String, String> userCodeOpenIdMap = userVoList.stream()
                    .collect(Collectors.toMap(UserVo::getUserCode, UserVo::getFeiShuOpenId));

            // 按照跟单人排序
            final List<ScmReceiveExcDataVo> sortedList = scmReceiveExcDataVoList.stream()
                    .sorted(Comparator.comparing(ScmReceiveExcDataVo::getConfirmUser))
                    .collect(Collectors.toList());

            for (int i = 0; i < sortedList.size(); i += bfMsgConfig.getBatchSize()) {
                int endIndex = Math.min(i + bfMsgConfig.getBatchSize(), sortedList.size());
                List<ScmReceiveExcDataVo> batchList = sortedList.subList(i, endIndex);

                StringBuilder msg = new StringBuilder();
                for (ScmReceiveExcDataVo scmReceiveExcDataVo : batchList) {
                    final String feiShuOpenId = userCodeOpenIdMap.get(scmReceiveExcDataVo.getConfirmUser());
                    if (StringUtils.isBlank(feiShuOpenId)) {
                        log.error("用户:{}飞书id为空", scmReceiveExcDataVo.getConfirmUsername());
                        continue;
                    }

                    final String randomBadMsg = this.randomGetStr(bfMsgConfig.getReceiveBadList());
                    msg.append(String.format(randomBadMsg, scmReceiveExcDataVo.getSupplierCode(),
                            scmReceiveExcDataVo.getReceiveOrderNo(), feiShuOpenId));
                }

                if (StringUtils.isNotBlank(msg.toString())) {
                    BfExcDataBo bfExcDataBo = new BfExcDataBo();
                    bfExcDataBo.setImgCode(this.randomGetStr(bfMsgConfig.getBadImgList()));
                    bfExcDataBo.setTitle(title);
                    bfExcDataBo.setContent(msg.toString());
                    this.pushFeiShuMsg(bfExcDataBo);
                }
            }
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public void bfPurchaseExcDataJob() {

        LocalDate localDate = LocalDate.now();
        final List<ScmPurchaseExcDataVo> scmPurchaseExcDataVoList = sdaRemoteService.getBfPurchaseExcData(localDate);
        String title = "准交率异常推送";

        if (CollectionUtil.isEmpty(scmPurchaseExcDataVoList)) {
            BfExcDataBo bfExcDataBo = new BfExcDataBo();
            bfExcDataBo.setImgCode(this.randomGetStr(bfMsgConfig.getGoodImgList()));
            bfExcDataBo.setTitle(title);
            bfExcDataBo.setContent(this.randomGetStr(bfMsgConfig.getPurchaseGoodList()));
            this.pushFeiShuMsg(bfExcDataBo);
        } else {
            // 获取跟单飞书openId
            final List<String> confirmUserList = scmPurchaseExcDataVoList.stream()
                    .map(ScmPurchaseExcDataVo::getConfirmUser)
                    .distinct()
                    .collect(Collectors.toList());
            final List<UserVo> userVoList = udbRemoteService.getListByUserCodeList(confirmUserList);
            final Map<String, String> userCodeOpenIdMap = userVoList.stream()
                    .collect(Collectors.toMap(UserVo::getUserCode, UserVo::getFeiShuOpenId));

            // 按照跟单人排序
            final List<ScmPurchaseExcDataVo> sortedList = scmPurchaseExcDataVoList.stream()
                    .sorted(Comparator.comparing(ScmPurchaseExcDataVo::getConfirmUser))
                    .collect(Collectors.toList());

            for (int i = 0; i < sortedList.size(); i += bfMsgConfig.getBatchSize()) {
                int endIndex = Math.min(i + bfMsgConfig.getBatchSize(), sortedList.size());
                List<ScmPurchaseExcDataVo> batchList = sortedList.subList(i, endIndex);

                StringBuilder msg = new StringBuilder();
                for (ScmPurchaseExcDataVo scmPurchaseExcDataVo : batchList) {
                    final String feiShuOpenId = userCodeOpenIdMap.get(scmPurchaseExcDataVo.getConfirmUser());
                    if (StringUtils.isBlank(feiShuOpenId)) {
                        log.error("用户:{}飞书id为空", scmPurchaseExcDataVo.getConfirmUsername());
                        continue;
                    }

                    final String randomBadMsg = this.randomGetStr(bfMsgConfig.getPurchaseBadList());
                    msg.append(String.format(randomBadMsg, scmPurchaseExcDataVo.getSupplierCode(), feiShuOpenId));
                }

                if (StringUtils.isNotBlank(msg.toString())) {
                    BfExcDataBo bfExcDataBo = new BfExcDataBo();
                    bfExcDataBo.setImgCode(this.randomGetStr(bfMsgConfig.getBadImgList()));
                    bfExcDataBo.setTitle(title);
                    bfExcDataBo.setContent(msg.toString());
                    this.pushFeiShuMsg(bfExcDataBo);
                }
            }
        }


    }

    public String randomGetStr(List<String> strList) {
        Random random = new Random();
        int randomIndex = random.nextInt(strList.size());
        return strList.get(randomIndex);
    }
}
