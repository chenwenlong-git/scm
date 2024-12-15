package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.hete.supply.scm.api.scm.entity.enums.OverPlan;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessPlanDelay;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.converter.EmployeeProcessAbilityConverter;
import com.hete.supply.scm.server.scm.entity.bo.OrdersToProcessPlanParametersBo;
import com.hete.supply.scm.server.scm.entity.bo.ProductionPoolConfigBo;
import com.hete.supply.scm.server.scm.entity.dto.UpdateProcessPlanDto;
import com.hete.supply.scm.server.scm.entity.dto.UpdateProcessProcedureEmployeePlanDto;
import com.hete.supply.scm.server.scm.entity.po.EmployeeProcessAbilityPo;
import com.hete.supply.scm.server.scm.enums.EndDelayStatus;
import com.hete.supply.scm.server.scm.enums.ProductionPlanReceivingStatus;
import com.hete.supply.scm.server.scm.enums.StartDelayStatus;
import com.hete.supply.scm.server.scm.process.converter.ProcessProcedureEmployeePlanConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderPlanInfoVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderPlanVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessProcedureEmployeePlanVo;
import com.hete.supply.scm.server.scm.process.enums.ProcessOrderValidationErrorMessage;
import com.hete.supply.scm.server.scm.process.enums.ProcessPlanStrategy;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.supply.scm.server.scm.service.base.ProductionPoolBaseService;
import com.hete.supply.scm.server.scm.service.base.StringBuilderLoggerBaseService;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年07月27日 13:50
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProcessPlanBizService {
    private final ProcessPlanBaseService processPlanBaseService;
    private final ProcessOrderDao processOrderDao;
    private final ProcessProcedureEmployeePlanDao processProcedureEmployeePlanDao;
    private final ProcessOrderProcedureDao processOrderProcedureDao;
    private final ProcessOrderBaseService processOrderBaseService;
    private final ProcessOrderItemDao processOrderItemDao;
    private final EmployeeProcessAbilityDao employeeProcessAbilityDao;
    private final ProcessDao processDao;
    private final ProductionPoolBaseService productionPoolBaseService;
    private final CapacityPoolBaseService capacityPoolBaseService;
    private final ProcessOrderScanDao processOrderScanDao;
    private final StringBuilderLoggerBaseService stringBuilderLoggerBaseService;
    private final IdGenerateService idGenerateService;
    private final static int PRODUCTION_DATE_OFFSET = 3;

    /**
     * @Description 排产池分页搜索
     * @author yanjiawei
     * @Date 15:15 2023/8/23
     */
    public CommonPageResult.PageInfo<ProcessOrderPlanVo> getByPage(ProcessOrderPlanQueryDto processOrderPlanQueryDto) {
        final ProcessOrderStatus processOrderStatus = processOrderPlanQueryDto.getProcessOrderStatus();
        ProcessOrderPlanQueryBo processOrderPlanQueryBo = this.getProcessOrderNosByCondition(processOrderPlanQueryDto);
        if (processOrderPlanQueryBo.getIsEmpty()) {
            return new CommonPageResult.PageInfo<>();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeThreeDay = now.minus(PRODUCTION_DATE_OFFSET, ChronoUnit.DAYS);
        processOrderPlanQueryDto.setSystemProcessPlanLatestExpectEndTimeStart(beforeThreeDay);
        processOrderPlanQueryDto.setSystemProcessPlanLatestExpectEndTimeEnd(now);

        CommonPageResult.PageInfo<ProcessOrderPlanVo> result
                = processProcedureEmployeePlanDao.getByPage(PageDTO.of(processOrderPlanQueryDto.getPageNo(),
                processOrderPlanQueryDto.getPageSize()), processOrderPlanQueryDto, processOrderPlanQueryBo);
        List<ProcessOrderPlanVo> records = result.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return result;
        }

        // 获取工序复杂系数
        List<String> processOrderNos = records.stream().map(ProcessOrderPlanVo::getProcessOrderNo).collect(Collectors.toList());
        List<ProcessProcedureComplexCoefficientBo> processProcedureComplexCoefficientBos
                = Optional.of(processOrderProcedureDao.getProcessProcedureCapacityNumBo(processOrderNos)).orElse(Collections.emptyList());

        records.forEach(record -> {
            final String processOrderNo = record.getProcessOrderNo();
            Integer totalProcessNum = Objects.isNull(record.getTotalProcessNum()) ? 0 : record.getTotalProcessNum();
            ProcessProcedureComplexCoefficientBo match = processProcedureComplexCoefficientBos.stream()
                    .filter(processProcedureComplexCoefficientBo -> Objects.equals(processOrderNo, processProcedureComplexCoefficientBo.getProcessOrderNo()))
                    .findFirst().orElse(null);
            if (Objects.nonNull(match)) {
                Integer complexCoefficient = match.getComplexCoefficient();
                record.setCapacityNum(totalProcessNum * complexCoefficient);
            }

            // 设置业务时间
            if (Objects.equals(ProcessOrderStatus.WAIT_PLAN, processOrderStatus)) {
                record.setBusinessTime(record.getCreateTime());
            }
            if (Objects.equals(ProcessOrderStatus.WAIT_PRODUCE, processOrderStatus) || Objects.equals(ProcessOrderStatus.PROCESSING, processOrderStatus)) {
                LocalDateTime processPlanTime = record.getProcessPlanTime();
                record.setProcessPlanTimeOverTwentyFour(Objects.nonNull(processPlanTime) && Math.abs(ChronoUnit.HOURS.between(processPlanTime, now)) > 24);
                record.setBusinessTime(record.getProcessPlanEarliestExpectBeginTime());
            }
            if (Objects.equals(ProcessOrderStatus.WAIT_MOVING, processOrderStatus)) {
                record.setBusinessTime(record.getProcessCompletionTime());
            }
        });

        result.setRecords(records.stream().sorted(Comparator.comparing(ProcessOrderPlanVo::getBusinessTime).reversed()).collect(Collectors.toList()));
        return result;
    }

    /**
     * @Description 获取分页条件
     * @author yanjiawei
     * @Date 15:15 2023/8/23
     */
    private ProcessOrderPlanQueryBo getProcessOrderNosByCondition(ProcessOrderPlanQueryDto dto) {
        ProcessOrderPlanQueryBo processOrderPlanQueryBo = new ProcessOrderPlanQueryBo();
        final List<String> skus = dto.getSkus();
        final List<String> materialSkuList = dto.getMaterialSkuList();

        List<String> processOrderNosBySku = processOrderBaseService.getProcessOrderNosBySkus(skus);
        processOrderPlanQueryBo.setProcessOrderNosBySku(processOrderNosBySku);

        List<String> processOrderNosByMaterialSku = processOrderBaseService.getProcessOrderNosByMaterialSkus(materialSkuList);
        processOrderPlanQueryBo.setProcessOrderNosByMaterialSku(processOrderNosByMaterialSku);

        boolean isEmpty = CollectionUtils.isNotEmpty(skus) && CollectionUtils.isEmpty(processOrderNosBySku);
        if (CollectionUtils.isNotEmpty(materialSkuList) && CollectionUtils.isEmpty(processOrderNosByMaterialSku)) {
            isEmpty = true;
        }
        processOrderPlanQueryBo.setIsEmpty(isEmpty);
        return processOrderPlanQueryBo;
    }

    /**
     * @Description 获取排产计划详情
     * @author yanjiawei
     * @Date 15:16 2023/8/23
     */
    public ProcessOrderPlanInfoVo info(ProcessOrderPlanQueryInfoDto dto) {
        final String processOrderNo = dto.getProcessOrderNo();

        // 获取加工单 & 加工单明细 & 加工单工序 & 工序信息 & 排产计划
        processOrderBaseService.validateProcessOrderExists(processOrderNo, ProcessOrderValidationErrorMessage.PROCESS_ORDER_NOT_FOUND_PLEASE_REFRESH);
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);

        List<ProcessOrderProcedurePo> processOrderProcedurePos = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isEmpty(processOrderProcedurePos)) {
            throw new BizException("手动排产失败，加工单工序信息不存在！");
        }
        final List<Long> processIds = processOrderProcedurePos.stream().map(ProcessOrderProcedurePo::getProcessId).collect(Collectors.toList());
        final List<Long> processOrderProcedureIds = processOrderProcedurePos.stream().map(ProcessOrderProcedurePo::getProcessOrderProcedureId).collect(Collectors.toList());
        List<ProcessPo> processPos = processDao.getByProcessIds(processIds);
        List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.getByProcessOrderProcedureIds(processOrderProcedureIds);

        List<EmployeeLevelBo> employeeLevelBos = Lists.newArrayList();
        List<ProcessProcedureEmployeePlanPo> processProcedureEmployeePlans = processProcedureEmployeePlanDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(processProcedureEmployeePlans)) {
            Set<String> employeeNos = processProcedureEmployeePlans.stream().map(ProcessProcedureEmployeePlanPo::getEmployeeNo).collect(Collectors.toSet());
            employeeLevelBos = Optional.of(processPlanBaseService.getEmployeeLevelBos(employeeNos)).orElse(Collections.emptyList());
        }

        // 排产计划详情实体
        ProcessOrderPlanInfoVo processOrderPlanInfoVo = new ProcessOrderPlanInfoVo();
        processOrderPlanInfoVo.setProcessOrderNo(processOrderPo.getProcessOrderNo());

        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(processOrderNo);
        ProcessOrderItemPo processOrderItemPo = processOrderItemPos.stream().findFirst().orElse(null);
        if (Objects.nonNull(processOrderItemPo)) {
            processOrderPlanInfoVo.setSku(processOrderItemPo.getSku());
        }

        processOrderPlanInfoVo.setProcessOrderType(processOrderPo.getProcessOrderType());
        processOrderPlanInfoVo.setDeliverDate(processOrderPo.getDeliverDate());
        processOrderPlanInfoVo.setTotalProcessNum(processOrderPo.getTotalProcessNum());
        processOrderPlanInfoVo.setIsReceiveMaterial(processOrderPo.getIsReceiveMaterial());

        // 实体里面的详细计划
        List<EmployeeLevelBo> finalEmployeeLevelBos = employeeLevelBos;
        List<ProcessProcedureEmployeePlanVo> processProcedureEmployeePlanVos = processOrderProcedurePos.stream().map(processOrderProcedurePo -> {
            ProcessProcedureEmployeePlanVo processProcedureEmployeePlanVo = new ProcessProcedureEmployeePlanVo();
            processProcedureEmployeePlanVo.setProcessOrderProcedureId(processOrderProcedurePo.getProcessOrderProcedureId());

            // 工序信息
            final Long processId = processOrderProcedurePo.getProcessId();
            final Long processOrderProcedureId = processOrderProcedurePo.getProcessOrderProcedureId();
            ProcessPo matchProcess = processPos.stream().filter(processPo -> Objects.equals(processId, processPo.getProcessId())).findFirst().orElse(null);
            if (Objects.nonNull(matchProcess)) {
                processProcedureEmployeePlanVo.setProcessId(matchProcess.getProcessId());
                processProcedureEmployeePlanVo.setProcessSecondName(matchProcess.getProcessSecondName());
            }

            // 扫码记录
            ProcessOrderScanPo matchScan
                    = processOrderScanPos.stream().filter(scan -> Objects.equals(processOrderProcedureId, scan.getProcessOrderProcedureId())).findFirst().orElse(null);
            if (Objects.isNull(matchScan)) {
                processProcedureEmployeePlanVo.setProductionPlanReceivingStatus(ProductionPlanReceivingStatus.NOT_RECEIVED);
            } else {
                if (StrUtil.isNotBlank(matchScan.getReceiptUser())) {
                    processProcedureEmployeePlanVo.setProductionPlanReceivingStatus(ProductionPlanReceivingStatus.RECEIVED);
                } else {
                    processProcedureEmployeePlanVo.setProductionPlanReceivingStatus(ProductionPlanReceivingStatus.NOT_RECEIVED);
                }
            }

            // 排产计划
            ProcessProcedureEmployeePlanPo matchPlan = processProcedureEmployeePlans.stream()
                    .filter(plan -> Objects.equals(processOrderProcedurePo.getProcessOrderProcedureId(), plan.getProcessOrderProcedureId())).findFirst().orElse(null);
            if (Objects.nonNull(matchPlan)) {
                final String employeeNo = matchPlan.getEmployeeNo();

                processProcedureEmployeePlanVo.setProcessProcedureEmployeePlanId(matchPlan.getProcessProcedureEmployeePlanId());
                processProcedureEmployeePlanVo.setEmployeeNo(employeeNo);
                processProcedureEmployeePlanVo.setEmployeeName(matchPlan.getEmployeeName());
                processProcedureEmployeePlanVo.setExpectBeginDateTime(matchPlan.getExpectBeginTime());
                processProcedureEmployeePlanVo.setExpectEndDateTime(matchPlan.getExpectEndTime());

                // 人员等级
                EmployeeLevelBo matchEmployeeLevelBo = finalEmployeeLevelBos.stream()
                        .filter(employeeLevelBo -> Objects.equals(employeeNo, employeeLevelBo.getEmployeeNo())).findFirst().orElse(null);
                if (Objects.nonNull(matchEmployeeLevelBo)) {
                    processProcedureEmployeePlanVo.setGradeType(matchEmployeeLevelBo.getGradeType());
                    processProcedureEmployeePlanVo.setGradeName(matchEmployeeLevelBo.getGradeName());
                }
                processOrderPlanInfoVo.setProcessPlanTime(matchPlan.getExpectBeginTime().withHour(0).withMinute(0).withNano(0));
            }
            return processProcedureEmployeePlanVo;
        }).collect(Collectors.toList());
        processOrderPlanInfoVo.setProcessProcedureEmployeePlanVos(processProcedureEmployeePlanVos);

        return processOrderPlanInfoVo;
    }

    /**
     * @Description 手动排产
     * @author yanjiawei
     * @Date 15:21 2023/8/23
     */
    @Transactional(rollbackFor = Exception.class)
    public void doProcessPlan(DoProcessPlanDto dto) {
        final String processOrderNo = dto.getProcessOrderNo();
        processOrderBaseService.validateProcessOrderExists(processOrderNo, ProcessOrderValidationErrorMessage.PROCESS_ORDER_NOT_FOUND_PLEASE_REFRESH);
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);

        final ProcessOrderType processOrderType = dto.getProcessOrderType();
        final List<ProcessProcedureEmployeePlanDto> processProcedureEmployeePlanDtoList = dto.getProcessProcedureEmployeePlanDtoList();
        final Integer totalProcessNum = processOrderPo.getTotalProcessNum();

        // 获取产能池编号
        ProductionPoolConfigBo productionPoolConfig = capacityPoolBaseService.getProductionPoolConfig(processOrderType);
        final String productionPoolCode = Objects.isNull(productionPoolConfig) ? "" : productionPoolConfig.getProductionPoolCode();

        List<ProcessProcedureEmployeePlanPo> processProcedureEmployeePlanPos = ProcessProcedureEmployeePlanConverter.dtoToPos(processProcedureEmployeePlanDtoList);
        if (CollectionUtils.isNotEmpty(processProcedureEmployeePlanPos)) {
            processProcedureEmployeePlanPos.forEach(processProcedureEmployeePlanPo -> {
                processProcedureEmployeePlanPo.setProcessOrderNo(processOrderNo);
                processProcedureEmployeePlanPo.setProcessNum(totalProcessNum);
                processProcedureEmployeePlanPo.setProductionPoolCode(productionPoolCode);
            });
            List<ProcessProcedureEmployeePlanBo> processProcedureEmployeePlanBos = ProcessProcedureEmployeePlanConverter.toBos(processProcedureEmployeePlanPos);
            ProcessPlanAfterBo processPlanAfterBo = new ProcessPlanAfterBo(Collections.emptyList(), processProcedureEmployeePlanBos, processOrderPo);
            processPlanBaseService.doAfterProcessPlan(processPlanAfterBo);
        }
    }

    /**
     * @Description 取消排产
     * @author yanjiawei
     * @Date 2023/8/23 15:22
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelProcessPlan(CancelProcessPlanDto dto) {
        final String processOrderNo = dto.getProcessOrderNo();
        List<ProcessProcedureEmployeePlanPo> delPlanPos = processProcedureEmployeePlanDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isEmpty(delPlanPos)) {
            throw new ParamIllegalException("数据已被删除，请刷新页面!");
        }
        processPlanBaseService.cancelProcessPlan(new CancelProcessPlanBo(processOrderNo));
    }

    /**
     * @Description 编辑排产
     * @author yanjiawei
     * @Date 2023/8/23 15:23
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessPlan(UpdateProcessPlanDto dtoList) {
        // 需要更新的排产计划
        List<UpdateProcessProcedureEmployeePlanDto> needUpdatePlans = dtoList.getProcessProcedureEmployeePlanDtoList();

        // 已存在的排产计划
        List<Long> needUpdatePlanIds = needUpdatePlans.stream().map(UpdateProcessProcedureEmployeePlanDto::getProcessProcedureEmployeePlanId).collect(Collectors.toList());
        List<ProcessProcedureEmployeePlanPo> existPlans = processProcedureEmployeePlanDao.getByIds(needUpdatePlanIds);
        if (CollectionUtils.isEmpty(needUpdatePlans)) {
            throw new ParamIllegalException("数据已被删除，请刷新页面");
        }
        if (needUpdatePlans.size() != existPlans.size()) {
            throw new ParamIllegalException("数据已被更新，请刷新页面后重试");
        }
        ProcessProcedureEmployeePlanPo processProcedureEmployeePlanPo = existPlans.stream().findFirst().orElse(null);
        if (Objects.isNull(processProcedureEmployeePlanPo)) {
            throw new ParamIllegalException("数据已被更新，请刷新页面后重试");
        }
        final String processOrderNo = processProcedureEmployeePlanPo.getProcessOrderNo();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new ParamIllegalException("数据已被更新，请刷新页面后重试");
        }

        for (ProcessProcedureEmployeePlanPo existPlan : existPlans) {
            final Long processProcedureEmployeePlanId = existPlan.getProcessProcedureEmployeePlanId();
            final String employeeNo = existPlan.getEmployeeNo();
            final LocalDateTime expectBeginTime = existPlan.getExpectBeginTime();
            final LocalDateTime expectEndTime = existPlan.getExpectEndTime();
            UpdateProcessProcedureEmployeePlanDto matchNeedUpdate = needUpdatePlans.stream()
                    .filter(needUpdatePlan -> Objects.equals(processProcedureEmployeePlanId, needUpdatePlan.getProcessProcedureEmployeePlanId()))
                    .findFirst().orElse(null);
            if (Objects.nonNull(matchNeedUpdate)) {
                if (!Objects.equals(employeeNo, matchNeedUpdate.getEmployeeNo())) {
                    existPlan.setEmployeeNo(matchNeedUpdate.getEmployeeNo());
                    existPlan.setEmployeeName(matchNeedUpdate.getEmployeeName());
                }
                if (!Objects.equals(expectBeginTime, matchNeedUpdate.getExpectBeginDateTime())) {
                    existPlan.setExpectBeginTime(matchNeedUpdate.getExpectBeginDateTime());
                }
                if (!Objects.equals(expectEndTime, matchNeedUpdate.getExpectEndDateTime())) {
                    existPlan.setExpectEndTime(matchNeedUpdate.getExpectEndDateTime());
                }
            }
        }
        processProcedureEmployeePlanDao.updateBatchByIdVersion(existPlans);
        List<ProcessProcedureEmployeePlanBo> processProcedureEmployeePlanBos = ProcessProcedureEmployeePlanConverter.toBos(existPlans);
        processOrderPo = processOrderBaseService.refreshProcessOrderTime(processOrderPo, processProcedureEmployeePlanBos);
        processOrderDao.updateByIdVersion(processOrderPo);
    }

    /**
     * @Description 获取排产计划
     * @author yanjiawei
     * @Date 2023/8/23 15:23
     */
    public List<ProcessProcedureEmployeePlanVo> getProcessProcedureEmployeePlan(EmployeeScheduleDto employeeScheduleDto) {
        final List<GetProcessProcedureEmployeePlanDto> dtoList = employeeScheduleDto.getDtoList();
        List<ProcessProcedureEmployeePlanPo> employeePlanPos
                = Optional.ofNullable(processProcedureEmployeePlanDao.getProcessProcedureEmployeePlanPos(dtoList)).orElse(Collections.emptyList());
        return ProcessProcedureEmployeePlanConverter.posToVos(employeePlanPos);
    }

    /**
     * @Description 校验订单是否延期
     * @author yanjiawei
     * @Date 2023/8/23 15:26
     */
    @Transactional(rollbackFor = Exception.class)
    public void processPlanDelayCheckTask() {
        LocalDate today = LocalDate.now();
        List<ProcessProcedureEmployeePlanPo> checkPlans = processProcedureEmployeePlanDao.getCheckPlans(today);
        if (CollectionUtils.isEmpty(checkPlans)) {
            return;
        }

        Map<String, List<ProcessProcedureEmployeePlanPo>> groupedByProcessOrderNo =
                checkPlans.stream().collect(Collectors.groupingBy(ProcessProcedureEmployeePlanPo::getProcessOrderNo));
        log.info("筛选排产计划实际开始时间和实际结束时间为空的加工单，条数：{}", groupedByProcessOrderNo.size());

        groupedByProcessOrderNo.forEach((processOrderNo, employeePlans) -> {
            for (ProcessProcedureEmployeePlanPo checkPlan : employeePlans) {
                final Long processProcedureEmployeePlanId = checkPlan.getProcessProcedureEmployeePlanId();
                final LocalDateTime expectBeginTime = checkPlan.getExpectBeginTime();
                final LocalDateTime expectEndTime = checkPlan.getExpectEndTime();
                final LocalDateTime now = LocalDateTime.now();

                if (Objects.isNull(expectBeginTime) || Objects.isNull(expectEndTime)) {
                    log.error("筛选排产计划实际开始时间和实际结束时间为空的加工单异常，存在空的预计开始时间和结束时间，" +
                            "processProcedureEmployeePlanId:{}", processProcedureEmployeePlanId);
                    continue;
                }

                // 校验实际开始时间和实际结束时间是否延期
                StartDelayStatus startDelayStatus = ScmTimeUtil.checkStartDelay(null, expectBeginTime);
                EndDelayStatus endDelayStatus = ScmTimeUtil.checkEndDelay(null, expectEndTime);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedStartTime = expectBeginTime.format(formatter);
                String formattedEndTime = expectEndTime.format(formatter);
                String formattedNowTime = now.format(formatter);

                // 当前加工单下任意一个计划延期，则更新订单是否延期=true，同时判断下一个加工单
                if (StartDelayStatus.DELAYED.equals(startDelayStatus) || EndDelayStatus.DELAYED.equals(endDelayStatus)) {
                    ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
                    if (Objects.nonNull(processOrderPo)) {
                        processOrderPo.setProcessPlanDelay(ProcessPlanDelay.TRUE);
                        processOrderDao.updateByIdVersion(processOrderPo);
                        log.info("更新排产订单是否延期：{}，加工单号：{}，当前时间：{}，预计开始时间：{}，预计结束时间：{}",
                                ProcessPlanDelay.TRUE, processOrderNo, formattedNowTime, formattedStartTime, formattedEndTime);
                        break;
                    }
                }
            }
        });
    }


    /**
     * @Description 自动排产定时任务
     * @author yanjiawei
     * @Date 2023/8/23 15:28
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeProductionPlanTask() {
        TreeSet<String> productionPoolCodes = productionPoolBaseService.getProductionPoolCodes();
        if (CollectionUtils.isEmpty(productionPoolCodes)) {
            throw new BizException("初始化排产计划产能池失败！请配置产能池");
        }

        for (String productionPoolCode : productionPoolCodes) {
            final ProductionPoolConfigBo productionPoolConfig = capacityPoolBaseService.getProductionPoolConfig(productionPoolCode);
            if (Objects.isNull(productionPoolConfig)) {
                throw new BizException("获取产能池失败！请配置，产能池编号：[{}]", productionPoolCode);
            }
            // 初始化产能池
            SpringUtil.getBean(this.getClass()).initializeProductionCapacityPool(productionPoolConfig);
            // 执行排产计划
            SpringUtil.getBean(this.getClass()).doProcessPlan(productionPoolConfig);
        }
    }


    /**
     * 初始化排产计划产能池
     */
    public void initializeProductionCapacityPool(@NotNull ProductionPoolConfigBo productionPoolConfig) {
        final String productionPoolCode = productionPoolConfig.getProductionPoolCode();
        final Set<String> employeeNos = productionPoolConfig.getEmployeeNos();
        if (CollectionUtils.isEmpty(employeeNos)) {
            throw new BizException("产能池员工未配置，产能池编号：[{}]", productionPoolCode);
        }

        final TreeSet<LocalDate> processPlanDates = processPlanBaseService.getProcessPlanDate(productionPoolConfig);
        List<EmployeeProcessAbilityPo> delAbilityList = employeeProcessAbilityDao.getEmployeeProcessAbilityBoList(productionPoolCode, processPlanDates);
        if (CollectionUtils.isNotEmpty(delAbilityList)) {
            delAbilityList.forEach(employeeProcessAbilityDao::removeById);
        }

        for (LocalDate processPlanDate : processPlanDates) {
            List<EmployeeProcessAbilityPo> newCapacityPools = processPlanBaseService.buildProductionSchedulePool(employeeNos);
            // 回写产能池编号 & 产能池有效期
            newCapacityPools.forEach(newCapacityPool -> {
                newCapacityPool.setProductionPoolCode(productionPoolCode);
                newCapacityPool.setValidityTime(processPlanDate.atStartOfDay());
            });
            if (CollectionUtils.isNotEmpty(newCapacityPools)) {
                employeeProcessAbilityDao.insertBatch(newCapacityPools);
            }
        }

        // 根据已排产计划预占产能
        List<ProcessProcedureEmployeePlanPo> existPlans = processProcedureEmployeePlanDao.getProcessProcedureEmployeePlanPos(productionPoolCode, processPlanDates);
        if (CollectionUtils.isEmpty(existPlans)) {
            return;
        }

        final Set<Long> processIds = existPlans.stream().map(ProcessProcedureEmployeePlanPo::getProcessId).collect(Collectors.toSet());
        ProcessPlanRelateQueryParamsBo processPlanRelateQueryParamsBo = new ProcessPlanRelateQueryParamsBo();
        processPlanRelateQueryParamsBo.setProcessIds(processIds);
        processPlanRelateQueryParamsBo.setProductionPoolCode(productionPoolCode);
        processPlanRelateQueryParamsBo.setProcessPlanDates(processPlanDates);
        ProcessPlanRelateBo processPlanRelateBo = processPlanBaseService.getProcessOrderRelateProcessPlanInfoBo(processPlanRelateQueryParamsBo);
        List<EmployeeProcessAbilityBo> productionCapacityPool = processPlanRelateBo.getEmployeeProcessAbilityBos();

        List<ProcessProcedureEmployeePlanBo> processProcedureEmployeePlanBos = ProcessProcedureEmployeePlanConverter.toBos(existPlans);
        for (ProcessProcedureEmployeePlanBo existPlan : processProcedureEmployeePlanBos) {
            productionCapacityPool = processPlanBaseService.deductCapacity(existPlan, productionPoolConfig, productionCapacityPool, new StringBuilder());
        }
        employeeProcessAbilityDao.updateBatchByIdVersion(EmployeeProcessAbilityConverter.convertToPo(productionCapacityPool));
    }

    /**
     * @Description 执行排产计划
     * @author yanjiawei
     * @Date 2023/8/23 15:29
     */
    public void doProcessPlan(ProductionPoolConfigBo productionPoolConfig) {
        final String productionPoolCode = productionPoolConfig.getProductionPoolCode();
        final Long maxProductionOrder = productionPoolConfig.getMaxProductionOrders();
        AtomicLong maxProductionOrders = new AtomicLong(maxProductionOrder);

        // 配置校验 & 最大单量校验
        final TreeSet<LocalDate> processPlanDates = processPlanBaseService.getProcessPlanDate(productionPoolConfig);
        if (CollectionUtils.isEmpty(processPlanDates)) {
            throw new BizException("自动排产失败！无法生成排产时间，请检查排产周期！");
        }
        final Set<ProcessOrderType> orderTypes = productionPoolConfig.getOrderTypes();
        if (CollectionUtils.isEmpty(orderTypes)) {
            throw new BizException("自动排产失败！请检查配置订单类型，产能池编号：[{}]", productionPoolCode);
        }
        if (insufficientAvailableProductionCapacity(maxProductionOrders)) {
            log.info("自动排产结束！今天已达到最大单量：{}", maxProductionOrder);
            return;
        }

        // 根据超额/非超额执行排产计划
        final List<OverPlan> overPlans = Arrays.asList(OverPlan.TRUE, OverPlan.FALSE);
        for (OverPlan overPlan : overPlans) {
            if (insufficientAvailableProductionCapacity(maxProductionOrders)) {
                log.info("自动排产结束！今天已达到最大单量：{}", maxProductionOrder);
                return;
            }

            OrdersToProcessPlanParametersBo ordersToProcessPlanParametersBo = new OrdersToProcessPlanParametersBo();
            ordersToProcessPlanParametersBo.setOverPlan(overPlan);
            ordersToProcessPlanParametersBo.setProcessOrderTypes(orderTypes);
            ordersToProcessPlanParametersBo.setProcessOrderStatus(ProcessOrderStatus.WAIT_PLAN);
            ordersToProcessPlanParametersBo.setProcessStatus(ProcessStatus.ENABLED);
            List<ProcessPlanOrderBo> ordersToProcessPlan = processPlanBaseService.getOrdersToProcessPlan(ordersToProcessPlanParametersBo);
            if (CollectionUtils.isEmpty(ordersToProcessPlan)) {
                continue;
            }
            final String ordersBizTrace
                    = idGenerateService.getConfuseCode(ScmConstant.FILTERED_PRODUCTION_SCHEDULE_LOG_PREFIX, TimeType.CN_DAY, ConfuseLength.L_4);
            processPlanBaseService.logProcessPlanOrders(productionPoolCode, overPlan, orderTypes, ordersToProcessPlan, ordersBizTrace);

            for (ProcessPlanOrderBo processPlanOrderBo : ordersToProcessPlan) {
                if (insufficientAvailableProductionCapacity(maxProductionOrders)) {
                    log.info("自动排产结束！今天已达到最大单量：{}", maxProductionOrder);
                    break;
                }

                final String processOrderNo = processPlanOrderBo.getProcessOrderNo();
                final String processOrderBizTrace
                        = idGenerateService.getConfuseCode(ScmConstant.PRODUCTION_SCHEDULE_LOG_PREFIX, TimeType.CN_DAY, ConfuseLength.L_4);
                StringBuilder processOrderLogs = new StringBuilder();
                processPlanBaseService.appendStartProcessPlanLog(processOrderLogs, processOrderNo, "常规");

                // 执行常规策略排产
                boolean planSuccess = SpringUtil.getBean(this.getClass()).doProcessPlan(processOrderNo, processPlanDates, productionPoolConfig,
                        ProcessPlanStrategy.NON_EARLIEST, processOrderLogs);
                if (!planSuccess) {
                    // 执行时间备选策略排产
                    processPlanBaseService.appendStartProcessPlanLog(processOrderLogs, processOrderNo, "时间最早");
                    planSuccess = SpringUtil.getBean(this.getClass()).doProcessPlan(processOrderNo, processPlanDates, productionPoolConfig,
                            ProcessPlanStrategy.EARLIEST, processOrderLogs);
                }

                // 排产结果
                if (planSuccess) {
                    maxProductionOrders.getAndDecrement();
                    processPlanBaseService.appendProcessPlanResultLog(processOrderLogs, processOrderNo, "排产成功！");
                    processPlanOrderBo.setSuccessPlan(true);
                } else {
                    processOrderBaseService.updateOverPlan(processOrderNo, OverPlan.TRUE);
                    processPlanBaseService.appendProcessPlanResultLog(processOrderLogs, processOrderNo, "更新超额订单!");
                }
                stringBuilderLoggerBaseService.logStringBuilderContent(processOrderLogs, processOrderBizTrace);
            }

            processPlanBaseService.logCompleteProcessPlanOrders(productionPoolCode, overPlan, orderTypes, ordersToProcessPlan, ordersBizTrace);
        }
    }

    /**
     * 剩余可排产单量不足。
     *
     * @param maxProductionOrders 最大生产订单数的原子长整型。
     * @return 如果剩余可排产单量不足够，则返回 true；否则返回 false。
     */
    private boolean insufficientAvailableProductionCapacity(AtomicLong maxProductionOrders) {
        return maxProductionOrders.get() <= 0;
    }

    /**
     * 执行排产计划生成操作。
     *
     * @param processOrderNo       加工单号
     * @param processPlanDates     加工计划日期集合
     * @param productionPoolConfig 产能池配置信息
     * @return 是否成功执行加工计划生成操作
     */
    public boolean doProcessPlan(String processOrderNo,
                                 TreeSet<LocalDate> processPlanDates,
                                 ProductionPoolConfigBo productionPoolConfig,
                                 ProcessPlanStrategy processPlanStrategy,
                                 StringBuilder processOrderLogs) {
        // 产能池配置信息
        final String productionPoolCode = productionPoolConfig.getProductionPoolCode();
        final LocalTime workStartTime = productionPoolConfig.getStartTime();
        final LocalTime workEndTime = productionPoolConfig.getEndTime();
        final List<BreakTimeSegmentBo> breakTimeSegments = productionPoolConfig.getBreakTimeSegments();
        final Set<LocalDate> holidays = productionPoolConfig.getHolidays();
        final BigDecimal workHourDuration = productionPoolConfig.getWorkHourDuration();
        LocalDateTime expectBeginTime;

        // 加工单信息 & 加工单工序信息 & 工序信息
        final ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            log.warn("执行工序排产计划失败！无加工单信息，加工单号：[{}] ", processOrderNo);
            return false;
        }
        final List<ProcessOrderProcedurePo> processOrderProcedurePos = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isEmpty(processOrderProcedurePos)) {
            log.warn("执行工序排产计划失败！无工序信息，加工单号：[{}] ", processOrderNo);
            return false;
        }
        final LocalDate planBeginDate = processPlanDates.stream().findFirst().orElse(null);
        if (Objects.isNull(planBeginDate)) {
            throw new BizException("执行工序排产计划失败！无排产开始时间，产能池编号：[{}]", productionPoolCode);
        }

        final Integer totalProcessNum = processOrderPo.getTotalProcessNum();
        final Set<Long> processIds = processOrderProcedurePos.stream().map(ProcessOrderProcedurePo::getProcessId).collect(Collectors.toSet());
        final List<Long> processOrderProcedureIds = processOrderProcedurePos.stream().map(ProcessOrderProcedurePo::getProcessOrderProcedureId).collect(Collectors.toList());
        final List<ProcessPo> processPos = processDao.getByProcessIds(new ArrayList<>(processIds));
        List<ProcessProcedureEmployeePlanBo> newProductionSchedules = Lists.newArrayList();

        // 获取工序关联信息（产能池、已排产计划、停工时间）
        ProcessPlanRelateQueryParamsBo processPlanRelateQueryParamsBo = new ProcessPlanRelateQueryParamsBo();
        processPlanRelateQueryParamsBo.setProcessIds(processIds);
        processPlanRelateQueryParamsBo.setProductionPoolCode(productionPoolCode);
        processPlanRelateQueryParamsBo.setProcessPlanDates(processPlanDates);
        ProcessPlanRelateBo processPlanRelateBo = processPlanBaseService.getProcessOrderRelateProcessPlanInfoBo(processPlanRelateQueryParamsBo);

        List<EmployeeProcessAbilityBo> productionCapacityPool = processPlanRelateBo.getEmployeeProcessAbilityBos();
        List<ProcessProcedureEmployeePlanBo> existProductionSchedules = processPlanRelateBo.getProcessProcedureEmployeePlanBos();
        List<EmployeeRestTimeBo> employeeRestTimes = processPlanRelateBo.getEmployeeRestTimeBos();
        expectBeginTime = processPlanBaseService.getProcessPlanWorkingBeginDateTime(planBeginDate, workStartTime);

        for (ProcessOrderProcedurePo processOrderProcedurePo : processOrderProcedurePos) {
            final Long processId = processOrderProcedurePo.getProcessId();
            final String processName = processOrderProcedurePo.getProcessName();
            final BigDecimal commission = processOrderProcedurePo.getCommission();
            final Long processOrderProcedureId = processOrderProcedurePo.getProcessOrderProcedureId();
            ProcessPo matchProcess = processPos.stream().filter(processPo -> Objects.equals(processId, processPo.getProcessId())).findFirst().orElse(null);
            if (Objects.isNull(matchProcess)) {
                throw new BizException("执行工序排产计划失败！无工序信息，工序名称：[{}]", processName);
            }
            final Integer setupDuration = matchProcess.getSetupDuration();

            // 通过加工单工序筛选符合职级能力产能人员
            processPlanBaseService.appendFilterEmployeeLog(processOrderLogs, processId, processName, totalProcessNum);
            final List<ProcessPlanEmployeeBo> productionSchedulesEmployees = processPlanBaseService.getProductionSchedulesEmployees(processId, totalProcessNum,
                    productionCapacityPool, processPlanStrategy, productionPoolConfig, expectBeginTime,
                    processPlanDates, existProductionSchedules, employeeRestTimes, processOrderLogs);
            if (CollectionUtils.isEmpty(productionSchedulesEmployees)) {
                return false;
            }

            // 根据筛选工序产能人员计算计划时间
            for (ProcessPlanEmployeeBo productionSchedulesEmployee : productionSchedulesEmployees) {
                final String employeeNo = productionSchedulesEmployee.getEmployeeNo();
                final String employeeName = productionSchedulesEmployee.getEmployeeName();
                final Integer processTotalCapacity = productionSchedulesEmployee.getProcessTotalCapacity();

                // 获取排产计划的开始时间和结束时间
                processPlanBaseService.appendSelectEmployeeLog(processOrderLogs, employeeNo, employeeName);
                ProcessEmployeePlanTimeParamBo productionScheduleTimeParam = ProcessEmployeePlanTimeParamBo.builder()
                        .employeeNo(employeeNo)
                        .employeeName(employeeName)
                        .processNum(totalProcessNum)
                        .processTotalCapacity(processTotalCapacity)
                        .expectBeginTime(expectBeginTime)
                        .workStartTime(workStartTime)
                        .workEndTime(workEndTime)
                        .holidays(holidays)
                        .processPlanDates(processPlanDates)
                        .workHourDuration(workHourDuration)
                        .breakTimeSegments(breakTimeSegments)
                        .existProductionSchedules(existProductionSchedules)
                        .employeeRestTimes(employeeRestTimes)
                        .build();
                EmployeePlanTimeBo productionScheduleTime = processPlanBaseService.getProductionScheduleTime(productionScheduleTimeParam, processOrderLogs);

                if (Objects.nonNull(productionScheduleTime)) {
                    // 能通过人员计算到对应的计划开始时间和结束时间，添加到排产计划
                    processOrderLogs.append(System.lineSeparator()).append("【保存排产计划】工序已成功排产！记录排产计划。").append(System.lineSeparator());
                    final LocalDateTime expectEndDateTime = productionScheduleTime.getExpectEndDateTime();

                    ProcessProcedureEmployeePlanBo newProductionSchedule = ProcessProcedureEmployeePlanConverter.toBo(productionPoolCode,
                            processOrderNo, processOrderProcedureId, processId, processName, totalProcessNum, commission, productionScheduleTime);
                    // 添加到新排产计划
                    newProductionSchedules.add(newProductionSchedule);
                    processPlanBaseService.appendProductionSchedulesLog(processOrderLogs, newProductionSchedules, "【保存排产计划】当前新增排产计划：");
                    // 更新已存在排产计划
                    existProductionSchedules.add(newProductionSchedule);
                    processPlanBaseService.appendProductionSchedulesLog(processOrderLogs, newProductionSchedules, "【保存排产计划】当前已存在排产计划：");

                    // 当前员工单量
                    productionCapacityPool = processPlanBaseService.updateCompleteCount(employeeNo, productionCapacityPool, 1);
                    processPlanBaseService.appendUpdateEmployeeProcessedCountLog(processOrderLogs, employeeNo);

                    // 扣减产能
                    productionCapacityPool = processPlanBaseService.deductCapacity(newProductionSchedule,
                            productionPoolConfig, productionCapacityPool, processOrderLogs);

                    // 下一道工序的开始时间=上一道工序的结束时间+整备时间
                    if (setupDuration > 0) {
                        expectBeginTime = processPlanBaseService.calculateNextStartTime(expectEndDateTime, setupDuration, workStartTime,
                                workEndTime, breakTimeSegments, holidays);
                        processPlanBaseService.appendUpdateNextProcessStartTimeLog(processOrderLogs, expectEndDateTime, setupDuration, expectBeginTime);
                    } else {
                        expectBeginTime = productionScheduleTime.getExpectEndDateTime();
                        processPlanBaseService.appendUpdateNextProcessStartTimeLog(processOrderLogs, expectEndDateTime, setupDuration, expectBeginTime);
                    }
                    break;
                }
                processPlanBaseService.appendFailEmployeeLog(processOrderLogs, employeeNo, employeeName);
            }
        }

        if (newProductionSchedules.size() == processOrderProcedureIds.size()) {
            ProcessPlanAfterBo processPlanAfterBo = new ProcessPlanAfterBo(productionCapacityPool, newProductionSchedules, processOrderPo);
            processPlanBaseService.doAfterProcessPlan(processPlanAfterBo);
            return true;
        }
        return false;
    }
}











