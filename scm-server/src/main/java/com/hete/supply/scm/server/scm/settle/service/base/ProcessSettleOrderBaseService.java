package com.hete.supply.scm.server.scm.settle.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.hete.supply.scm.api.scm.entity.enums.DeductType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessSettleStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.entity.bo.CommissionAmountBo;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.process.converter.CommissionDetailConverter;
import com.hete.supply.scm.server.scm.process.dao.ProcessDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessSettleDetailReportDao;
import com.hete.supply.scm.server.scm.process.dao.ScanCommissionDetailDao;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessSettleDetailReportPo;
import com.hete.supply.scm.server.scm.process.entity.po.ScanCommissionDetailPo;
import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.enums.CommissionCategory;
import com.hete.supply.scm.server.scm.process.service.base.ProcessBaseService;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderScanBaseService;
import com.hete.supply.scm.server.scm.settle.dao.*;
import com.hete.supply.scm.server.scm.settle.entity.bo.PatrolProcessSettleOrderBo;
import com.hete.supply.scm.server.scm.settle.entity.bo.SettleCompleteUserBo;
import com.hete.supply.scm.api.scm.entity.dto.GetProcessSettleOrderDetailAndScanSettleDto;
import com.hete.supply.scm.server.scm.settle.entity.po.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderDetailAndScanSettleVo;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleExamine;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderType;
import com.hete.supply.scm.server.scm.settle.enums.SupplementDeductType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购结算单基础类
 *
 * @author ChenWenLong
 * @date 2022/11/15 10:08
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class ProcessSettleOrderBaseService {
    private final ProcessSettleOrderDao processSettleOrderDao;
    private final ProcessOrderScanBaseService processOrderScanBaseService;
    private final ProcessSettleOrderItemDao processSettleOrderItemDao;
    private final ProcessSettleOrderBillDao processSettleOrderBillDao;
    private final ProcessSettleOrderScanDao processSettleOrderScanDao;
    private final SupplementOrderBaseService supplementOrderBaseService;
    private final DeductOrderBaseService deductOrderBaseService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final DeductOrderDao deductOrderDao;
    private final SupplementOrderDao supplementOrderDao;
    private final ProcessDao processDao;
    private final ProcessSettleDetailReportDao processSettleDetailReportDao;
    private final ScanCommissionDetailDao scanCommissionDetailDao;

    /**
     * 统计加工结算单
     *
     * @author ChenWenLong
     * @date 2022/11/21 15:28
     */
    @Transactional(rollbackFor = Exception.class)
    public void countProcessSettleOrder(String completeUser,
                                        LocalDateTime completeTime,
                                        String settleTime) {
        //月份
        String month;
        //自定义统计月份
        LocalDateTime warehousingTimeStart;
        LocalDateTime warehousingTimeEnd;
        if (StringUtils.isBlank(settleTime)) {
            // 获取上个月的第一天
            warehousingTimeStart = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
            // 获取上个月的最后一天
            warehousingTimeEnd = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
            //月份
            month = LocalDateTimeUtil.format(TimeUtil.utcConvertZone(TimeUtil.now(), TimeZoneId.CN), ScmConstant.SETTLE_ORDER_MONTH);
        } else {
            warehousingTimeStart = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(settleTime), TimeZoneId.CN, TimeZoneId.UTC);
            warehousingTimeEnd = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(settleTime), TimeZoneId.CN, TimeZoneId.UTC);
            //月份
            month = warehousingTimeEnd.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        List<ProcessSettleOrderPo> processSettleOrderPoList = processSettleOrderDao.getListByMonth(month);
        if (CollectionUtil.isNotEmpty(processSettleOrderPoList)) {
            throw new BizException("加工结算单统计{}月份错误，数据库已经存在", month);
        }

        log.info("统计加工结算的月初时间：{}", warehousingTimeStart);
        log.info("统计加工结算的月末时间：{}", warehousingTimeEnd);

        //要插入的数据加工结算单详情
        List<ProcessSettleOrderItemPo> processSettleOrderItemPos = new ArrayList<>();

        //要插入的数据加工结算单扫码记录
        List<ProcessSettleOrderScanPo> processSettleOrderScanPos = new ArrayList<>();

        //要插入的数据加工结算单补扣单
        List<ProcessSettleOrderBillPo> processSettleOrderBillPos = new ArrayList<>();

        //已经增加的结算用户
        Map<String, SettleCompleteUserBo> settleCompleteUser = new HashMap<>();


        //生成加工结算单
        String processSettleOrderNo = idGenerateService.getConfuseCode(ScmConstant.PROCESS_SETTLE_ORDER_CODE, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);

        //创建结算单
        ProcessSettleOrderPo processSettleOrderPo = new ProcessSettleOrderPo();
        processSettleOrderPo.setProcessSettleOrderNo(processSettleOrderNo);
        processSettleOrderPo.setProcessSettleStatus(ProcessSettleStatus.WAIT_SETTLE);
        processSettleOrderPo.setMonth(month);
        processSettleOrderDao.insert(processSettleOrderPo);
        Long processSettleOrderId = processSettleOrderPo.getProcessSettleOrderId();

        //统计：加工扫码记录
        //查询扫码记录的数据
        List<ProcessOrderScanPo> processOrderScanPos = processOrderScanBaseService.getByCompleteUserAndTime(completeUser, completeTime, warehousingTimeStart, warehousingTimeEnd);
        Map<String, List<ProcessOrderScanPo>> processOrderScanGroup = processOrderScanPos.stream().collect(Collectors.groupingBy(ProcessOrderScanPo::getCompleteUser));
        //创建
        processOrderScanGroup.forEach((String key, List<ProcessOrderScanPo> itemPos) -> {

            // 采购雪花id
            long processSettleOrderItemId = idGenerateService.getSnowflakeId();
            //创建单个用户详情
            ProcessSettleOrderItemPo processSettleOrderItemPo = new ProcessSettleOrderItemPo();
            processSettleOrderItemPo.setProcessSettleOrderId(processSettleOrderId);
            processSettleOrderItemPo.setProcessSettleOrderNo(processSettleOrderNo);
            processSettleOrderItemPo.setCompleteUser(key);
            String completeUsername = itemPos.get(0).getCompleteUsername();
            processSettleOrderItemPo.setCompleteUsername(completeUsername);

            HashSet<String> processOrderNoList = itemPos.stream().map(ProcessOrderScanPo::getProcessOrderNo).collect(Collectors.toCollection(HashSet::new));

            processSettleOrderItemPo.setProcessNum(processOrderNoList.size());
            //正品数量quality_goods_cnt
            int qualityGoodsCnt = itemPos.stream().mapToInt(ProcessOrderScanPo::getQualityGoodsCnt).sum();
            processSettleOrderItemPo.setSkuNum(qualityGoodsCnt);
            processSettleOrderItemPo.setProcessSettleOrderItemId(processSettleOrderItemId);

            //增加的结算用户
            SettleCompleteUserBo settleCompleteUserBo = new SettleCompleteUserBo();
            settleCompleteUserBo.setProcessSettleOrderItemId(processSettleOrderItemId);
            settleCompleteUserBo.setCompleteUser(key);
            settleCompleteUserBo.setCompleteUserName(completeUsername);
            settleCompleteUser.put(key, settleCompleteUserBo);

            BigDecimal settlePrice = new BigDecimal(0);

            List<Long> processOrderScanIds = itemPos.stream()
                    .map(ProcessOrderScanPo::getProcessOrderScanId)
                    .collect(Collectors.toList());
            List<CommissionBo> commissions = processOrderScanBaseService.getCommissions(processOrderScanIds);

            //记录每一个扫码记录
            for (ProcessOrderScanPo itemPo : itemPos) {
                Long processOrderScanId = itemPo.getProcessOrderScanId();

                ProcessSettleOrderScanPo processSettleOrderScanPo = new ProcessSettleOrderScanPo();
                processSettleOrderScanPo.setProcessSettleOrderItemId(processSettleOrderItemId);
                processSettleOrderScanPo.setProcessOrderScanId(processOrderScanId);
                processSettleOrderScanPo.setTotalProcessCommission(itemPo.getProcessCommission());
                processSettleOrderScanPo.setCompleteTime(itemPo.getCompleteTime());
                processSettleOrderScanPo.setProcessOrderNo(itemPo.getProcessOrderNo());
                processSettleOrderScanPo.setProcessCode(itemPo.getProcessCode());
                processSettleOrderScanPo.setProcessName(itemPo.getProcessName());
                processSettleOrderScanPo.setProcessCommission(itemPo.getProcessCommission());
                processSettleOrderScanPo.setQualityGoodsCnt(itemPo.getQualityGoodsCnt());

                CommissionBo matchScanCommissionDetail = commissions.stream()
                        .filter(commissionBo ->
                                Objects.equals(processOrderScanId, commissionBo.getProcessOrderScanId()))
                        .findFirst()
                        .orElse(null);
                if (Objects.nonNull(matchScanCommissionDetail)) {
                    BigDecimal totalProcessCommission = matchScanCommissionDetail.getTotalCommission();
                    //累加结算金额
                    settlePrice = settlePrice.add(totalProcessCommission);
                    processSettleOrderScanPo.setTotalProcessCommission(totalProcessCommission);
                }

                processSettleOrderScanPo.setOrderUser(itemPo.getOrderUser());
                processSettleOrderScanPo.setOrderUsername(itemPo.getOrderUsername());
                processSettleOrderScanPo.setCompleteUser(itemPo.getCompleteUser());
                processSettleOrderScanPo.setCompleteUsername(itemPo.getCompleteUsername());
                processSettleOrderScanPos.add(processSettleOrderScanPo);
            }
            processSettleOrderItemPo.setSettlePrice(settlePrice);
            processSettleOrderItemPos.add(processSettleOrderItemPo);

        });


        //统计：补款单
        List<SupplementOrderPo> supplementOrderPos = supplementOrderBaseService.getBySupplementUserAndExamineTime(completeUser, completeTime, warehousingTimeStart, warehousingTimeEnd);
        Map<String, List<SupplementOrderPo>> supplementOrderPoGroup = supplementOrderPos.stream().collect(Collectors.groupingBy(SupplementOrderPo::getSupplementUser));
        //创建
        supplementOrderPoGroup.forEach((String key, List<SupplementOrderPo> itemPos) -> {
            Long processSettleOrderItemId;
            //结算金额
            BigDecimal totalProcessCommission = itemPos.stream().map(SupplementOrderPo::getSupplementPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (settleCompleteUser.get(key) == null) {
                // 采购雪花id
                processSettleOrderItemId = idGenerateService.getSnowflakeId();
                //创建单个用户详情
                ProcessSettleOrderItemPo newProcessSettleOrderItemPo = new ProcessSettleOrderItemPo();
                newProcessSettleOrderItemPo.setProcessSettleOrderId(processSettleOrderId);
                newProcessSettleOrderItemPo.setProcessSettleOrderNo(processSettleOrderNo);
                newProcessSettleOrderItemPo.setCompleteUser(key);
                String completeUsername = itemPos.get(0).getSupplementUsername();
                newProcessSettleOrderItemPo.setCompleteUsername(completeUsername);

                newProcessSettleOrderItemPo.setSettlePrice(totalProcessCommission);
                newProcessSettleOrderItemPo.setProcessSettleOrderItemId(processSettleOrderItemId);
                processSettleOrderItemPos.add(newProcessSettleOrderItemPo);

                //增加的结算用户
                SettleCompleteUserBo settleCompleteUserBo = new SettleCompleteUserBo();
                settleCompleteUserBo.setProcessSettleOrderItemId(processSettleOrderItemId);
                settleCompleteUserBo.setCompleteUser(key);
                settleCompleteUserBo.setCompleteUserName(completeUsername);
                settleCompleteUser.put(key, settleCompleteUserBo);
            } else {
                processSettleOrderItemId = settleCompleteUser.get(key).getProcessSettleOrderItemId();
                //追加结算金额
                for (int i = 0; i < processSettleOrderItemPos.size(); i++) {
                    if (processSettleOrderItemPos.get(i).getCompleteUser().equals(settleCompleteUser.get(key).getCompleteUser())) {
                        ProcessSettleOrderItemPo newProcessSettleOrderItemPo = processSettleOrderItemPos.get(i);
                        newProcessSettleOrderItemPo.setSettlePrice((processSettleOrderItemPos.get(i).getSettlePrice()).add(totalProcessCommission));
                        processSettleOrderItemPos.set(i, newProcessSettleOrderItemPo);
                    }
                }
            }

            //创建详情
            for (SupplementOrderPo itemPo : itemPos) {
                ProcessSettleOrderBillPo processSettleOrderBillPo = new ProcessSettleOrderBillPo();
                processSettleOrderBillPo.setProcessSettleOrderItemId(processSettleOrderItemId);
                processSettleOrderBillPo.setProcessSettleOrderBillType(ProcessSettleOrderBillType.REPLENISH);
                processSettleOrderBillPo.setBusinessNo(itemPo.getSupplementOrderNo());
                processSettleOrderBillPo.setSupplementDeductType(SupplementDeductType.getSupplementDeductBySupplement(itemPo.getSupplementType()));
                processSettleOrderBillPo.setExamineTime(itemPo.getExamineTime());
                processSettleOrderBillPo.setPrice(itemPo.getSupplementPrice());
                processSettleOrderBillPo.setStatusName(itemPo.getSupplementStatus().getRemark());
                processSettleOrderBillPos.add(processSettleOrderBillPo);
            }
        });

        //总付款
        BigDecimal totalPrice = processSettleOrderItemPos.stream().map(ProcessSettleOrderItemPo::getSettlePrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //统计：扣款单
        List<DeductOrderPo> deductOrderPos = deductOrderBaseService.getByDeductUserAndExamineTime(completeUser, completeTime, warehousingTimeStart, warehousingTimeEnd);
        //总扣款结算金额
        BigDecimal totalDeductPrice = deductOrderPos.stream().map(DeductOrderPo::getDeductPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, List<DeductOrderPo>> deductPoGroup = deductOrderPos.stream().collect(Collectors.groupingBy(DeductOrderPo::getDeductUser));
        //创建
        deductPoGroup.forEach((String key, List<DeductOrderPo> itemPos) -> {
            Long processSettleOrderItemId;
            //结算金额
            BigDecimal totalProcessCommission = itemPos.stream().map(DeductOrderPo::getDeductPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (settleCompleteUser.get(key) == null) {
                // 采购雪花id
                processSettleOrderItemId = idGenerateService.getSnowflakeId();
                //创建单个用户详情
                ProcessSettleOrderItemPo newProcessSettleOrderItemPo = new ProcessSettleOrderItemPo();
                newProcessSettleOrderItemPo.setProcessSettleOrderId(processSettleOrderId);
                newProcessSettleOrderItemPo.setProcessSettleOrderNo(processSettleOrderNo);
                newProcessSettleOrderItemPo.setCompleteUser(key);
                String completeUsername = itemPos.get(0).getDeductUsername();
                newProcessSettleOrderItemPo.setCompleteUsername(completeUsername);
                BigDecimal start = new BigDecimal(0);
                newProcessSettleOrderItemPo.setSettlePrice(start.subtract(totalProcessCommission));
                newProcessSettleOrderItemPo.setProcessSettleOrderItemId(processSettleOrderItemId);
                processSettleOrderItemPos.add(newProcessSettleOrderItemPo);

            } else {
                processSettleOrderItemId = settleCompleteUser.get(key).getProcessSettleOrderItemId();

                //追加结算金额
                for (int i = 0; i < processSettleOrderItemPos.size(); i++) {
                    if (processSettleOrderItemPos.get(i).getCompleteUser().equals(settleCompleteUser.get(key).getCompleteUser())) {
                        ProcessSettleOrderItemPo newProcessSettleOrderItemPo = processSettleOrderItemPos.get(i);
                        newProcessSettleOrderItemPo.setSettlePrice((processSettleOrderItemPos.get(i).getSettlePrice()).subtract(totalProcessCommission));
                        processSettleOrderItemPos.set(i, newProcessSettleOrderItemPo);
                    }
                }

            }
            //创建详情
            for (DeductOrderPo itemPo : itemPos) {
                ProcessSettleOrderBillPo processSettleOrderBillPo = new ProcessSettleOrderBillPo();
                processSettleOrderBillPo.setProcessSettleOrderItemId(processSettleOrderItemId);
                processSettleOrderBillPo.setProcessSettleOrderBillType(ProcessSettleOrderBillType.DEDUCT);
                processSettleOrderBillPo.setBusinessNo(itemPo.getDeductOrderNo());
                processSettleOrderBillPo.setSupplementDeductType(SupplementDeductType.getSupplementDeductByDeduct(itemPo.getDeductType()));
                processSettleOrderBillPo.setExamineTime(itemPo.getExamineTime());
                processSettleOrderBillPo.setPrice(itemPo.getDeductPrice());
                processSettleOrderBillPo.setStatusName(itemPo.getDeductStatus().getRemark());
                processSettleOrderBillPos.add(processSettleOrderBillPo);
            }

        });

        //总款
        //BigDecimal total = processSettleOrderItemPos.stream().map(ProcessSettleOrderItemPo::getSettlePrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //总扣款

        //应付款
        BigDecimal payPrice = totalPrice.subtract(totalDeductPrice);

        //更新统计金额
        ProcessSettleOrderPo updateProcessSettleOrder = new ProcessSettleOrderPo();
        updateProcessSettleOrder.setProcessSettleOrderId(processSettleOrderId);
        updateProcessSettleOrder.setTotalPrice(totalPrice);
        updateProcessSettleOrder.setDeductPrice(totalDeductPrice);
        updateProcessSettleOrder.setPayPrice(payPrice);
        processSettleOrderDao.updateById(updateProcessSettleOrder);

        processSettleOrderItemDao.insertBatch(processSettleOrderItemPos);
        processSettleOrderScanDao.insertBatch(processSettleOrderScanPos);
        processSettleOrderBillDao.insertBatch(processSettleOrderBillPos);

        //创建日志记录
        ProcessSettleOrderPo processSettleOrderPoLog = processSettleOrderDao.getById(processSettleOrderId);
        this.createStatusChangeLog(processSettleOrderPoLog, null);
        for (ProcessSettleOrderItemPo processSettleOrderItemPo : processSettleOrderItemPos) {
            Long processSettleOrderItemId = processSettleOrderItemPo.getProcessSettleOrderItemId();
            doCalculateSettleItemReport(processSettleOrderItemId);
        }
    }

    /**
     * 日志
     *
     * @param processSettleOrderPo
     */
    public void createStatusChangeLog(ProcessSettleOrderPo processSettleOrderPo, ProcessSettleExamine processSettleExamine) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.PROCESS_SETTLE_STATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.PROCESS_SETTLE_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.PROCESS_SETTLE_STATUS.name());
        bizLogCreateMqDto.setBizCode(processSettleOrderPo.getProcessSettleOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());

        String userKey = StringUtils.isNotBlank(GlobalContext.getUserKey()) ? GlobalContext.getUserKey() : ScmConstant.SYSTEM_USER;
        String username = StringUtils.isNotBlank(GlobalContext.getUsername()) ? GlobalContext.getUsername() : ScmConstant.MQ_DEFAULT_USER;
        bizLogCreateMqDto.setOperateUser(userKey);
        bizLogCreateMqDto.setOperateUsername(username);

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();

        bizLogCreateMqDto.setContent(processSettleOrderPo.getProcessSettleStatus().getName());

        if (ProcessSettleExamine.EXAMINE_REFUSE.equals(processSettleExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(ProcessSettleExamine.EXAMINE_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(processSettleOrderPo.getExamineRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    /**
     * 巡检结算单数据
     *
     * @param settleTime: 格式yyyy-MM
     * @return void
     * @author ChenWenLong
     * @date 2023/6/7 14:48
     */
    public void patrolSettleOrder(String settleTime) {
        LocalDateTime settleTimeStart;
        LocalDateTime settleTimeEnd;
        if (StringUtils.isBlank(settleTime)) {
            // 获取上个月的第一天
            settleTimeStart = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
            // 获取上个月的最后一天
            settleTimeEnd = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
        } else {
            settleTimeStart = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(settleTime), TimeZoneId.CN, TimeZoneId.UTC);
            settleTimeEnd = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(settleTime), TimeZoneId.CN, TimeZoneId.UTC);
        }

        //月份
        String month = settleTimeEnd.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String settleMonth = settleTimeEnd.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        //获取月份结算单
        ProcessSettleOrderPo processSettleOrderPo = processSettleOrderDao.getByMonth(month);
        if (null == processSettleOrderPo) {
            log.info("巡检加工结算单数据{}月份时未发现生成结算单", settleMonth);
            return;
        }

        List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderNo(processSettleOrderPo.getProcessSettleOrderNo());
        if (CollectionUtils.isEmpty(processSettleOrderItemPoList)) {
            log.info("巡检加工结算单数据{}月份时未发现生成结算单", settleMonth);
            return;
        }
        Map<String, List<ProcessSettleOrderItemPo>> processSettleOrderItemPoMap = processSettleOrderItemPoList.stream().collect(Collectors.groupingBy(ProcessSettleOrderItemPo::getCompleteUser));
        List<Long> processSettleOrderItemList = processSettleOrderItemPoList.stream()
                .map(ProcessSettleOrderItemPo::getProcessSettleOrderItemId)
                .collect(Collectors.toList());

        List<String> completeUserList = processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getCompleteUser).collect(Collectors.toList());

        //查询扫码记录的数据
        List<ProcessOrderScanPo> processOrderScanPoList = processOrderScanBaseService.getByCompleteUserAndTime(
                null,
                null,
                settleTimeStart,
                settleTimeEnd);
        Map<String, List<ProcessOrderScanPo>> processOrderScanPoMap = processOrderScanPoList.stream().collect(Collectors.groupingBy(ProcessOrderScanPo::getCompleteUser));

        //查询结算的扫码记录
        Map<String, List<ProcessSettleOrderScanPo>> processSettleOrderScanMap = processSettleOrderScanDao.getMapByProcessSettleOrderItemId(processSettleOrderItemList);

        //查询补款单
        List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getByExamineTimeAndSettleOrderNo(
                settleTimeStart,
                settleTimeEnd,
                List.of(processSettleOrderPo.getProcessSettleOrderNo()),
                List.of(SupplementType.PROCESS, SupplementType.OTHER),
                Collections.emptyList(),
                completeUserList);
        Map<String, List<SupplementOrderPo>> supplementOrderPoMap = supplementOrderPoList.stream().collect(Collectors.groupingBy(SupplementOrderPo::getSupplementUser));

        Map<Long, List<ProcessSettleOrderBillPo>> supplementBillPoMap = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(processSettleOrderItemList, ProcessSettleOrderBillType.REPLENISH)
                .stream().collect(Collectors.groupingBy(ProcessSettleOrderBillPo::getProcessSettleOrderItemId));

        //查询扣款单
        List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByExamineTimeAndSettleOrderNo(
                settleTimeStart,
                settleTimeEnd,
                List.of(processSettleOrderPo.getProcessSettleOrderNo()),
                List.of(DeductType.PROCESS),
                Collections.emptyList(),
                completeUserList);

        Map<String, List<DeductOrderPo>> deductOrderPoMap = deductOrderPoList.stream().collect(Collectors.groupingBy(DeductOrderPo::getDeductUser));
        Map<Long, List<ProcessSettleOrderBillPo>> deductBillPoMap = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(processSettleOrderItemList, ProcessSettleOrderBillType.DEDUCT)
                .stream().collect(Collectors.groupingBy(ProcessSettleOrderBillPo::getProcessSettleOrderItemId));

        //结算单数据检验
        List<PatrolProcessSettleOrderBo> patrolProcessSettleOrderBoList = this.verifySettleOrder(
                processSettleOrderScanMap,
                processOrderScanPoMap,
                processSettleOrderItemPoMap,
                supplementBillPoMap,
                supplementOrderPoMap,
                deductBillPoMap,
                deductOrderPoMap
        );

        //推送日志
        if (patrolProcessSettleOrderBoList.stream().anyMatch(w -> CollectionUtils.isNotEmpty(w.getProcessSettleOrderTypeList()))) {
            log.error("巡检加工结算单数据{}月份时发现数据异常。bo={}", settleMonth, JacksonUtil.parse2Str(patrolProcessSettleOrderBoList));
        } else {
            log.info("巡检加工结算单数据{}月份时未发现问题。bo={}", settleMonth, JacksonUtil.parse2Str(patrolProcessSettleOrderBoList));
        }

    }

    /**
     * 结算单数据检验
     *
     * @param processSettleOrderScanMap:
     * @param processOrderScanPoMap:
     * @param processSettleOrderItemPoMap:
     * @param supplementBillPoMap:
     * @param supplementOrderPoMap:
     * @param deductBillPoMap:
     * @param deductOrderPoMap:
     * @return List<PatrolProcessSettleOrderBo>
     * @author ChenWenLong
     * @date 2023/6/8 10:00
     */
    public List<PatrolProcessSettleOrderBo> verifySettleOrder(Map<String, List<ProcessSettleOrderScanPo>> processSettleOrderScanMap,
                                                              Map<String, List<ProcessOrderScanPo>> processOrderScanPoMap,
                                                              Map<String, List<ProcessSettleOrderItemPo>> processSettleOrderItemPoMap,
                                                              Map<Long, List<ProcessSettleOrderBillPo>> supplementBillPoMap,
                                                              Map<String, List<SupplementOrderPo>> supplementOrderPoMap,
                                                              Map<Long, List<ProcessSettleOrderBillPo>> deductBillPoMap,
                                                              Map<String, List<DeductOrderPo>> deductOrderPoMap) {
        //需要记录日志的BO
        List<PatrolProcessSettleOrderBo> patrolProcessSettleOrderBoList = new ArrayList<>();

        //循环每用户数据对比
        processSettleOrderScanMap.forEach((String completeUser, List<ProcessSettleOrderScanPo> processSettleOrderScanPoList) -> {
            PatrolProcessSettleOrderBo patrolProcessSettleOrderBo = new PatrolProcessSettleOrderBo();
            List<ProcessSettleOrderType> processSettleOrderTypeList = new ArrayList<>();

            // 扫码记录数据检验
            List<ProcessOrderScanPo> processOrderScanPoList = Optional.ofNullable(processOrderScanPoMap.get(completeUser))
                    .orElse(new ArrayList<>());
            boolean processExist = processSettleOrderScanPoList.size() == processOrderScanPoList.size() &&
                    processSettleOrderScanPoList.stream().mapToInt(ProcessSettleOrderScanPo::getQualityGoodsCnt).sum() ==
                            processOrderScanPoList.stream().mapToInt(ProcessOrderScanPo::getQualityGoodsCnt).sum() &&
                    processSettleOrderScanPoList.stream().map(ProcessSettleOrderScanPo::getProcessCommission).reduce(BigDecimal.ZERO, BigDecimal::add)
                            .equals(processOrderScanPoList.stream().map(ProcessOrderScanPo::getProcessCommission).reduce(BigDecimal.ZERO, BigDecimal::add));

            if (!processExist) {
                processSettleOrderTypeList.add(ProcessSettleOrderType.SCAN);
                patrolProcessSettleOrderBo.setProcessOrderScanPoList(processOrderScanPoList.stream().map(
                        po -> {
                            ProcessOrderScanPo processOrderScanPo = new ProcessOrderScanPo();
                            processOrderScanPo.setProcessCode(po.getProcessCode());
                            processOrderScanPo.setQualityGoodsCnt(po.getQualityGoodsCnt());
                            processOrderScanPo.setProcessCommission(po.getProcessCommission());
                            processOrderScanPo.setExtraCommission(po.getExtraCommission());
                            return processOrderScanPo;
                        }
                ).collect(Collectors.toList()));
                patrolProcessSettleOrderBo.setProcessSettleOrderScanPoList(processSettleOrderScanPoList.stream().map(
                        po -> {
                            ProcessSettleOrderScanPo processSettleOrderScanPo = new ProcessSettleOrderScanPo();
                            processSettleOrderScanPo.setProcessCode(po.getProcessCode());
                            processSettleOrderScanPo.setQualityGoodsCnt(po.getQualityGoodsCnt());
                            processSettleOrderScanPo.setProcessCommission(po.getProcessCommission());
                            return processSettleOrderScanPo;
                        }
                ).collect(Collectors.toList()));
            }


            //获取加工结算详情Po
            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = Optional.ofNullable(processSettleOrderItemPoMap.get(completeUser))
                    .orElse(new ArrayList<>());

            ProcessSettleOrderItemPo processSettleOrderItemPo = processSettleOrderItemPoList.stream().findFirst().orElse(null);
            if (null != processSettleOrderItemPo) {
                // 补款单数据检验
                List<ProcessSettleOrderBillPo> supplementBillPoList = Optional.ofNullable(supplementBillPoMap.get(processSettleOrderItemPo.getProcessSettleOrderItemId()))
                        .orElse(new ArrayList<>());
                List<String> supplementNoList = supplementBillPoList
                        .stream()
                        .map(ProcessSettleOrderBillPo::getBusinessNo)
                        .collect(Collectors.toList());

                List<SupplementOrderPo> supplementOrderPoList = Optional.ofNullable(supplementOrderPoMap.get(completeUser))
                        .orElse(new ArrayList<>());

                boolean supplementExist = supplementNoList.size() == supplementOrderPoList.size() &&
                        supplementOrderPoList.stream()
                                .map(SupplementOrderPo::getSupplementOrderNo)
                                .allMatch(supplementNoList::contains);

                if (!supplementExist) {
                    processSettleOrderTypeList.add(ProcessSettleOrderType.REPLENISH);
                    patrolProcessSettleOrderBo.setSupplementOrderPoList(supplementOrderPoList);
                    patrolProcessSettleOrderBo.setSupplementBillPoList(supplementBillPoList);
                }


                //扣款单数据检验
                List<ProcessSettleOrderBillPo> deductBillPoList = Optional.ofNullable(deductBillPoMap.get(processSettleOrderItemPo.getProcessSettleOrderItemId()))
                        .orElse(new ArrayList<>());
                List<DeductOrderPo> deductOrderPoList = Optional.ofNullable(deductOrderPoMap.get(completeUser))
                        .orElse(new ArrayList<>());
                List<String> deductNoList = deductBillPoList.stream()
                        .map(ProcessSettleOrderBillPo::getBusinessNo).collect(Collectors.toList());

                boolean deductExist = deductNoList.size() == deductOrderPoList.size() &&
                        deductOrderPoList.stream()
                                .map(DeductOrderPo::getDeductOrderNo)
                                .allMatch(deductNoList::contains);

                if (!deductExist) {
                    processSettleOrderTypeList.add(ProcessSettleOrderType.DEDUCT);
                    patrolProcessSettleOrderBo.setDeductOrderPoList(deductOrderPoList);
                    patrolProcessSettleOrderBo.setDeductBillPoList(deductBillPoList);
                }


            }

            patrolProcessSettleOrderBo.setProcessSettleOrderTypeList(processSettleOrderTypeList);
            patrolProcessSettleOrderBo.setCompleteUser(completeUser);
            patrolProcessSettleOrderBoList.add(patrolProcessSettleOrderBo);
        });

        return patrolProcessSettleOrderBoList;
    }


    public void doCalculateSettleItemReport(Long processSettleOrderItemId) {
        // 结算单明细
        ProcessSettleOrderItemPo settleOrderItemPo = processSettleOrderItemDao.getById(processSettleOrderItemId);
        if (Objects.isNull(settleOrderItemPo)) {
            log.warn("结算单明细不存在，生成结算单明细报表失败!processSettleOrderItemId:{}", processSettleOrderItemId);
            return;
        }

        // 扫码结算单
        List<ProcessSettleOrderScanPo> settleOrderScanPos
                = processSettleOrderScanDao.getByProcessSettleOrderItemId(processSettleOrderItemId);
        if (CollectionUtils.isEmpty(settleOrderScanPos)) {
            log.warn("扫码结算单明细不存在，生成结算单明细报表失败!processSettleOrderItemId:{}", processSettleOrderItemId);
            return;
        }

        // 工序提成
        List<Long> processSettleOrderScanIds = settleOrderScanPos.stream()
                .map(ProcessSettleOrderScanPo::getProcessOrderScanId)
                .filter(processSettleOrderScanId -> processSettleOrderScanId != 0)
                .collect(Collectors.toList());
        List<ScanCommissionDetailPo> allScanCommissions
                = scanCommissionDetailDao.listByProcessOrderScanIds(processSettleOrderScanIds);

        // 工序信息
        Set<String> processCodes = settleOrderScanPos.stream()
                .map(ProcessSettleOrderScanPo::getProcessCode)
                .collect(Collectors.toSet());
        List<ProcessPo> processPos = processDao.getByProcessCodes(processCodes);

        // 根据工序编码分组，求和正品数
        Map<String, Integer> sumByProcessCode = settleOrderScanPos.stream()
                .collect(Collectors.groupingBy(
                        ProcessSettleOrderScanPo::getProcessCode,
                        Collectors.summingInt(po -> Objects.requireNonNullElse(po.getQualityGoodsCnt(), 0))
                ));

        Long processSettleOrderId = settleOrderItemPo.getProcessSettleOrderId();
        String completeUser = settleOrderItemPo.getCompleteUser();
        String completeUsername = settleOrderItemPo.getCompleteUsername();

        List<ProcessSettleDetailReportPo> processSettleDetailReportPos = Lists.newArrayList();
        sumByProcessCode.forEach((processCode, totalQualityGoodsCnt) -> {
            ProcessSettleDetailReportPo reportPo = new ProcessSettleDetailReportPo();
            reportPo.setProcessSettleOrderId(processSettleOrderId);
            reportPo.setProcessSettleOrderItemId(processSettleOrderItemId);
            reportPo.setCompleteUser(completeUser);
            reportPo.setCompleteUsername(completeUsername);

            reportPo.setProcessCode(processCode);
            ProcessPo matchProcess = processPos.stream()
                    .filter(processPo -> Objects.equals(processCode, processPo.getProcessCode()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchProcess)) {
                reportPo.setProcessLabel(matchProcess.getProcessLabel());
                reportPo.setProcessName(matchProcess.getProcessName());
            }

            reportPo.setQualityGoodsCnt(sumByProcessCode.getOrDefault(processCode, 0));

            // 获取当前工序所有扫码记录
            List<Long> matchProcessScanIds = settleOrderScanPos.stream()
                    .filter(settleOrderScanPo -> Objects.equals(processCode, settleOrderScanPo.getProcessCode()) &&
                            !Objects.equals(0L, settleOrderScanPo.getProcessOrderScanId()))
                    .map(ProcessSettleOrderScanPo::getProcessOrderScanId)
                    .collect(Collectors.toList());
            // 匹配当前工序所有阶梯提成
            List<ScanCommissionDetailPo> matchProcessCommissions = allScanCommissions.stream()
                    .filter(allScanCommission -> matchProcessScanIds.contains(allScanCommission.getProcessOrderScanId()))
                    .collect(Collectors.toList());

            List<CommissionDetailBo> commissionDetails
                    = CommissionDetailConverter.convertToCommissionDetailBoList(matchProcessCommissions);
            CommissionAmountBo firstLevelCommission
                    = processOrderScanBaseService.calculateCommissionAmount(commissionDetails, CommissionCategory.STAIR, CommissionAttribute.FIRST_LEVEL);
            reportPo.setFirstLevelQualityGoodsCnt(firstLevelCommission.getTotalQuantity());
            reportPo.setFirstLevelTotalCommission(firstLevelCommission.getTotalAmount());

            CommissionAmountBo secondLevelCommission
                    = processOrderScanBaseService.calculateCommissionAmount(commissionDetails, CommissionCategory.STAIR, CommissionAttribute.SECOND_LEVEL);
            reportPo.setSecondLevelQualityGoodsCnt(secondLevelCommission.getTotalQuantity());
            reportPo.setSecondLevelTotalCommission(secondLevelCommission.getTotalAmount());

            processSettleDetailReportPos.add(reportPo);
        });
        processSettleDetailReportDao.insertBatch(processSettleDetailReportPos);
    }
}
