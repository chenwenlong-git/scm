package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.ProcessMaterialReceiptStatus;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.vo.ScheduleStatisticsVo;
import com.hete.supply.scm.server.scm.entity.vo.ScheduleTabVo;
import com.hete.supply.scm.server.scm.enums.ProductionProcessStatus;
import com.hete.supply.scm.server.scm.process.builder.ProcessOrderScanBuilder;
import com.hete.supply.scm.server.scm.process.converter.H5ProcessOrderOperationScanConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.process.enums.ProcessStage;
import com.hete.supply.scm.server.scm.process.enums.ProcessingStatus;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderScanBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年08月09日 00:01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class H5ProcessBizService {

    private final ProcessOrderScanBizService processOrderScanBizService;
    private final ProcessProcedureEmployeePlanDao processProcedureEmployeePlanDao;
    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderProcedureDao processOrderProcedureDao;
    private final ProcessOrderScanDao processOrderScanDao;
    private final ProcessDao processDao;
    private final ProcessOrderScanBaseService processOrderScanBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final ProcessMaterialReceiptDao processMaterialReceiptDao;
    private final ProcessOrderItemDao processOrderItemDao;


    /**
     * 获取当前用户加工单工序扫码信息
     *
     * @return
     */
    public List<H5ProcessOrderOperationScanVo> getH5ProcessList() {
        // 获取当前用户
        final String userKey = GlobalContext.getUserKey();
        if (StrUtil.isBlank(userKey)) {
            throw new BizException("当前用户信息为空，请重新登录");
        }
        List<H5ProcessOrderOperationScanVo> h5ProcessList = getH5ProcessList(userKey);
        refreshStatus(h5ProcessList);
        return sortList(h5ProcessList);
    }

    /**
     * @Description 按要求开始时间进行排序
     * @author yanjiawei
     * @Date 2023/8/23 17:07
     */
    public List<H5ProcessOrderOperationScanVo> sortList(List<H5ProcessOrderOperationScanVo> h5ProcessList) {
        if (CollectionUtils.isNotEmpty(h5ProcessList)) {
            return h5ProcessList.stream()
                    .filter(item -> item != null && item.getProcessOrderProcedure() != null)
                    .sorted(Comparator.comparing(item -> item.getProcessOrderProcedure().getEstimatedStartTime()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * @Description 刷新H5工作台加工状态
     * @author yanjiawei
     * @Date 2023/8/23 17:08
     */
    public void refreshStatus(List<H5ProcessOrderOperationScanVo> operationScanVoList) {
        if (operationScanVoList == null) {
            return;
        }

        for (H5ProcessOrderOperationScanVo scanVo : operationScanVoList) {
            if (scanVo != null) {
                if (ProcessingStatus.WAITING.equals(scanVo.getProcessStatus())
                        || ProcessingStatus.AWAITING_RECEIPT.equals(scanVo.getProcessStatus())
                        || ProcessingStatus.AWAITING_START.equals(scanVo.getProcessStatus())) {
                    scanVo.setProductionProcessStatus(ProductionProcessStatus.PENDING);
                } else if (ProcessingStatus.PROCESSING.equals(scanVo.getProcessStatus())) {
                    scanVo.setProductionProcessStatus(ProductionProcessStatus.IN_PROGRESS);
                } else if (ProcessingStatus.COMPLETED.equals(scanVo.getProcessStatus())) {
                    scanVo.setProductionProcessStatus(ProductionProcessStatus.COMPLETED);
                }
            }
        }
    }

    /**
     * 通过员工编号获取工序扫码信息
     *
     * @param userKey
     * @return
     */
    public List<H5ProcessOrderOperationScanVo> getH5ProcessList(String userKey) {
        // 获取当前用户关联的加工单工序id
        Set<Long> relateProcessOrderProcedureIds = this.getSearchCondition(userKey);
        if (CollectionUtils.isEmpty(relateProcessOrderProcedureIds)) {
            return Collections.emptyList();
        }
        return getH5ProcessList(relateProcessOrderProcedureIds);
    }

    /**
     * @Description 通过加工单工序ids获取H5工作台信息
     * @author yanjiawei
     * @Date 2023/8/30 17:14
     */
    public List<H5ProcessOrderOperationScanVo> getH5ProcessList(Set<Long> processOrderProcedureIds) {
        List<H5ProcessOrderOperationScanVo> resultList = Lists.newArrayList();
        // 获取工序排产计划
        List<ProcessProcedureEmployeePlanPo> employeePlanPos
                = processProcedureEmployeePlanDao.getByProcessOrderProcedureIds(processOrderProcedureIds);
        if (CollectionUtils.isEmpty(employeePlanPos)) {
            return Collections.emptyList();
        }

        // 获取加工单信息
        List<ProcessOrderPo> processOrderPos
                = employeePlanPos.isEmpty() ? Collections.emptyList() :
                processOrderDao.getByProcessOrderNos(employeePlanPos.stream().map(ProcessProcedureEmployeePlanPo::getProcessOrderNo)
                        .collect(Collectors.toList()));

        // 根据加工单分组
        Map<String, List<ProcessProcedureEmployeePlanPo>> groupedByProcessOrderNo
                = employeePlanPos.stream().collect(Collectors.groupingBy(ProcessProcedureEmployeePlanPo::getProcessOrderNo));
        groupedByProcessOrderNo.forEach((processOrderNo, employeePlans) -> {
            // 获取加工单扫码工序信息
            ProcessOrderScanByNoDto param = new ProcessOrderScanByNoDto();
            param.setProcessOrderNo(processOrderNo);
            ProcessOrderScanByH5Vo processOrderScanByH5Vo = processOrderScanBizService.getByProcessOrderNo(param);

            ProcessOrderPo matchProcessOrder = processOrderPos.stream()
                    .filter(processOrderPo -> Objects.equals(processOrderNo, processOrderPo.getProcessOrderNo()))
                    .findFirst().orElse(null);

            List<H5ProcessOrderOperationScanVo> h5ProcessOrderOperationScanVos
                    = H5ProcessOrderOperationScanConverter.toVo(processOrderScanByH5Vo, matchProcessOrder, employeePlans);
            if (CollectionUtils.isNotEmpty(h5ProcessOrderOperationScanVos)) {
                resultList.addAll(h5ProcessOrderOperationScanVos);
            }
        });
        return resultList;
    }


    /**
     * @Description 获取查询条件
     * @author yanjiawei
     * @Date 2023/8/23 17:08
     */
    private Set<Long> getSearchCondition(String userKey) {
        if (StrUtil.isBlank(userKey)) {
            return Collections.emptySet();
        }

        LocalDateTime processPlanTime = LocalDateTime.now();
        Set<Long> allProcessOrderProcedureIds = Sets.newHashSet();
        List<ProcessProcedureEmployeePlanPo> processProcedureEmployeePlanPos = processProcedureEmployeePlanDao.getEmployeeProcessPlanByEmployeeNoAndStartTime(
                new HashSet<>(Collections.singletonList(userKey)), LocalDateTime.now());
        if (CollectionUtils.isNotEmpty(processProcedureEmployeePlanPos)) {
            Set<Long> processOrderProcedureIds
                    = processProcedureEmployeePlanPos.stream().map(ProcessProcedureEmployeePlanPo::getProcessOrderProcedureId)
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(processOrderProcedureIds)) {
                allProcessOrderProcedureIds.addAll(processOrderProcedureIds);
            }
        }

        List<ProcessOrderScanPo> scanByUserKeys = processOrderScanDao.getByRelateUserKey(userKey, processPlanTime);
        if (CollectionUtils.isNotEmpty(scanByUserKeys)) {
            Set<Long> processOrderProcedureIds
                    = scanByUserKeys.stream().map(ProcessOrderScanPo::getProcessOrderProcedureId)
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(processOrderProcedureIds)) {
                allProcessOrderProcedureIds.addAll(processOrderProcedureIds);
            }
        }
        return allProcessOrderProcedureIds;
    }

    /**
     * 开始加工
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean beginProcedure(ProcessOrderStartProcessingByH5Dto dto) {
        final String processOrderNo = dto.getProcessOrderNo();
        final Long processOrderProcedureId = dto.getProcessOrderProcedureId();
        final String currentUserKey = GlobalContext.getUserKey();

        // 工序操作环节校验
        processOrderScanBaseService.validateProcessStage(processOrderProcedureId, ProcessStage.PROCESSING);

        if (StrUtil.isBlank(currentUserKey)) {
            throw new ParamIllegalException("请重新登录！");
        }
        ProcessOrderProcedurePo processOrderProcedurePo = processOrderProcedureDao.getById(processOrderProcedureId);
        if (Objects.isNull(processOrderProcedurePo)) {
            throw new ParamIllegalException("加工工序不存在！");
        }
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new ParamIllegalException("加工单不存在！");
        }
        ProcessPo processPo = processDao.getByProcessCode(processOrderProcedurePo.getProcessCode());
        if (Objects.isNull(processPo)) {
            throw new ParamIllegalException("工序不存在！");
        }
        ProcessOrderScanPo processOrderScanPo
                = processOrderScanDao.getByProcessOrderProcedureId(processOrderProcedureId);
        if (Objects.isNull(processOrderScanPo)) {
            throw new ParamIllegalException("加工失败，扫码记录不存在，请刷新");
        }

        processOrderScanPo.setProcessingTime(new DateTime().toLocalDateTime());
        processOrderScanPo.setProcessingUser(GlobalContext.getUserKey());
        processOrderScanPo.setProcessingUsername(GlobalContext.getUsername());
        return processOrderScanDao.updateByIdVersion(processOrderScanPo);
    }


    /**
     * 获取H5选项卡信息。
     *
     * @return 日程表选项卡的信息。
     */
    public ScheduleTabVo getTab() {
        final String userKey = GlobalContext.getUserKey();
        if (StrUtil.isBlank(userKey)) {
            throw new BizException("当前用户信息为空，请重新登录");
        }

        ScheduleTabVo scheduleTabVo = new ScheduleTabVo();
        scheduleTabVo.setEmployeeNo(userKey);
        scheduleTabVo.setEmployeeName(GlobalContext.getUsername());
        List<H5ProcessOrderOperationScanVo> operationScanVos = getH5ProcessList();
        if (CollectionUtils.isEmpty(operationScanVos)) {
            scheduleTabVo.setPlannedCount(0);
            scheduleTabVo.setProcessedCount(0);
            scheduleTabVo.setCommission(BigDecimal.ZERO);
        } else {
            long plannedCount = operationScanVos.stream().map(H5ProcessOrderOperationScanVo::getProcessStatus)
                    .filter(status -> Objects.nonNull(status) && (status == ProcessingStatus.WAITING ||
                            status == ProcessingStatus.AWAITING_RECEIPT || status == ProcessingStatus.AWAITING_START))
                    .count();
            scheduleTabVo.setPlannedCount(plannedCount);

            int processedCount = operationScanVos.stream().mapToInt(H5ProcessOrderOperationScanVo::getProcessNum).sum();
            scheduleTabVo.setProcessedCount(processedCount);

            BigDecimal totalCommission = operationScanVos.stream()
                    .map(H5ProcessOrderOperationScanVo::getCommission)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            scheduleTabVo.setCommission(totalCommission);
        }

        return scheduleTabVo;
    }


    /**
     * 获取H5统计信息。
     *
     * @return 日程统计信息的对象。
     */
    public ScheduleStatisticsVo getScheduleStatistics() {
        final String userKey = GlobalContext.getUserKey();
        if (StrUtil.isBlank(userKey)) {
            throw new BizException("当前用户信息为空，请重新登录");
        }

        ScheduleStatisticsVo scheduleStatisticsVo = new ScheduleStatisticsVo();
        scheduleStatisticsVo.setEmployeeNo(userKey);
        scheduleStatisticsVo.setEmployeeName(GlobalContext.getUsername());

        List<H5ProcessOrderOperationScanVo> operationScanVos = getH5ProcessList();
        if (CollectionUtils.isEmpty(operationScanVos)) {
            scheduleStatisticsVo.setPendingOrders(0);
            scheduleStatisticsVo.setProcessingOrders(0);
            scheduleStatisticsVo.setCompletedOrders(0);
        } else {
            long pendingOrders = operationScanVos.stream().map(H5ProcessOrderOperationScanVo::getProcessStatus)
                    .filter(status -> Objects.nonNull(status) && (status == ProcessingStatus.WAITING ||
                            status == ProcessingStatus.AWAITING_RECEIPT || status == ProcessingStatus.AWAITING_START))
                    .count();
            scheduleStatisticsVo.setPendingOrders(pendingOrders);

            long processingOrders = operationScanVos.stream()
                    .map(H5ProcessOrderOperationScanVo::getProcessStatus)
                    .filter(status -> Objects.nonNull(status) && status == ProcessingStatus.PROCESSING)
                    .count();
            scheduleStatisticsVo.setProcessingOrders(processingOrders);

            long completedOrders = operationScanVos.stream()
                    .map(H5ProcessOrderOperationScanVo::getProcessStatus)
                    .filter(status -> Objects.nonNull(status) && status == ProcessingStatus.COMPLETED)
                    .count();
            scheduleStatisticsVo.setCompletedOrders(completedOrders);
        }
        return scheduleStatisticsVo;
    }

    /**
     * 获取加工订单号获取工作台工序待办明细。
     *
     * @param dto H5 加工订单工序扫描明细的数据传输对象。
     * @return 加工订单工序扫描明细的视图对象。
     */
    public ProcessOrderScanDetailVo getProcessPlanDetail(H5ProcessOrderProcedureScanDetailDto dto) {
        final String processOrderNo = dto.getProcessOrderNo();

        if (!processOrderNo.startsWith(ScmConstant.PROCESS_ORDER_NO_PREFIX)) {
            throw new ParamIllegalException("请扫描业务加工单，扫码内容：" + processOrderNo);
        }
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new ParamIllegalException("加工单信息不存在，请校验后扫码，扫码内容：{}", processOrderNo);
        }

        // 获取加工单扫码详情
        ProcessOrderScanByNoDto queryProcessOrderScanParam = new ProcessOrderScanByNoDto();
        queryProcessOrderScanParam.setProcessOrderNo(processOrderNo);
        ProcessOrderScanByH5Vo processOrderScanByH5Vo = processOrderScanBizService.getByProcessOrderNo(queryProcessOrderScanParam);
        if (Objects.isNull(processOrderScanByH5Vo)) {
            return null;
        }

        // 获取工序关联的排产计划
        List<ProcessOrderProcedureByH5Vo> procedures
                = Optional.ofNullable(processOrderScanByH5Vo.getProcessOrderProcedures())
                .orElse(Collections.emptyList());
        Set<Long> processOrderProcedureIds
                = procedures.stream().map(ProcessOrderProcedureByH5Vo::getProcessOrderProcedureId)
                .collect(Collectors.toSet());
        List<ProcessProcedureEmployeePlanPo> employeePlanPos
                = processOrderProcedureIds.isEmpty() ? Collections.emptyList() : processOrderProcedureIds.stream()
                .map(processOrderProcedureId -> processProcedureEmployeePlanDao.getByProcessOrderProcedureIds(Collections.singleton(processOrderProcedureId)))
                .flatMap(Collection::stream).collect(Collectors.toList());

        // 是否回料
        List<ProcessDeliveryOrderVo>
                processDeliveryOrderVoList
                = wmsRemoteService.getProcessDeliveryOrder(processOrderNo, WmsEnum.DeliveryType.PROCESS);
        List<ProcessMaterialReceiptPo> processMaterialReceiptPoList
                = processMaterialReceiptDao.getByProcessOrderNo(processOrderNo);

        int pendingDeliveryCount = (int) processDeliveryOrderVoList.stream()
                .filter(it -> !it.getDeliveryState().equals(WmsEnum.DeliveryState.SIGNED_OFF)
                        && !it.getDeliveryState().equals(WmsEnum.DeliveryState.FINISHED)
                        && !it.getDeliveryState().equals(WmsEnum.DeliveryState.CANCELING)
                        && !it.getDeliveryState().equals(WmsEnum.DeliveryState.CANCELED))
                .count();
        int waitReceiptCount = (int) processMaterialReceiptPoList.stream()
                .filter(it -> ProcessMaterialReceiptStatus.WAIT_RECEIVE.equals(it.getProcessMaterialReceiptStatus()))
                .count();
        if (pendingDeliveryCount == 0 && waitReceiptCount == 0) {
            processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.TRUE);
        }
        return H5ProcessOrderOperationScanConverter.toDetailVo(processOrderScanByH5Vo, processOrderPo, employeePlanPos);
    }

    public List<H5ProcessOrderOperationScanVo> getH5ProcessListByUserKey(H5ProcessInfoDto h5ProcessInfoDto) {
        String userKey = h5ProcessInfoDto.getUserKey();
        if (StrUtil.isBlank(userKey)) {
            throw new ParamIllegalException("无法通过用户编号获取工序排产信息，userKey is null");
        }
        List<H5ProcessOrderOperationScanVo> h5ProcessList = getH5ProcessList(userKey);
        refreshStatus(h5ProcessList);
        return sortList(h5ProcessList);
    }

    public CommonPageResult.PageInfo<H5WorkbenchVo> getWorkbenchPage(H5WorkbenchPageDto dto) {
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");
        IPage<H5WorkbenchVo> pageResult = processOrderScanDao.selectWorkbenchPage(curUserKey, dto);
        List<H5WorkbenchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PageInfoUtil.getPageInfo(pageResult);
        }

        List<Long> processOrderScanIdList = records.stream()
                .map(H5WorkbenchVo::getProcessOrderScanId)
                .collect(Collectors.toList());
        List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.getByIds(processOrderScanIdList);

        Set<String> processOrderNoList = processOrderScanPos.stream()
                .map(ProcessOrderScanPo::getProcessOrderNo)
                .collect(Collectors.toSet());
        List<ProcessOrderPo> processOrderPoList = processOrderDao.getByProcessOrderNos(processOrderNoList);

        List<ProcessOrderItemPo> processOrderItemPoList
                = processOrderItemDao.getByProcessOrderNos(processOrderNoList);

        Set<String> processCodeList = processOrderScanPos.stream()
                .map(ProcessOrderScanPo::getProcessCode)
                .collect(Collectors.toSet());
        List<ProcessPo> processPoList = processDao.getByProcessCodes(processCodeList);

        for (H5WorkbenchVo record : records) {
            Long processOrderScanId = record.getProcessOrderScanId();

            ProcessOrderScanPo matchProcessOrderScan = processOrderScanPos.stream()
                    .filter(it -> Objects.equals(it.getProcessOrderScanId(), processOrderScanId))
                    .findFirst().orElse(null);
            if (Objects.isNull(matchProcessOrderScan)) {
                continue;
            }

            // 工序信息
            String processCode = matchProcessOrderScan.getProcessCode();
            ProcessPo matchProcess = processPoList.stream()
                    .filter(processPo -> Objects.equals(processPo.getProcessCode(), processCode))
                    .findFirst().orElse(null);

            H5WorkbenchVo.H5ScanWorkbenchVo h5ScanWorkbenchVo
                    = ProcessOrderScanBuilder.buildH5ScanWorkbenchVo(matchProcessOrderScan, matchProcess);
            record.setH5ProcedureScanWorkbench(h5ScanWorkbenchVo);

            // 加工单
            String processOrderNo = matchProcessOrderScan.getProcessOrderNo();
            ProcessOrderPo matchProc = processOrderPoList.stream()
                    .filter(processOrderPo -> Objects.equals(processOrderPo.getProcessOrderNo(), processOrderNo))
                    .findFirst().orElse(null);
            // 加工单明细
            ProcessOrderItemPo matchPoi = processOrderItemPoList.stream()
                    .filter(processOrderItemPo -> Objects.equals(processOrderItemPo.getProcessOrderNo(), processOrderNo))
                    .findFirst().orElse(null);

            H5WorkbenchVo.H5ProcessOrderWorkbenchVo h5ProcessOrderWorkbenchVo
                    = ProcessOrderScanBuilder.buildH5ProcessOrderWorkbenchVo(matchProc, matchPoi);
            record.setH5ProcessOrderWorkbench(h5ProcessOrderWorkbenchVo);
        }

        return PageInfoUtil.getPageInfo(pageResult);
    }
}












