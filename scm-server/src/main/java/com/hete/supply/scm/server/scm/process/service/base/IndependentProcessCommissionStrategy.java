package com.hete.supply.scm.server.scm.process.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderScanDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderScanRelateDao;
import com.hete.supply.scm.server.scm.process.dao.ScanCommissionDetailDao;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessWithCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.process.entity.po.ScanCommissionDetailPo;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

/**
 * 独立工序提成详情计算策略实现类。
 *
 * @author yanjiawei
 * Created on 2024/2/26.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndependentProcessCommissionStrategy implements CommissionDetailsCalculationStrategy {

    private final ProcessOrderScanDao processOrderScanDao;
    private final ProcessBaseService processBaseService;
    private final ScanCommissionDetailDao scanCommissionDetailDao;
    private final ProcessOrderScanBaseService processOrderScanBaseService;
    private final ProcessOrderScanRelateDao processOrderScanRelateDao;

    @Override
    @RedisLock(key = "#processOrderScanId", prefix = ScmRedisConstant.REFRESH_COMMISSION, waitTime = 1, leaseTime = -1)
    public void createCommissionDetails(Long processOrderScanId) {
        // 根据提供的ID检索 ProcessOrderScanPo
        ProcessOrderScanPo processOrderScanPo = processOrderScanDao.getById(processOrderScanId);

        // 如果 processOrderScanPo 为 null，则抛出异常
        if (Objects.isNull(processOrderScanPo)) {
            throw new ParamIllegalException("工序扫码信息不存在，请刷新页面");
        }

        // 从 ProcessOrderScanPo 中提取相关信息
        String processCode = processOrderScanPo.getProcessCode();
        String processName = processOrderScanPo.getProcessName();
        BigDecimal baseCommission = processOrderScanPo.getProcessCommission();
        BigDecimal extraCommission = processOrderScanPo.getExtraCommission();

        // 检索给定工序编码的提成规则
        List<ProcessWithCommissionRuleBo> processWithCommissionRules
                = processBaseService.getProcessWithCommissionRule(Collections.singletonList(processCode));

        ProcessWithCommissionRuleBo processWithCommissionRuleBo = processWithCommissionRules.stream()
                .filter(processWithCommissionRule -> Objects.equals(processCode,
                        processWithCommissionRule.getProcessCode()))
                .findFirst()
                .orElse(null);
        // 如果提成规则为 null 或为空，则记录警告
        if (Objects.isNull(processWithCommissionRuleBo) || CollectionUtils.isEmpty(
                processWithCommissionRuleBo.getRules())) {
            throw new BizException("操作失败，无{}工序规则信息，请联系相关业务人员配置对应规则信息后重试！", processName);
        }

        // 检查是否存在给定工序扫码ID的现有提成详情，如果存在则记录警告
        List<ScanCommissionDetailPo> existCommissionDetails
                = scanCommissionDetailDao.listByProcessOrderScanId(processOrderScanId);
        if (CollectionUtils.isNotEmpty(existCommissionDetails)) {
            throw new BizException("操作失败，{}工序阶梯提成信息已存在！请联系相关业务人员！", processName);
        }

        // 计算时间范围并累积正品数
        LocalDateTime completeTimeEnd = processOrderScanPo.getCompleteTime();
        LocalDateTime completeCnTimeBegin = completeTimeEnd.withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        LocalDateTime completeTimeBegin = TimeUtil.convertZone(completeCnTimeBegin, TimeZoneId.CN, TimeZoneId.UTC);

        Integer currentQualityGoodsCnt = processOrderScanPo.getQualityGoodsCnt();
        String completeUser = processOrderScanPo.getCompleteUser();
        String completeUsername = processOrderScanPo.getCompleteUsername();

        int currentTotalQualityGoodsCnt = getCurProTotalCompleteQuality(processCode,
                completeUser,
                completeTimeBegin, completeTimeEnd);
        int beforeTotalQualityGoodsCnt = currentTotalQualityGoodsCnt - currentQualityGoodsCnt;

        // 获取提成规则并创建提成详情列表
        TreeSet<ProcessCommissionRuleBo> rules = processWithCommissionRuleBo.getRules();
        List<ScanCommissionDetailPo> scanCommissionDetailPos = processOrderScanBaseService.createScanCommissionDetails(
                processOrderScanId,
                processCode,
                baseCommission,
                extraCommission,
                beforeTotalQualityGoodsCnt,
                currentTotalQualityGoodsCnt,
                completeUsername, rules);

        // 批量插入提成详情
        scanCommissionDetailDao.insertBatch(scanCommissionDetailPos);
    }

    /**
     * @Description 获取当前工序总完成正品数
     * @author yanjiawei
     * @Date 2024/2/28 10:03
     */
    private int getCurProTotalCompleteQuality(String processCode,
                                              String completeUser,
                                              LocalDateTime completeTimeBegin,
                                              LocalDateTime completeTimeEnd) {
        int currentTotalQualityGoodsCnt = 0;

        // 当前工序累计完成数量
        int curProcessGoodsCnt = processOrderScanDao.sumQualityGoodsCnt(processCode,
                completeUser,
                completeTimeBegin,
                completeTimeEnd);
        currentTotalQualityGoodsCnt += curProcessGoodsCnt;

        // 关联组合工序累计完成数量
        int relateGoodsCnt = processOrderScanRelateDao.calculateQualityGoodsCountTotal(processCode,
                null,
                completeUser,
                completeTimeBegin,
                completeTimeEnd);
        currentTotalQualityGoodsCnt += relateGoodsCnt;

        return currentTotalQualityGoodsCnt;
    }

    /**
     * 获取工序类型。
     *
     * @return 独立工序类型
     */
    @Override
    public ProcessType getProcessType() {
        return ProcessType.INDEPENDENT_PROCESS;
    }
}

