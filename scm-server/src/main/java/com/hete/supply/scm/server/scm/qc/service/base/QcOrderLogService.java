package com.hete.supply.scm.server.scm.qc.service.base;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author yanjiawei
 * Created on 2024/4/29.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QcOrderLogService {

    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;

    /**
     * 日志
     *
     * @param qcOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/27 11:16
     */
    public void createStatusChangeLog(QcOrderPo qcOrderPo) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.QC_ORDER_STATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.QC_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.QC_ORDER_STATUS.name());
        bizLogCreateMqDto.setBizCode(qcOrderPo.getQcOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        String userKey = StringUtils.isNotBlank(
                GlobalContext.getUserKey()) ? GlobalContext.getUserKey() : ScmConstant.SYSTEM_USER;
        String username = StringUtils.isNotBlank(
                GlobalContext.getUsername()) ? GlobalContext.getUsername() : ScmConstant.MQ_DEFAULT_USER;
        bizLogCreateMqDto.setOperateUser(userKey);
        bizLogCreateMqDto.setOperateUsername(username);

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();

        bizLogCreateMqDto.setContent(qcOrderPo.getQcState()
                .getRemark());

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }
}
