package com.hete.supply.scm.server.scm.process.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.process.dao.ProcessDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderScanDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderScanRelateDao;
import com.hete.supply.scm.server.scm.process.dao.ScanCommissionDetailDao;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessWithCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanRelatePo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.po.ScanCommissionDetailPo;
import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.enums.CommissionCategory;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 复合工序提成详情计算策略实现类。
 *
 * @author yanjiawei
 * Created on 2024/2/26.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompoundProcessCommissionStrategy implements CommissionDetailsCalculationStrategy {

    private final ProcessOrderScanDao processOrderScanDao;
    private final ProcessBaseService processBaseService;
    private final ScanCommissionDetailDao scanCommissionDetailDao;
    private final ProcessOrderScanBaseService processOrderScanBaseService;
    private final ProcessOrderScanRelateDao processOrderScanRelateDao;
    private final ProcessDao processDao;

    @Override
    @RedisLock(key = "#processOrderScanId", prefix = ScmRedisConstant.REFRESH_COMMISSION, waitTime = 1, leaseTime = -1)
    public void createCommissionDetails(Long processOrderScanId) {
        ProcessOrderScanPo processOrderScanPo = processOrderScanDao.getById(processOrderScanId);
        if (Objects.isNull(processOrderScanPo)) {
            throw new ParamIllegalException("工序扫码信息不存在，请刷新页面");
        }

        List<ScanCommissionDetailPo> existCommissionDetails
                = scanCommissionDetailDao.listByProcessOrderScanId(processOrderScanId);
        if (CollectionUtils.isNotEmpty(existCommissionDetails)) {
            throw new BizException("操作失败，{}工序阶梯提成信息已存在！请联系相关业务人员！",
                    processOrderScanPo.getProcessName());
        }

        String processCode = processOrderScanPo.getProcessCode();

        // 获取关联非组合关系扫码记录
        List<ProcessOrderScanRelatePo> processOrderScanRelatePos
                = processOrderScanRelateDao.listByProcessOrderScanId(processOrderScanId);
        if (CollectionUtils.isEmpty(processOrderScanRelatePos)) {
            return;
        }

        List<String> indProcCodes = processOrderScanRelatePos.stream()
                .map(ProcessOrderScanRelatePo::getProcessCode)
                .collect(Collectors.toList());
        List<ProcessPo> processPos = processDao.getByProcessCodes(indProcCodes);
        List<ProcessWithCommissionRuleBo> processWithCommissionRules
                = processBaseService.getProcessWithCommissionRule(indProcCodes);

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

        List<ScanCommissionDetailPo> allScanCommissionDetailPos = Lists.newArrayList();
        Map<String, Integer> processCodeCompleteQualityCache = new HashMap<>(16);

        for (ProcessOrderScanRelatePo processOrderScanRelatePo : processOrderScanRelatePos) {
            String indProcCode = processOrderScanRelatePo.getProcessCode();
            String processName = processPos.stream()
                    .filter(processPo -> Objects.equals(indProcCode, processPo.getProcessCode()))
                    .map(ProcessPo::getProcessName)
                    .findFirst()
                    .orElse("");
            BigDecimal baseCommission = processOrderScanRelatePo.getProcessCommission();
            BigDecimal extraCommission = processOrderScanRelatePo.getExtraCommission();

            int beforeTotalQualityGoodsCnt = getCurProTotalCompleteQuality(indProcCode,
                    processOrderScanId,
                    completeUser, completeTimeBegin,
                    completeTimeEnd,
                    processCodeCompleteQualityCache);
            int currentTotalQualityGoodsCnt = beforeTotalQualityGoodsCnt + currentQualityGoodsCnt;

            // 如果提成规则为 null 或为空，则记录警告
            TreeSet<ProcessCommissionRuleBo> matchRules = processWithCommissionRules.stream()
                    .filter(processWithCommissionRule -> Objects.equals(indProcCode,
                            processWithCommissionRule.getProcessCode()))
                    .map(ProcessWithCommissionRuleBo::getRules)
                    .findFirst()
                    .orElse(null);
            if (CollectionUtils.isEmpty(matchRules)) {
                throw new BizException(
                        "操作失败，计算组合工序发现无{}工序规则信息，请联系相关业务人员配置对应规则信息后重试！",
                        processName);
            }

            // 获取提成规则并创建提成详情列表
            List<ScanCommissionDetailPo> scanCommissionDetailPos
                    = processOrderScanBaseService.createScanCommissionDetails(processOrderScanId, processCode,
                    baseCommission, extraCommission,
                    beforeTotalQualityGoodsCnt,
                    currentTotalQualityGoodsCnt,
                    completeUsername, matchRules);
            allScanCommissionDetailPos.addAll(scanCommissionDetailPos);

            // 更新缓存累计完成数量
            processCodeCompleteQualityCache.put(indProcCode,
                    processCodeCompleteQualityCache.getOrDefault(indProcCode, 0) +
                            currentQualityGoodsCnt);
        }

        // 批量插入提成详情
        List<ScanCommissionDetailPo> scanCommissionDetailPos = Lists.newArrayList();
        Map<CommissionAttribute, List<ScanCommissionDetailPo>> groupByCommissionAttribute
                = allScanCommissionDetailPos.stream()
                .collect(Collectors.groupingBy(ScanCommissionDetailPo::getCommissionAttribute));
        for (Map.Entry<CommissionAttribute, List<ScanCommissionDetailPo>> entry :
                groupByCommissionAttribute.entrySet()) {
            CommissionAttribute commissionAttribute = entry.getKey();

            List<ScanCommissionDetailPo> detailList = entry.getValue();
            BigDecimal totalAmount = detailList.stream()
                    .map(ScanCommissionDetailPo::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            ScanCommissionDetailPo scanCommissionDetailPo = new ScanCommissionDetailPo();
            scanCommissionDetailPo.setProcessOrderScanId(processOrderScanId);
            scanCommissionDetailPo.setCommissionCategory(CommissionCategory.STAIR);
            scanCommissionDetailPo.setCommissionAttribute(commissionAttribute);
            scanCommissionDetailPo.setTotalAmount(totalAmount);
            scanCommissionDetailPos.add(scanCommissionDetailPo);
        }

        scanCommissionDetailDao.insertBatch(scanCommissionDetailPos);
    }

    /**
     * @Description 获取当前工序总完成正品数
     * @author yanjiawei
     * @Date 2024/2/28 10:03
     */
    private int getCurProTotalCompleteQuality(String independentProcessCode,
                                              Long processOrderScanId,
                                              String completeUser,
                                              LocalDateTime completeTimeBegin,
                                              LocalDateTime completeTimeEnd,
                                              Map<String, Integer> processCodeCompleteQualityCache) {
        int currentTotalQualityGoodsCnt = 0;

        // 获取当前非组合工序扫码累计完成数量
        int curProcessGoodsCnt = processOrderScanDao.sumQualityGoodsCnt(independentProcessCode,
                completeUser,
                completeTimeBegin,
                completeTimeEnd);
        currentTotalQualityGoodsCnt += curProcessGoodsCnt;

        // 关联工序累计完成数量
        int relateGoodsCnt = processOrderScanRelateDao.calculateQualityGoodsCountTotal(independentProcessCode,
                processOrderScanId,
                completeUser,
                completeTimeBegin,
                completeTimeEnd);
        currentTotalQualityGoodsCnt += relateGoodsCnt;

        // 累计数量本地缓存
        currentTotalQualityGoodsCnt += processCodeCompleteQualityCache.getOrDefault(independentProcessCode, 0);
        return currentTotalQualityGoodsCnt;
    }

    @Override
    public ProcessType getProcessType() {
        return ProcessType.COMPOUND_PROCESS;
    }
}
