package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.ProcessOrderScanQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.NeedProcessPlan;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessPlanDelay;
import com.hete.supply.scm.api.scm.entity.enums.ProcessProgressStatus;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderScanExportVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderScanMonthStatisticsExportVo;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.CommissionAmountBo;
import com.hete.supply.scm.server.scm.entity.bo.OperatorUserBo;
import com.hete.supply.scm.server.scm.entity.bo.ProcessCodeMappingEntryBo;
import com.hete.supply.scm.server.scm.enums.DefaultDatabaseTime;
import com.hete.supply.scm.server.scm.process.converter.ProcessCommissionRuleConverter;
import com.hete.supply.scm.server.scm.process.converter.ProcessOrderConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.enums.CommissionCategory;
import com.hete.supply.scm.server.scm.process.enums.ProcessOrderScanFilterType;
import com.hete.supply.scm.server.scm.process.enums.ProcessStage;
import com.hete.supply.scm.server.scm.process.service.base.ProcessBaseService;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderBaseService;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderScanBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.mybatis.plus.global.SqlConstant;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/17 09:39
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessOrderScanBizService {
    private final ProcessOrderScanDao processOrderScanDao;
    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderProcedureDao processOrderProcedureDao;
    private final ProcessDao processDao;
    private final ProcessOrderBaseService processOrderBaseService;
    private final ProcessOrderItemDao processOrderItemDao;
    private final ProcessOrderScanBaseService processOrderScanBaseService;
    private final ProcessProcedureEmployeePlanDao processProcedureEmployeePlanDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final RedissonClient redissonClient;
    private final ProcessBaseService processBaseService;
    private final ScanCommissionDetailDao scanCommissionDetailDao;
    private final SdaRemoteService sdaRemoteService;

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    public static final String MONTHLY_PROCESSING_DETAILS_SQL = "scan:quality_goods_cnt:sum:sql";
    private final ProcessOrderScanRelateDao processOrderScanRelateDao;

    private final static int PAGE_SIZE = 1000;
    private final static int MAX_ITERATIONS = 200;

    public CommonPageResult.PageInfo<ProcessOrderScanVo> getByPage(ProcessOrderScanQueryDto queryDto) {
        CommonPageResult.PageInfo<ProcessOrderScanVo> pageResult = processOrderScanDao.getByPage(queryDto);
        if (CollectionUtils.isEmpty(pageResult.getRecords())) {
            return new CommonPageResult.PageInfo<>();
        }

        // 在查询结果的基础上赋值新字段
        assignNewFields(pageResult);
        return pageResult;
    }

    private void assignNewFields(CommonPageResult.PageInfo<ProcessOrderScanVo> pageResult) {
        List<ProcessOrderScanVo> records = pageResult.getRecords();
        // 如果工序记录列表为空，不进行操作
        if (CollectionUtils.isEmpty(records)) {
            return;
        }

        // 工序信息
        List<String> processCodes = records.stream()
                .map(ProcessOrderScanVo::getProcessCode)
                .collect(Collectors.toList());
        List<ProcessPo> processPoList = processDao.getByProcessCodes(processCodes);

        // 工序提成详情信息
        List<Long> processOrderScanIds = records.stream()
                .map(ProcessOrderScanVo::getProcessOrderScanId)
                .collect(Collectors.toList());
        List<CommissionBo> commissions = processOrderScanBaseService.getCommissions(processOrderScanIds);

        for (ProcessOrderScanVo record : records) {
            String processCode = record.getProcessCode();
            Long processOrderScanId = record.getProcessOrderScanId();

            //工序信息
            ProcessPo matchProcessPo = processPoList.stream()
                    .filter(processPo -> Objects.equals(processCode, processPo.getProcessCode()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchProcessPo)) {
                record.setProcessSecondName(matchProcessPo.getProcessSecondName());
                record.setProcessLabel(matchProcessPo.getProcessLabel());
                record.setProcessType(matchProcessPo.getProcessType());
            }

            //工序提成
            CommissionBo matchCommission = commissions.stream()
                    .filter(commission -> Objects.equals(processOrderScanId, commission.getProcessOrderScanId()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchCommission)) {
                if (CollectionUtils.isNotEmpty(matchCommission.getCommissionDetails())) {
                    List<CommissionDetailBo> commissionDetails = matchCommission.getCommissionDetails();
                    CommissionAmountBo firstLevelCommission
                            = processOrderScanBaseService.calculateCommissionAmount(commissionDetails, CommissionCategory.STAIR, CommissionAttribute.FIRST_LEVEL);
                    record.setFirstLevelCommissionAmount(firstLevelCommission.getTotalAmount());

                    CommissionAmountBo secondLevelCommission
                            = processOrderScanBaseService.calculateCommissionAmount(commissionDetails, CommissionCategory.STAIR, CommissionAttribute.SECOND_LEVEL);
                    record.setSecondLevelCommissionAmount(secondLevelCommission.getTotalAmount());
                }
                record.setTotalCommissionAmount(matchCommission.getTotalCommission());
            }

            //工序状态
            if (StrUtil.isBlank(record.getReceiptUser())) {
                record.setProcessProgressStatus(ProcessProgressStatus.UN_START);
            } else if (StrUtil.isNotBlank(record.getReceiptUser()) && StrUtil.isBlank(record.getCompleteUser())) {
                record.setProcessProgressStatus(ProcessProgressStatus.PROCESSING);
            } else if (StrUtil.isNotBlank(record.getCompleteUser())) {
                record.setProcessProgressStatus(ProcessProgressStatus.COMPLETED);
            }
        }
    }

    public Integer getExportTotals(ProcessOrderScanQueryByApiDto dto) {
        return processOrderScanDao.getExportTotals(dto);
    }

    /**
     * 查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderScanExportVo> getExportList(ProcessOrderScanQueryByApiDto dto) {
        CommonPageResult.PageInfo<ProcessOrderScanExportVo> exportList
                = processOrderScanDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<ProcessOrderScanExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        // 工序信息
        List<String> processCodes = records.stream()
                .map(ProcessOrderScanExportVo::getProcessCode)
                .collect(Collectors.toList());
        List<ProcessPo> processPoList = processDao.getByProcessCodes(processCodes);

        // 工序提成详情信息
        List<Long> processOrderScanIds = records.stream()
                .map(ProcessOrderScanExportVo::getProcessOrderScanId)
                .collect(Collectors.toList());
        List<CommissionBo> commissions = processOrderScanBaseService.getCommissions(processOrderScanIds);

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(ProcessOrderScanExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        for (ProcessOrderScanExportVo record : records) {
            Long processOrderScanId = record.getProcessOrderScanId();
            String processCode = record.getProcessCode();

            record.setPlatform(platCodeNameMap.get(record.getPlatform()));

            ProcessPo matchProcessPo = processPoList.stream()
                    .filter(processPo -> Objects.equals(processCode, processPo.getProcessCode()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchProcessPo)) {
                record.setProcessSecondName(matchProcessPo.getProcessSecondName());
                record.setProcessLabel(matchProcessPo.getProcessLabel());
                record.setProcessCommission(matchProcessPo.getCommission());
            }

            CommissionBo matchCommission = commissions.stream()
                    .filter(commission -> Objects.equals(processOrderScanId, commission.getProcessOrderScanId()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchCommission)) {
                record.setTotalProcessCommission(matchCommission.getTotalCommission());

                if (CollectionUtils.isNotEmpty(matchCommission.getCommissionDetails())) {
                    List<CommissionDetailBo> commissionDetails = matchCommission.getCommissionDetails();
                    BigDecimal firstLevelCommissionAmount = commissionDetails.stream()
                            .filter(detail -> Objects.equals(CommissionCategory.STAIR,
                                    detail.getCommissionCategory()) &&
                                    Objects.equals(CommissionAttribute.FIRST_LEVEL, detail.getCommissionAttribute()))
                            .map(CommissionDetailBo::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    record.setFirstLevelCommissionAmount(firstLevelCommissionAmount);

                    BigDecimal secondLevelCommissionAmount = commissionDetails.stream()
                            .filter(detail -> Objects.equals(CommissionCategory.STAIR,
                                    detail.getCommissionCategory()) &&
                                    Objects.equals(CommissionAttribute.SECOND_LEVEL, detail.getCommissionAttribute()))
                            .map(CommissionDetailBo::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    record.setSecondLevelCommissionAmount(secondLevelCommissionAmount);
                }
            }
        }

        return exportList;
    }

    /**
     * 查询加工单详情
     *
     * @param dto
     * @return
     */
    public ProcessOrderScanByH5Vo getByProcessOrderNo(ProcessOrderScanByNoDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        Assert.notNull(processOrderPo,
                () -> new ParamIllegalException("当前单号：{}，请扫正确的加工单号：JG********", processOrderNo));

        ProcessOrderScanByH5Vo processOrderScanByH5Vo = new ProcessOrderScanByH5Vo();
        processOrderScanByH5Vo.setProcessOrderId(processOrderPo.getProcessOrderId());
        processOrderScanByH5Vo.setProcessOrderNo(processOrderPo.getProcessOrderNo());
        processOrderScanByH5Vo.setSpu(processOrderPo.getSpu());
        processOrderScanByH5Vo.setProcessOrderStatus(processOrderPo.getProcessOrderStatus());
        processOrderScanByH5Vo.setFileCodeList(Arrays.asList(processOrderPo.getFileCode()
                .split(",")));
        // 获取加工明细
        List<ProcessOrderItemPo> processOrderItemPos
                = processOrderItemDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        List<ProcessOrderItemVo> processOrderItemVos = processOrderItemPos.stream()
                .map(ProcessOrderConverter.INSTANCE::convert)
                .collect(Collectors.toList());
        processOrderScanByH5Vo.setProcessOrderItems(processOrderItemVos);
        ArrayList<ProcessOrderProcedureByH5Vo> processOrderProcedureByH5Vos = new ArrayList<>();


        // 获取加工单所有工序
        List<ProcessOrderProcedurePo> processOrderProcedurePos
                = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        List<ProcessPo> processPoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processOrderProcedurePos)) {
            List<Long> processIds = processOrderProcedurePos.stream()
                    .map(ProcessOrderProcedurePo::getProcessId)
                    .collect(Collectors.toList());
            processPoList = processDao.getByProcessIds(processIds);
        }
        Map<String, ProcessPo> groupedProcessPos = processPoList.stream()
                .collect(
                        Collectors.toMap(ProcessPo::getProcessCode, item -> item, (existing, replacement) -> existing));


        // 获取加工单所有的扫码记录
        List<ProcessOrderScanPo> processOrderScanPoList
                = Optional.ofNullable(processOrderScanDao.getByProcessOrderNo(processOrderNo))
                .orElse(Collections.emptyList());
        Map<Long, List<ProcessOrderScanPo>> groupedProcessOrderScanPos = processOrderScanPoList.stream()
                .collect(Collectors.groupingBy(ProcessOrderScanPo::getProcessOrderProcedureId));
        Optional<ProcessOrderScanPo> lastScanPoOptional = processOrderScanPoList.stream()
                .filter(it -> null != it.getCompleteTime())
                .max(Comparator.comparing(ProcessOrderScanPo::getCreateTime));
        lastScanPoOptional.ifPresent(processOrderScanByH5Vo::setLastProcessOrderScanPo);

        Optional<ProcessOrderScanPo> firstScanOptional = processOrderScanPoList.stream()
                .min(Comparator.comparing(ProcessOrderScanPo::getCreateTime));
        if (firstScanOptional.isPresent()) {
            ProcessOrderScanPo firstProcessOrderScanPo = firstScanOptional.get();
            processOrderScanByH5Vo.setMaxAvailableReceiptNum(
                    firstProcessOrderScanPo.getReceiptNum() + processOrderPo.getAvailableProductNum());
        } else {
            Optional<ProcessOrderItemPo> firstOrderItemPo = processOrderItemPos.stream()
                    .findFirst();
            firstOrderItemPo.ifPresent(processOrderItemPo -> processOrderScanByH5Vo.setMaxAvailableReceiptNum(
                    processOrderItemPo.getProcessNum()));
        }

        // 优先把未完成的加工工序排在前面
        processOrderProcedurePos.forEach(it -> {
            ProcessOrderProcedureByH5Vo processOrderProcedureByH5Vo
                    = this.getProcessOrderProcedureByH5Vo(it, groupedProcessPos);
            List<ProcessOrderScanPo> processOrderScanPos
                    = groupedProcessOrderScanPos.get(it.getProcessOrderProcedureId());

            // 存在扫码记录，但是未完成收货
            if (CollectionUtils.isNotEmpty(processOrderScanPos)) {
                Optional<ProcessOrderScanPo> first = processOrderScanPos.stream()
                        .findFirst();

                if (first.isPresent() && null == first.get()
                        .getCompleteTime()) {
                    ProcessOrderScanPo processOrderScanPo = first.get();
                    processOrderProcedureByH5Vo.setReceiptNum(processOrderScanPo.getReceiptNum());
                    processOrderProcedureByH5Vo.setReceiptUser(processOrderScanPo.getReceiptUser());
                    processOrderProcedureByH5Vo.setReceiptUsername(processOrderScanPo.getReceiptUsername());
                    processOrderProcedureByH5Vo.setReceiptTime(processOrderScanPo.getReceiptTime());
                    processOrderProcedureByH5Vo.setProcessingTime(processOrderScanPo.getProcessingTime());
                    processOrderProcedureByH5Vo.setProcessingUser(processOrderScanPo.getProcessingUser());
                    processOrderProcedureByH5Vo.setProcessingUsername(processOrderScanPo.getProcessingUsername());
                    processOrderProcedureByH5Vo.setDefectiveGoodsCnt(processOrderScanPo.getDefectiveGoodsCnt());
                    processOrderProcedureByH5Vo.setQualityGoodsCnt(processOrderScanPo.getQualityGoodsCnt());
                    processOrderProcedureByH5Vo.setCompleteTime(processOrderScanPo.getCompleteTime());
                    processOrderProcedureByH5Vo.setCompleteUsername(processOrderScanPo.getCompleteUsername());
                    processOrderProcedureByH5Vo.setSort(it.getSort());
                    processOrderProcedureByH5Vos.add(processOrderProcedureByH5Vo);
                }
            }
            // 不存在扫码记录
            if (CollectionUtils.isEmpty(processOrderScanPos)) {
                processOrderProcedureByH5Vo.setReceiptNum(0);
                processOrderProcedureByH5Vo.setReceiptUser("");
                processOrderProcedureByH5Vo.setReceiptUsername("");
                processOrderProcedureByH5Vo.setReceiptTime(null);
                processOrderProcedureByH5Vo.setProcessingTime(null);
                processOrderProcedureByH5Vo.setProcessingUser("");
                processOrderProcedureByH5Vo.setProcessingUsername("");
                processOrderProcedureByH5Vo.setDefectiveGoodsCnt(0);
                processOrderProcedureByH5Vo.setQualityGoodsCnt(0);
                processOrderProcedureByH5Vo.setCompleteTime(null);
                processOrderProcedureByH5Vo.setCompleteUsername("");
                processOrderProcedureByH5Vo.setSort(it.getSort());
                processOrderProcedureByH5Vos.add(processOrderProcedureByH5Vo);
            }


        });

        // 过滤需要进行扫码的工序
        processOrderProcedurePos.forEach(it -> {
            List<ProcessOrderScanPo> processOrderScanPos
                    = groupedProcessOrderScanPos.get(it.getProcessOrderProcedureId());
            if (CollectionUtils.isNotEmpty(processOrderScanPos)) {
                Optional<ProcessOrderScanPo> firstScanPo = processOrderScanPos.stream()
                        .findFirst();
                if (firstScanPo.isPresent() && null != firstScanPo.get()
                        .getCompleteTime()) {
                    ProcessOrderProcedureByH5Vo processOrderProcedureByH5Vo
                            = this.getProcessOrderProcedureByH5Vo(it, groupedProcessPos);
                    ProcessOrderScanPo processOrderScanPo = firstScanPo.get();
                    processOrderProcedureByH5Vo.setReceiptNum(processOrderScanPo.getReceiptNum());
                    processOrderProcedureByH5Vo.setReceiptUser(processOrderScanPo.getReceiptUser());
                    processOrderProcedureByH5Vo.setReceiptUsername(processOrderScanPo.getReceiptUsername());
                    processOrderProcedureByH5Vo.setReceiptTime(processOrderScanPo.getReceiptTime());
                    processOrderProcedureByH5Vo.setProcessingTime(processOrderScanPo.getProcessingTime());
                    processOrderProcedureByH5Vo.setProcessingUser(processOrderScanPo.getProcessingUser());
                    processOrderProcedureByH5Vo.setProcessingUsername(processOrderScanPo.getProcessingUsername());
                    processOrderProcedureByH5Vo.setDefectiveGoodsCnt(processOrderScanPo.getDefectiveGoodsCnt());
                    processOrderProcedureByH5Vo.setQualityGoodsCnt(processOrderScanPo.getQualityGoodsCnt());
                    processOrderProcedureByH5Vo.setCompleteTime(processOrderScanPo.getCompleteTime());
                    processOrderProcedureByH5Vo.setCompleteUsername(processOrderScanPo.getCompleteUsername());
                    processOrderProcedureByH5Vo.setSort(it.getSort());
                    processOrderProcedureByH5Vos.add(processOrderProcedureByH5Vo);
                }
            }
        });

        processOrderScanByH5Vo.setProcessOrderProcedures(processOrderProcedureByH5Vos);

        return processOrderScanByH5Vo;

    }

    /**
     * 实例化 Vo
     *
     * @param processOrderProcedurePo
     * @return
     */
    public ProcessOrderProcedureByH5Vo getProcessOrderProcedureByH5Vo(ProcessOrderProcedurePo processOrderProcedurePo,
                                                                      Map<String, ProcessPo> groupedProcessPos) {
        ProcessOrderProcedureByH5Vo processOrderProcedureByH5Vo = new ProcessOrderProcedureByH5Vo();
        processOrderProcedureByH5Vo.setProcessOrderProcedureId(processOrderProcedurePo.getProcessOrderProcedureId());
        processOrderProcedureByH5Vo.setProcessCode(processOrderProcedurePo.getProcessCode());
        processOrderProcedureByH5Vo.setProcessName(processOrderProcedurePo.getProcessName());
        ProcessPo processPo = groupedProcessPos.get(processOrderProcedurePo.getProcessCode());
        if (null != processPo) {
            processOrderProcedureByH5Vo.setProcessLabel(processPo.getProcessLabel());
            processOrderProcedureByH5Vo.setProcessSecondName(processPo.getProcessSecondName());
            processOrderProcedureByH5Vo.setCommission(processPo.getCommission());
        } else {
            processOrderProcedureByH5Vo.setCommission(BigDecimal.ZERO);
        }
        return processOrderProcedureByH5Vo;
    }

    /**
     * 确认接货
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmReceive(ProcessOrderScanConfirmReceiveDto dto) {
        final Long processOrderProcedureId = dto.getProcessOrderProcedureId();

        // 工序操作环节校验
        processOrderScanBaseService.validateProcessStage(processOrderProcedureId, ProcessStage.RECEIVING);

        ProcessOrderPo processOrderPo = processOrderScanBaseService.confirmReceive(processOrderProcedureId, dto.getReceiptNum());
        Integer sort = processOrderPo.getProcessOrderStatus().getSort();
        if (!ProcessOrderStatus.PROCESSING.getSort().equals(sort)) {
            // 需要将状态变成加工中
            OperatorUserBo operatorUserBo = new OperatorUserBo();
            processOrderBaseService.changeStatus(processOrderPo, ProcessOrderStatus.PROCESSING, operatorUserBo);
        }

        return true;
    }


    /**
     * 完成工序
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeProcedure(ProcessOrderScanCompleteProcedureDto dto) {
        // 加工单校验
        final String processOrderNo = dto.getProcessOrderNo();
        ProcessOrderPo processOrderPo = ParamValidUtils.requireNotNull(
                processOrderDao.getByProcessOrderNo(processOrderNo),
                "未找到相关的加工单信息！请核对后提交。"
        );

        // 工序操作环节校验
        Long processOrderProcedureId = dto.getProcessOrderProcedureId();
        processOrderScanBaseService.validateProcessStage(processOrderProcedureId, ProcessStage.COMPLETION);
        ProcessOrderScanPo processOrderScanPo = ParamValidUtils.requireNotNull(
                processOrderScanDao.getByProcessOrderProcedureId(processOrderProcedureId),
                "未找到相关工序扫码记录！请核对后提交"
        );

        // 是否完成扫码校验
        LocalDateTime completeTime = processOrderScanPo.getCompleteTime();
        if (Objects.nonNull(completeTime) && !Objects.equals(SqlConstant.getDefaultDate(), completeTime)) {
            throw new ParamIllegalException("该工序已完成！请核对后提交。");
        }

        // 操作人 = 接货人校验
        String receiptUser = processOrderScanPo.getReceiptUser();
        ParamValidUtils.requireEquals(GlobalContext.getUserKey(), receiptUser, "当前操作人非接货人，不可进行此操作");

        // 工序信息
        String processCode = processOrderScanPo.getProcessCode();
        ParamValidUtils.requireNotNull(
                processDao.getByProcessCode(processCode), "未找到相关工序！请核对后提交。"
        );

        // 正品数&次品数校验
        List<ProcessOrderScanPo> processOrderScanPoList
                = Optional.ofNullable(processOrderScanDao.getByProcessOrderNo(processOrderNo)).orElse(Collections.emptyList());
        Integer maxAvailableReceiptNum = 0;
        Optional<ProcessOrderScanPo> firstScanOptional = processOrderScanPoList.stream()
                .min(Comparator.comparing(ProcessOrderScanPo::getCreateTime));
        if (firstScanOptional.isPresent()) {
            ProcessOrderScanPo firstProcessOrderScanPo = firstScanOptional.get();
            maxAvailableReceiptNum = firstProcessOrderScanPo.getReceiptNum() + processOrderPo.getAvailableProductNum();
        }
        if (!maxAvailableReceiptNum.equals(dto.getQualityGoodsCnt() + dto.getDefectiveGoodsCnt())) {
            throw new ParamIllegalException("正品数和次品数必须等于：{}", maxAvailableReceiptNum);
        }

        if (NeedProcessPlan.TRUE.equals(processOrderPo.getNeedProcessPlan())) {
            ProcessProcedureEmployeePlanPo processProcedureEmployeePlanPo
                    = processProcedureEmployeePlanDao.getByProcessOrderProcedureId(processOrderProcedureId);
            if (Objects.nonNull(processProcedureEmployeePlanPo)) {
                LocalDateTime now = new DateTime().toLocalDateTime();
                processProcedureEmployeePlanPo.setActEndTime(now);
                log.info("完成加工！更新排产加工单工序的实际开始时间，加工单工序Id：{}，实际完成加工时间：{}",
                        processOrderProcedureId, now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                processProcedureEmployeePlanDao.updateById(processProcedureEmployeePlanPo);

                // 校验订单是否逾期
                LocalDateTime expectEndTime = processProcedureEmployeePlanPo.getExpectEndTime();
                if (now.isAfter(expectEndTime)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedExpectedEndTime = now.format(formatter);
                    String formattedActualEndTime = expectEndTime.format(formatter);

                    processOrderPo.setProcessPlanDelay(ProcessPlanDelay.TRUE);
                    processOrderDao.updateById(processOrderPo);
                    log.info("完成工序！更新排产加工单是否延期：{}，加工单号：{}，预计结束时间：{}，实际结束时间：{}",
                            ProcessPlanDelay.TRUE,
                            processOrderNo, formattedExpectedEndTime, formattedActualEndTime);
                }
            }
        }

        processOrderScanPo.setCompleteTime(new DateTime().toLocalDateTime());
        processOrderScanPo.setCompleteUser(GlobalContext.getUserKey());
        processOrderScanPo.setCompleteUsername(GlobalContext.getUsername());
        processOrderScanPo.setQualityGoodsCnt(dto.getQualityGoodsCnt());
        processOrderScanPo.setDefectiveGoodsCnt(dto.getDefectiveGoodsCnt());
        processOrderScanDao.updateByIdVersion(processOrderScanPo);

        processOrderScanBaseService.updateProcessOrderScanRelation(processOrderScanPo.getProcessOrderScanId());

        this.checkProcessOrderStatus(processOrderPo);
        // 创建提成明细
        processOrderScanBaseService.createCommissionDetails(processOrderScanPo.getProcessOrderScanId());
        return true;
    }


    /**
     * 修改加工单状态
     *
     * @param processOrderPo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkProcessOrderStatus(ProcessOrderPo processOrderPo) {
        String processOrderNo = processOrderPo.getProcessOrderNo();
        // 处理加工单状态
        // 必须前面一道工序完成，才能进行下一道工序的接货
        // 获取加工单所有工序
        List<ProcessOrderProcedurePo> processOrderProcedurePos
                = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        // 获取加工单所有的扫码记录
        List<ProcessOrderScanPo> processOrderScanPos
                = Optional.ofNullable(processOrderScanDao.getByProcessOrderNo(processOrderNo))
                .orElse(new ArrayList<>());
        // 过滤需要进行扫码的工序
        List<ProcessOrderProcedurePo> needScanProcedurePos = processOrderProcedurePos.stream()
                .filter(it -> {
                    List<Long> processOrderProcedureIds = processOrderScanPos.stream()
                            .map(ProcessOrderScanPo::getProcessOrderProcedureId)
                            .collect(Collectors.toList());
                    return !processOrderProcedureIds.contains(it.getProcessOrderProcedureId());
                })
                .collect(Collectors.toList());
        OperatorUserBo operatorUserBo = new OperatorUserBo();
        if (CollectionUtils.isEmpty(needScanProcedurePos)) {
            // 没有需要进行处理的工序了，需要将加工单变成完工待交接
            processOrderPo.setProcessCompletionTime(LocalDateTime.now());
            processOrderBaseService.changeStatus(processOrderPo, ProcessOrderStatus.WAIT_MOVING, operatorUserBo);
        }
        return true;
    }

    /**
     * @Description 根据工序id删除扫码记录
     * @author yanjiawei
     * @Date 2024/10/30 10:41
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.REMOVE_SCAN_PROCEDURE_ID, key = "#dto.processOrderProcedureId",
            waitTime = 1, leaseTime = -1, exceptionDesc = "清除扫码记录正在处理中，请稍后再试！")
    public Boolean removeByProcedureId(ProcessOrderScanRemoveByProcedureIdDto dto) {
        Long procedureId = dto.getProcessOrderProcedureId();
        ProcessOrderScanPo processOrderScanPo = ParamValidUtils.requireNotNull(
                processOrderScanDao.getByProcessOrderProcedureId(procedureId),
                "扫码记录不存在！请刷新页面后重试。"
        );
        String processOrderNo = processOrderScanPo.getProcessOrderNo();

        ProcessOrderPo processOrderPo = ParamValidUtils.requireNotNull(processOrderDao.getByProcessOrderNo(processOrderNo),
                StrUtil.format("加工单{}不存在!请刷新页面后重试。", processOrderNo)
        );
        if (processOrderPo.getProcessOrderStatus().getSort() > ProcessOrderStatus.WAIT_MOVING.getSort()) {
            throw new ParamIllegalException("{}之后的加工单禁止删除", ProcessOrderStatus.WAIT_MOVING.getDesc());
        }

        //只能删除本月的加工扫码记录
        DateTime monthBeginDateTime = DateUtil.beginOfMonth(new Date());
        LocalDateTime canDelBeginTime = TimeUtil.convertZone(monthBeginDateTime.toLocalDateTime(), TimeZoneId.CN, TimeZoneId.UTC);
        log.info("允许删除扫码记录开始时间=>{}", canDelBeginTime);
        if (null != processOrderScanPo.getCompleteTime() && processOrderScanPo.getCompleteTime().isBefore(canDelBeginTime)) {
            throw new ParamIllegalException("该扫码记录已经纳入结算单，无法清除！");
        }

        //校验：删除扫码记录等于最后一道工序扫码记录
        List<ProcessOrderScanPo> procScanPoList = Optional.ofNullable(processOrderScanDao.getByProcessOrderNo(processOrderNo)).orElse(new ArrayList<>());
        List<ProcessOrderScanPo> processOrderScanPoList = procScanPoList.stream()
                .sorted(Comparator.comparing(ProcessOrderScanPo::getProcessOrderScanId).reversed())
                .collect(Collectors.toList());
        Optional<ProcessOrderScanPo> firstScanPoOptional = processOrderScanPoList.stream().findFirst();
        if (firstScanPoOptional.isPresent()) {
            ProcessOrderScanPo lastProcessOrderScanPo = firstScanPoOptional.get();
            Assert.isTrue(processOrderScanPo.getProcessOrderScanId().equals(lastProcessOrderScanPo.getProcessOrderScanId()),
                    () -> new ParamIllegalException("请清除下一道的扫码记录才能清除本工序的扫码记录"));
        }

        //删除扫码记录关联信息&重新计算提成
        Long delScanId = processOrderScanPo.getProcessOrderScanId();
        processOrderScanBaseService.removeProcessOrderScanRelation(delScanId);
        processOrderScanBaseService.reCreateCommissionDetails(delScanId);
        processOrderScanDao.removeByIdVersion(processOrderScanPo);

        //变更加工单状态
        List<ProcessOrderScanPo> curAllScanPoList = procScanPoList.stream()
                .filter(item -> !processOrderScanPo.getProcessOrderScanId().equals(item.getProcessOrderScanId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(curAllScanPoList)) {
            processOrderBaseService.changeStatus(processOrderPo, ProcessOrderStatus.WAIT_PRODUCE, new OperatorUserBo());
        } else {
            processOrderPo.setProcessCompletionTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
            processOrderBaseService.changeStatus(processOrderPo, ProcessOrderStatus.PROCESSING, new OperatorUserBo());
        }
        return true;
    }


    /**
     * 统计扫码记录数据
     *
     * @return 扫码记录统计结果
     */
    public ProcessOrderScanStatNumVo scanRecordDataStatistics() {
        ProcessOrderScanStatNumVo result = new ProcessOrderScanStatNumVo();
        String completeUser = GlobalContext.getUserKey();
        final Date date = new Date();

        LocalDateTime thisMonthBeginTime = TimeUtil.convertZone(DateUtil.beginOfMonth(date)
                        .toLocalDateTime(), TimeZoneId.CN,
                TimeZoneId.UTC);
        LocalDateTime thisMonthEndTime = TimeUtil.convertZone(DateUtil.endOfMonth(date)
                        .toLocalDateTime(), TimeZoneId.CN,
                TimeZoneId.UTC);
        ProcessOrderScanStatNumVo thisMonthStaticResult
                = this.getProcessOrderScanStatNumVo(thisMonthBeginTime, thisMonthEndTime, completeUser);
        result.setTotalQualityGoodsCnt(thisMonthStaticResult.getTotalQualityGoodsCnt());
        result.setTotalProcessCommission(thisMonthStaticResult.getTotalProcessCommission());

        LocalDateTime todayBeginTime = TimeUtil.convertZone(LocalDate.now()
                .atStartOfDay(), TimeZoneId.CN, TimeZoneId.UTC);
        ProcessOrderScanStatNumVo todayStaticResult
                = this.getProcessOrderScanStatNumVo(todayBeginTime, LocalDateTime.now(), completeUser);
        result.setTodayQualityGoodsCnt(todayStaticResult.getTotalQualityGoodsCnt());
        result.setTodayProcessCommission(todayStaticResult.getTotalProcessCommission());

        LocalDateTime lastMonthBeginTime
                = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
        LocalDateTime lastMonthEndTime
                = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
        ProcessOrderScanStatNumVo lastMonthStaticResult
                = this.getProcessOrderScanStatNumVo(lastMonthBeginTime, lastMonthEndTime, completeUser);
        result.setLastMonthProcessCommission(lastMonthStaticResult.getTotalProcessCommission());
        return result;
    }


    private ProcessOrderScanStatNumVo getProcessOrderScanStatNumVo(LocalDateTime startTime,
                                                                   LocalDateTime endTime,
                                                                   String completeUser) {
        ProcessOrderScanStatNumVo resultVo = new ProcessOrderScanStatNumVo();
        ProcessOrderScanStatNumDto queryParam = new ProcessOrderScanStatNumDto(startTime, endTime, completeUser);

        ScanRecordDataStatisticsBo scanRecordDataStatisticsBo = processOrderScanDao.statNumByMonth(queryParam);
        List<Long> processScanIds = processOrderScanDao.statScanIdsNumByMonth(queryParam);
        List<CommissionBo> commissions = processOrderScanBaseService.getCommissions(processScanIds);

        if (Objects.nonNull(scanRecordDataStatisticsBo)) {
            resultVo.setTotalQualityGoodsCnt(scanRecordDataStatisticsBo.getTotalQualityGoodsCnt());

            if (CollectionUtils.isNotEmpty(commissions)) {
                BigDecimal totalProcessCommission = commissions.stream()
                        .map(CommissionBo::getTotalCommission)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                resultVo.setTotalProcessCommission(totalProcessCommission);
            }
        }
        return resultVo;
    }


    /**
     * 查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderScanStatListVo> statList(ProcessOrderScanStatListDto dto) {
        // 处理月份类型，设置完成人=当前用户
        handleMonthType(dto);
        dto.setCompleteUser(GlobalContext.getUserKey());

        CommonPageResult.PageInfo<ProcessOrderScanStatListVo> pageResult
                = processOrderScanDao.statList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<ProcessOrderScanStatListVo> processOrderScanExportVos = pageResult.getRecords();
        if (CollectionUtils.isEmpty(processOrderScanExportVos)) {
            return pageResult;
        }

        // 扫码记录工序代码列表
        Set<String> processCodes = processOrderScanExportVos.stream()
                .map(ProcessOrderScanStatListVo::getProcessCode)
                .collect(Collectors.toSet());
        List<ProcessWithCommissionRuleBo> processWithCommissionRuleBoList
                = processBaseService.getProcessWithCommissionRule(processCodes);

        // 扫码记录主键id
        Set<Long> processOrderScanIds = processOrderScanExportVos.stream()
                .map(ProcessOrderScanStatListVo::getProcessOrderScanId)
                .collect(Collectors.toSet());
        List<CommissionBo> scanCommissions = processOrderScanBaseService.getCommissions(processOrderScanIds);

        for (ProcessOrderScanStatListVo record : processOrderScanExportVos) {
            String processCode = record.getProcessCode();
            Long processOrderScanId = record.getProcessOrderScanId();
            String completeUser = GlobalContext.getUserKey();

            // 通过工序代码匹配工序信息 & 工序规则信息列表
            ProcessWithCommissionRuleBo matchProcessWithCommissionRuleBo = processWithCommissionRuleBoList.stream()
                    .filter(processWithCommissionRuleBo -> Objects.equals(processCode,
                            processWithCommissionRuleBo.getProcessCode()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchProcessWithCommissionRuleBo)) {
                ProcessBo process = matchProcessWithCommissionRuleBo.getProcess();
                if (Objects.nonNull(process)) {
                    record.setProcessSecondName(process.getProcessSecondName());
                    record.setProcessLabel(process.getProcessLabel());
                    record.setProcessType(process.getProcessType());
                }
            }

            // 统计当前临界值
            LocalDateTime calculateCntBeginTime;
            if (Objects.nonNull(dto.getCompleteTimeStart())) {
                calculateCntBeginTime = dto.getCompleteTimeStart();
            } else {
                LocalDateTime beginOfMonthCnTime
                        = TimeUtil.convertZone(record.getCompleteTime(), TimeZoneId.UTC, TimeZoneId.CN)
                        .with(TemporalAdjusters.firstDayOfMonth())
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0);
                calculateCntBeginTime = TimeUtil.convertZone(beginOfMonthCnTime, TimeZoneId.CN, TimeZoneId.UTC);
            }
            int curProcessGoodsCnt = processOrderScanDao.sumQualityGoodsCnt(processCode,
                    completeUser,
                    calculateCntBeginTime,
                    record.getCompleteTime());
            int relateGoodsCnt = processOrderScanRelateDao.calculateQualityGoodsCountTotal(processCode,
                    null,
                    completeUser,
                    calculateCntBeginTime,
                    record.getCompleteTime());
            record.setCurrentProcessTotalQualityGoodsCnt(curProcessGoodsCnt + relateGoodsCnt);

            // 匹配扫码记录提成明细
            CommissionBo matchCommission = scanCommissions.stream()
                    .filter(scanCommission -> Objects.equals(processOrderScanId,
                            scanCommission.getProcessOrderScanId()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchCommission)) {
                record.setTotalProcessCommission(matchCommission.getTotalCommission());

                List<CommissionDetailBo> commissionDetails = matchCommission.getCommissionDetails();
                List<ProcessOrderScanStatListVo.ScanCommissionDetailVo> scanCommissionDetailVos
                        = ProcessCommissionRuleConverter.toScanCommissionDetailVos(commissionDetails);
                record.setScanCommissionDetailVos(scanCommissionDetailVos);
            }
        }
        return pageResult;
    }

    private void handleMonthType(ProcessOrderScanStatListDto dto) {
        if (ProcessOrderScanFilterType.MONTH.equals(dto.getType())) {
            Date date = new Date();
            LocalDateTime completeTimeBegin = TimeUtil.convertZone(DateUtil.beginOfMonth(date)
                            .toLocalDateTime(), TimeZoneId.CN,
                    TimeZoneId.UTC);
            LocalDateTime completeTimeEnd = TimeUtil.convertZone(DateUtil.endOfMonth(date)
                            .toLocalDateTime(), TimeZoneId.CN,
                    TimeZoneId.UTC);
            dto.setCompleteTimeStart(completeTimeBegin);
            dto.setCompleteTimeEnd(completeTimeEnd);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportMonthStatistics(ProcessOrderScanQueryByApiDto dto) {
        Integer monthScanStaticCount = getMonthScanStaticCount(dto);
        ParamValidUtils.requireGreaterThan(monthScanStaticCount, 0, "导出数据为空。");
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                GlobalContext.getUsername(), FileOperateBizType.SCM_PROCESS_ORDER_SCAN_MONTH_STATISTICS.getCode(), dto));
    }

    public CommonResult<ExportationListResultBo<ProcessOrderScanMonthStatisticsExportVo>> getMonthStatisticsExportList(ProcessOrderScanQueryByApiDto queryDto) {
        ExportationListResultBo<ProcessOrderScanMonthStatisticsExportVo> resultBo = new ExportationListResultBo<>();
        String sqlColumn = (String) redissonClient.getBucket(MONTHLY_PROCESSING_DETAILS_SQL)
                .get();
        if (StrUtil.isBlank(sqlColumn)) {
            List<ProcessCodeMappingEntryBo> processCodeMappingEntryBos = processDao.getProcessCodeMapping();
            if (CollectionUtils.isNotEmpty(processCodeMappingEntryBos)) {
                List<String> sqlMappingList = Lists.newArrayList();
                for (ProcessCodeMappingEntryBo processCodeMappingEntryBo : processCodeMappingEntryBos) {
                    sqlMappingList.add(
                            StrUtil.format("sum(case when process_code = {} then quality_goods_cnt else 0 end) as {}",
                                    processCodeMappingEntryBo.getProcessCode(),
                                    processCodeMappingEntryBo.getJavaColumnName()));
                }
                sqlColumn = String.join(",", sqlMappingList);
                RBucket<Object> bucket = redissonClient.getBucket(MONTHLY_PROCESSING_DETAILS_SQL);
                bucket.set(sqlColumn);
                bucket.expire(Instant.now()
                        .plus(Duration.ofDays(1)));
            }
        }

        IPage<ProcessOrderScanMonthStatisticsExportVo> pageResult
                = processOrderScanDao.getMonthStatisticsExportList(
                PageDTO.of(queryDto.getPageNo(), queryDto.getPageSize()), sqlColumn, queryDto);
        List<ProcessOrderScanMonthStatisticsExportVo> records = pageResult.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(record -> {
                record.setGradeTypeRemark(Objects.nonNull(record.getGradeType()) ? record.getGradeType()
                        .getRemark() : "");
            });
        }
        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    public Integer getMonthScanStaticCount(ProcessOrderScanQueryByApiDto queryDto) {
        return processOrderScanDao.getMonthScanStaticCount(queryDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCommissionDetailsJob(String scanCompleteTimeStr) {
        LocalDateTime completeTimeBegin
                = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);
        LocalDateTime completeTimeEnd
                = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);

        List<ProcessOrderScanPo> existExtraScans =
                processOrderScanDao.listByCompleteTime(completeTimeBegin, completeTimeEnd);
        if (CollectionUtils.isEmpty(existExtraScans)) {
            log.info("初始化{}月份扫码提成明细循环结束！", scanCompleteTimeStr);
            return;
        }
        List<String> processCodes = existExtraScans.stream()
                .distinct()
                .map(ProcessOrderScanPo::getProcessCode)
                .collect(Collectors.toList());
        List<ProcessWithCommissionRuleBo> processWithCommissionRules
                = processBaseService.getProcessWithCommissionRule(processCodes);

        for (ProcessOrderScanPo existExtraScan : existExtraScans) {
            Long processOrderScanId = existExtraScan.getProcessOrderScanId();
            String processCode = existExtraScan.getProcessCode();

            // 匹配工序信息
            ProcessWithCommissionRuleBo matchProcessRule = processWithCommissionRules.stream()
                    .filter(processWithCommissionRuleBo -> Objects.equals(processCode,
                            processWithCommissionRuleBo.getProcessCode()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchProcessRule)) {
                ProcessBo processPo = matchProcessRule.getProcess();
                TreeSet<ProcessCommissionRuleBo> rules = matchProcessRule.getRules();

                if (Objects.nonNull(processPo)) {
                    BigDecimal baseCommission = processPo.getCommission();
                    BigDecimal extraCommission = processPo.getExtraCommission();

                    // 更新扫码记录基础单价和额外单价
                    existExtraScan.setProcessCommission(baseCommission);
                    existExtraScan.setExtraCommission(extraCommission);
                    processOrderScanDao.updateByIdVersion(existExtraScan);

                    List<ScanCommissionDetailPo> scanCommissionDetailPos
                            = scanCommissionDetailDao.listByProcessOrderScanId(processOrderScanId);
                    if (CollectionUtils.isEmpty(scanCommissionDetailPos)) {
                        log.info(
                                "初始化{}月份扫码提成明细循环结束！原因：扫码记录无提成明细信息，循环下一个提成明细 scanId:{}",
                                scanCompleteTimeStr, processOrderScanId);
                        continue;
                    }

                    for (ScanCommissionDetailPo scanCommissionDetailPo : scanCommissionDetailPos) {
                        Long scanCommissionDetailId = scanCommissionDetailPo.getScanCommissionDetailId();
                        Integer sectionCount = scanCommissionDetailPo.getQuantity();
                        BigDecimal updateTotalAmountBefore = scanCommissionDetailPo.getTotalAmount();
                        CommissionAttribute commissionAttribute = scanCommissionDetailPo.getCommissionAttribute();

                        ProcessCommissionRuleBo matchRule = rules.stream()
                                .filter(rule -> Objects.equals(commissionAttribute.getCommissionLevel(),
                                        rule.getCommissionLevel()))
                                .findFirst()
                                .orElse(null);
                        if (Objects.nonNull(matchRule)) {
                            BigDecimal commissionCoefficient = matchRule.getCommissionCoefficient();
                            Integer sectionBegin = matchRule.getStartQuantity();
                            Integer sectionEnd = matchRule.getEndQuantity();
                            BigDecimal totalAmount
                                    = baseCommission.multiply(
                                            commissionCoefficient.multiply(BigDecimal.valueOf(sectionCount)))
                                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
                            scanCommissionDetailPo.setTotalAmount(
                                    totalAmount.add(extraCommission.multiply(BigDecimal.valueOf(sectionCount))));
                            log.info(
                                    "初始化提成明细：提成明细id:{},基础单价:{}，系数：{}%，变更前总提成：{} 变更后总提成:{}",
                                    scanCommissionDetailId, baseCommission, commissionCoefficient,
                                    updateTotalAmountBefore, scanCommissionDetailPo.getTotalAmount());
                            scanCommissionDetailPo.setCommissionRule(
                                    StrUtil.format("[{},{}]:{}*{}%", sectionBegin, sectionEnd, baseCommission,
                                            commissionCoefficient));
                        }
                        scanCommissionDetailDao.updateByIdVersion(scanCommissionDetailPo);
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void initScanExtraCommissionJob(String scanCompleteTimeStr) {
        // 获取额外提成单价>0的工序列表
        List<ProcessPo> processPos
                = processDao.getProcessesWithExtraCommissionGreaterThan(BigDecimal.ZERO);
        if (CollectionUtils.isEmpty(processPos)) {
            log.info("初始化扫码记录额外提成单价结束！原因：无额外单价超过{}的数据", BigDecimal.ZERO.intValue());
            return;
        }

        LocalDateTime completeTimeBegin
                = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);
        LocalDateTime completeTimeEnd
                = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);

        // 更新对应的扫码记录
        for (ProcessPo processPo : processPos) {
            String processCode = processPo.getProcessCode();
            BigDecimal extraCommission = processPo.getExtraCommission();

            List<ProcessOrderScanPo> updateExtraCommissionScans
                    = processOrderScanDao.listByProcessCode(processCode);

            if (CollectionUtils.isNotEmpty(updateExtraCommissionScans)) {
                List<ProcessOrderScanPo> completedScans = updateExtraCommissionScans.stream()
                        .filter(processOrderScanPo -> Objects.nonNull(processOrderScanPo.getCompleteTime())
                                && ScmTimeUtil.isWithinTimeRange(processOrderScanPo.getCompleteTime(),
                                completeTimeBegin, completeTimeEnd))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(completedScans)) {
                    log.info("初始化日期：{} 已完成工序扫码且对应的工序配置了额外单价的扫码记录，条数：{}",
                            scanCompleteTimeStr, completedScans.size());
                    completedScans.forEach(completedScan -> completedScan.setExtraCommission(extraCommission));
                    processOrderScanDao.updateBatchByIdVersion(completedScans);
                }

                List<ProcessOrderScanPo> unCompletedScans = updateExtraCommissionScans.stream()
                        .filter(processOrderScanPo -> Objects.isNull(processOrderScanPo.getCompleteTime()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(unCompletedScans)) {
                    log.info("初始化未完成工序扫码且对应的工序配置了额外单价的扫码记录，条数：{}", completedScans.size());
                    unCompletedScans.forEach(unCompletedScan -> unCompletedScan.setExtraCommission(extraCommission));
                    processOrderScanDao.updateBatchByIdVersion(unCompletedScans);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void refreshCommissionDetailsJob(String scanCompleteTimeStr) {
        LocalDateTime completeTimeBegin
                = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);
        LocalDateTime completeTimeEnd
                = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);

        List<ProcessOrderScanPo> distinctUserAndProcessCodeSortedByUserName
                = processOrderScanDao.getDistinctUserAndProcessCodeSortedByUser(completeTimeBegin, completeTimeEnd);
        if (CollectionUtils.isEmpty(distinctUserAndProcessCodeSortedByUserName)) {
            log.info("刷新 {} 月份工序扫码阶梯提成结束！", scanCompleteTimeStr);
            return;
        }

        for (ProcessOrderScanPo processOrderScanPo : distinctUserAndProcessCodeSortedByUserName) {
            String processCode = processOrderScanPo.getProcessCode();
            String completeUser = processOrderScanPo.getCompleteUser();

            List<ProcessOrderScanPo> processOrderScanList
                    = processOrderScanDao.getProcessOrderScanList(processCode, completeUser, completeTimeBegin,
                    completeTimeEnd);
            log.info("刷新 {} 月份工序扫码阶梯提成 完成人编号：{} 工序代码:{} 条数:{}", scanCompleteTimeStr,
                    completeUser, processCode, processOrderScanList.size());
            processOrderScanBaseService.createCommissionDetails(processCode, processOrderScanList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCommissionJob(String scanCompleteTimeStr) {
        LocalDateTime completeTimeBegin
                = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);
        LocalDateTime completeTimeEnd
                = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);

        int currentPage = 1;
        while (currentPage <= MAX_ITERATIONS) {
            // 调用分页查询方法
            IPage<ProcessOrderScanPo> processOrderScanPoIPage = processOrderScanDao.listByCompleteTimeWithPage(
                    currentPage, PAGE_SIZE, completeTimeBegin, completeTimeEnd);
            List<ProcessOrderScanPo> processOrderScanPos = processOrderScanPoIPage.getRecords();
            if (CollectionUtils.isEmpty(processOrderScanPos)) {
                log.info("刷新 {} 月份工序扫码阶梯提成结束！", scanCompleteTimeStr);
                return;
            }
            for (ProcessOrderScanPo processOrderScanPo : processOrderScanPos) {
                Long processOrderScanId = processOrderScanPo.getProcessOrderScanId();
                processOrderScanBaseService.updateCommissionDetails(processOrderScanId);
            }

            currentPage++;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProcessOrderScanJob(String scanCompleteTimeStr) {
        LocalDateTime completeTimeBegin
                = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);
        LocalDateTime completeTimeEnd
                = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(scanCompleteTimeStr), TimeZoneId.CN,
                TimeZoneId.UTC);

        List<String> processCodes = processOrderScanDao.listProcessCodesByCompleteTime(completeTimeBegin,
                completeTimeEnd);
        if (CollectionUtils.isEmpty(processCodes)) {
            log.info("更新 {} 月份工序扫码基础单价和额外单价结束！", scanCompleteTimeStr);
            return;
        }

        for (String processCode : processCodes) {
            List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.listByProcessCodeAndCompleteTime(
                    processCode, completeTimeBegin, completeTimeEnd);
            if (CollectionUtils.isEmpty(processCodes)) {
                log.info("更新 {} 月份工序扫码基础单价和额外单价结束！", scanCompleteTimeStr);
                continue;
            }

            ProcessPo processPo = processDao.getByProcessCode(processCode);
            if (Objects.isNull(processCode)) {
                throw new BizException("更新工序扫码记录失败！工序编码：{}对应工序信息不存在!");
            }
            processOrderScanPos.forEach(processOrderScanPo -> {
                processOrderScanPo.setProcessCommission(processPo.getCommission());
                processOrderScanPo.setExtraCommission(processPo.getExtraCommission());
            });
            processOrderScanDao.updateBatchByIdVersion(processOrderScanPos);
        }

    }
}
