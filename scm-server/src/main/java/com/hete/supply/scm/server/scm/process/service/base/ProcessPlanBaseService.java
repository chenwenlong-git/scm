package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.converter.EmployeeProcessAbilityConverter;
import com.hete.supply.scm.server.scm.entity.bo.*;
import com.hete.supply.scm.server.scm.entity.po.EmployeeProcessAbilityPo;
import com.hete.supply.scm.server.scm.process.converter.ProcessProcedureEmployeePlanConverter;
import com.hete.supply.scm.server.scm.process.dao.EmployeeProcessAbilityDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessProcedureEmployeePlanDao;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessProcedureEmployeePlanPo;
import com.hete.supply.scm.server.scm.process.enums.ProcessPlanStrategy;
import com.hete.supply.scm.server.scm.process.service.biz.EmployeeRestTimeBizService;
import com.hete.supply.scm.server.scm.service.base.StringBuilderLoggerBaseService;
import com.hete.supply.scm.server.supplier.dao.EmployeeGradeDao;
import com.hete.supply.scm.server.supplier.dao.EmployeeGradeRelationDao;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradePo;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradeRelationPo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yanjiawei
 * @date 2023年07月27日 07:42
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProcessPlanBaseService {
    @Autowired
    private Environment environment;
    private final EmployeeProcessAbilityDao employeeProcessAbilityDao;
    private final ProcessOrderDao processOrderDao;
    private final ProcessProcedureEmployeePlanDao processProcedureEmployeePlanDao;
    private final EmployeeGradeRelationDao employeeGradeRelationDao;
    private final EmployeeGradeDao employeeGradeDao;
    private final ProcessOrderBaseService processOrderBaseService;
    private final StringBuilderLoggerBaseService stringBuilderLoggerBaseService;
    private final EmployeeRestTimeBizService employeeRestTimeBizService;

    public Set<String> getProcessPlanEmployees(ProcessOrderType processOrderType) {
        if (Objects.isNull(processOrderType)) {
            throw new ParamIllegalException("获取员工信息配置失败，订单类型为空");
        }

        Set<String> processPlanEmployees = Sets.newHashSet();
        for (int index = 0; ; index++) {
            String key = StrUtil.format("process-plan-employee-config.{}[{}]", processOrderType, index);
            String property = environment.getProperty(key, String.class);
            if (StrUtil.isNotBlank(property)) {
                processPlanEmployees.add(property);
            } else {
                return processPlanEmployees;
            }
        }
    }

    public LocalDateTime getProcessPlanWorkingBeginDateTime(LocalDate today, LocalTime workStartTime) {
        return today.atTime(workStartTime);
    }

    /**
     * 重新计算时间并生成加工计划时间信息。
     *
     * @param expectBeginTime     预计开始时间
     * @param expectEndTime       预计结束时间
     * @param delight             延迟时长
     * @param chronoUnit          时间单位（例如：ChronoUnit.HOURS）
     * @param workStartTime       工作开始时间
     * @param workEndTime         工作结束时间
     * @param breakTimeSegments   休息时间段列表
     * @param holidays            节假日日期集合
     * @param cannotScheduleTimes 不能执行计划时间信息列表
     * @return 加工计划时间信息对象
     */
    public ProcessPlanTimeBo recalculateTime(LocalDateTime expectBeginTime, LocalDateTime expectEndTime,
                                             long delight, ChronoUnit chronoUnit,
                                             LocalTime workStartTime, LocalTime workEndTime,
                                             List<BreakTimeSegmentBo> breakTimeSegments,
                                             Set<LocalDate> holidays,
                                             List<ProcessPlanTimeBo> cannotScheduleTimes,
                                             StringBuilder processOrderLogs) {
        if (CollectionUtils.isEmpty(cannotScheduleTimes)) {
            return new ProcessPlanTimeBo(expectBeginTime, expectEndTime);
        }

        // 根据开始时间asc排序
        cannotScheduleTimes = cannotScheduleTimes.stream().sorted(Comparator.comparing(ProcessPlanTimeBo::getExpectBeginDateTime)).collect(Collectors.toList());

        // 用当前开始时间和结束时间轮询已存在的排产计划计划
        for (ProcessPlanTimeBo cannotScheduleTime : cannotScheduleTimes) {
            final LocalDateTime existBeginTime = cannotScheduleTime.getExpectBeginDateTime();
            final LocalDateTime existEndTime = cannotScheduleTime.getExpectEndDateTime();

            // 校验当前开始时间和结束时间与已存在计划的开始时间和技术时间是否冲突
            if (ScmTimeUtil.isTimeSlotConflict(expectBeginTime, expectEndTime, existBeginTime, existEndTime)) {

                // 如果存在冲突，开始时间 = 已存在结束时间；结束时间 = 已存在计划的结束时间+耗时,同时剔除掉存在的冲突时间，然后执行下一次轮询
                List<ProcessPlanTimeBo> newExistProcessPlanTimeBos = cannotScheduleTimes.stream()
                        .filter(processPlanTimeBo -> !Objects.equals(existBeginTime, processPlanTimeBo.getExpectBeginDateTime())
                                && !Objects.equals(existEndTime, processPlanTimeBo.getExpectEndDateTime())).collect(Collectors.toList());
                LocalDateTime newExpectEndTime = this.calculateEndTime(existEndTime, delight, chronoUnit, workStartTime, workEndTime, breakTimeSegments, holidays);
                this.appendConflictPlanTimeLog(processOrderLogs, expectBeginTime, expectEndTime, existBeginTime, existEndTime);

                return this.recalculateTime(existEndTime, newExpectEndTime, delight, chronoUnit,
                        workStartTime, workEndTime, breakTimeSegments, holidays, newExistProcessPlanTimeBos, processOrderLogs);
            } else {
                // 没有冲突，继续用开始时间+结束时间进行下一个时间段轮询
                List<ProcessPlanTimeBo> newExistProcessPlanTimeBos = cannotScheduleTimes.stream()
                        .filter(processPlanTimeBo -> !Objects.equals(existBeginTime, processPlanTimeBo.getExpectBeginDateTime())
                                && !Objects.equals(existEndTime, processPlanTimeBo.getExpectEndDateTime())).collect(Collectors.toList());

                return this.recalculateTime(expectBeginTime, expectEndTime, delight, chronoUnit,
                        workStartTime, workEndTime, breakTimeSegments, holidays, newExistProcessPlanTimeBos, processOrderLogs);
            }
        }

        // 递归结束后返回
        ProcessPlanTimeBo processPlanTimeBo = new ProcessPlanTimeBo();
        processPlanTimeBo.setExpectBeginDateTime(expectBeginTime);
        processPlanTimeBo.setExpectEndDateTime(expectEndTime);
        return processPlanTimeBo;
    }

    /**
     * 获取需要排产的单据
     *
     * @return
     */
    public List<ProcessPlanOrderBo> getOrdersToProcessPlan(OrdersToProcessPlanParametersBo param) {
        List<ProcessPlanOrderBo> processPlanOrderBo = processOrderDao.getOrdersToProcessPlan(param);
        if (CollectionUtils.isNotEmpty(processPlanOrderBo)) {
            // 按照 capacityNum 从高到低排序 如果 capacityNum 相同，按照创建时间从早到晚排序
            processPlanOrderBo.sort(Comparator
                    .comparing(ProcessPlanOrderBo::getCapacityNum).reversed()
                    .thenComparing(ProcessPlanOrderBo::getCreateTime));
        }
        return processPlanOrderBo;
    }

    /**
     * 更新员工已排产单量
     *
     * @param employeeNo
     * @param employeeProcessAbilityBos
     * @param completeCount
     * @return
     */
    public List<EmployeeProcessAbilityBo> updateCompleteCount(String employeeNo, List<EmployeeProcessAbilityBo> employeeProcessAbilityBos, int completeCount) {
        if (CollectionUtils.isEmpty(employeeProcessAbilityBos)) {
            return employeeProcessAbilityBos;
        }

        List<EmployeeProcessAbilityBo> matchEmployeeList = employeeProcessAbilityBos.stream().filter(employeeProcessAbilityBo -> Objects.equals(employeeNo, employeeProcessAbilityBo.getEmployeeNo())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(matchEmployeeList)) {
            matchEmployeeList.forEach(matchEmployee -> {
                int completePlanCount = Objects.isNull(matchEmployee.getCompletePlanCount()) ? 0 : matchEmployee.getCompletePlanCount();
                matchEmployee.setCompletePlanCount(completePlanCount + completeCount);
            });
        }
        return employeeProcessAbilityBos;
    }

    /**
     * 根据工序ID、工序数量等条件匹配产能池中的员工，并生成工序计划员工列表。
     *
     * @param processId                工序ID
     * @param processNum               工序数量
     * @param productionCapacityPool   产能池员工列表
     * @param processPlanStrategy      工序排产策略
     * @param productionPoolConfig     产能池配置信息
     * @param expectBeginTime          预计开始时间
     * @param processPlanDates         工序计划日期集合
     * @param existProductionSchedules 已存在的工序计划
     * @param employeeRestTimes        员工休息时间段列表
     * @param processOrderLogs         排产日志记录
     * @return 工序计划员工列表
     * @throws BizException 如果参数校验不通过或无法匹配到合适的员工时抛出业务异常
     */
    public List<ProcessPlanEmployeeBo> getProductionSchedulesEmployees(Long processId,
                                                                       int processNum,
                                                                       List<EmployeeProcessAbilityBo> productionCapacityPool,
                                                                       ProcessPlanStrategy processPlanStrategy,
                                                                       ProductionPoolConfigBo productionPoolConfig,
                                                                       LocalDateTime expectBeginTime,
                                                                       TreeSet<LocalDate> processPlanDates,
                                                                       List<ProcessProcedureEmployeePlanBo> existProductionSchedules,
                                                                       List<EmployeeRestTimeBo> employeeRestTimes,
                                                                       StringBuilder processOrderLogs) {
        if (Objects.isNull(processId)) {
            throw new BizException("执行排产计划失败！通过工序id匹配产能池人员异常，工序id为空");
        }
        if (processNum <= 0) {
            throw new BizException("执行排产计划失败！通过工序id匹配产能池人员异常，加工数量小于等于0");
        }
        if (CollectionUtils.isEmpty(productionCapacityPool)) {
            return Collections.emptyList();
        }

        // 刷新加工单产能池优先级并根据策略排序
        productionCapacityPool.sort(Comparator.comparingInt(EmployeeProcessAbilityBo::getCompletePlanCount)
                .thenComparing(EmployeeProcessAbilityBo::getGradeLevel));

        // 筛选满足本次加工数的人员
        Map<String, Integer> employeeTotalProcessedNum = productionCapacityPool.stream()
                .filter(employee -> employee.getProcessId().equals(processId))
                .collect(Collectors.groupingBy(EmployeeProcessAbilityBo::getEmployeeNo,
                        Collectors.summingInt(EmployeeProcessAbilityBo::getAvailableProcessedNum)));
        List<EmployeeProcessAbilityBo> match = productionCapacityPool.stream()
                .filter(employee -> Objects.equals(processId, employee.getProcessId()) && employeeTotalProcessedNum.get(employee.getEmployeeNo()) >= processNum)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(match)) {
            processOrderLogs.append(System.lineSeparator());
            processOrderLogs.append(StrUtil.format("【筛选工序员工】 剩余总产能数>={}，无法匹配到对应员工！", processNum));
            return Collections.emptyList();
        }
        this.appendFilterEmployeeProcessAbilityInfoLog(processOrderLogs, match);

        List<ProcessPlanEmployeeBo> productionSchedulesEmployees = match.stream().map(item -> {
            ProcessPlanEmployeeBo processPlanEmployeeBo = new ProcessPlanEmployeeBo();
            processPlanEmployeeBo.setEmployeeNo(item.getEmployeeNo());
            processPlanEmployeeBo.setEmployeeName(item.getEmployeeName());
            processPlanEmployeeBo.setProcessId(item.getProcessId());
            processPlanEmployeeBo.setProcessName(item.getProcessName());
            processPlanEmployeeBo.setProcessTotalCapacity(item.getTotalProcessedNum());
            processPlanEmployeeBo.setAvailableProcessedNum(item.getAvailableProcessedNum());
            processPlanEmployeeBo.setCompletePlanCount(item.getCompletePlanCount());
            processPlanEmployeeBo.setGradeLevel(item.getGradeLevel());
            return processPlanEmployeeBo;
        }).collect(Collectors.toList());
        if (ProcessPlanStrategy.EARLIEST.equals(processPlanStrategy)) {
            processOrderLogs.append("【筛选工序员工】 根据单量少、开始时间和结束时间最早优先级排序：");
            return this.generateProcessPlanByEarliestTime(processNum, productionSchedulesEmployees, productionPoolConfig,
                    expectBeginTime, processPlanDates, existProductionSchedules, employeeRestTimes, processOrderLogs);
        }
        return productionSchedulesEmployees;
    }

    /**
     * @Description 按照最早的开始时间和结束时间筛选人员
     * 计算出所有人的开始时间和结束时间，同时按开始时间和结束时间升序排序
     * @author yanjiawei
     * @Date 2023/8/31 16:11
     */
    public List<ProcessPlanEmployeeBo> generateProcessPlanByEarliestTime(Integer processNum,
                                                                         List<ProcessPlanEmployeeBo> productionSchedulesEmployees,
                                                                         ProductionPoolConfigBo productionPoolConfig,
                                                                         LocalDateTime expectBeginTime,
                                                                         TreeSet<LocalDate> processPlanDates,
                                                                         List<ProcessProcedureEmployeePlanBo> existProductionSchedules,
                                                                         List<EmployeeRestTimeBo> employeeRestTimes,
                                                                         StringBuilder processOrderLogs) {
        if (CollectionUtils.isEmpty(productionSchedulesEmployees)) {
            return Collections.emptyList();
        }

        // 产能池配置信息
        final LocalTime workStartTime = productionPoolConfig.getStartTime();
        final LocalTime workEndTime = productionPoolConfig.getEndTime();
        final List<BreakTimeSegmentBo> breakTimeSegments = productionPoolConfig.getBreakTimeSegments();
        final Set<LocalDate> holidays = productionPoolConfig.getHolidays();
        final BigDecimal workHourDuration = productionPoolConfig.getWorkHourDuration();

        for (ProcessPlanEmployeeBo productionSchedulesEmployee : productionSchedulesEmployees) {
            final String employeeNo = productionSchedulesEmployee.getEmployeeNo();
            final String employeeName = productionSchedulesEmployee.getEmployeeName();
            final Integer processTotalCapacity = productionSchedulesEmployee.getProcessTotalCapacity();

            ProcessEmployeePlanTimeParamBo productionScheduleTimeParam = ProcessEmployeePlanTimeParamBo.builder()
                    .employeeNo(employeeNo)
                    .employeeName(employeeName)
                    .processNum(processNum)
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
            EmployeePlanTimeBo productionScheduleTime = this.getProductionScheduleTime(productionScheduleTimeParam, processOrderLogs);

            if (Objects.nonNull(productionScheduleTime)) {
                productionSchedulesEmployee.setExpectBeginDateTime(productionScheduleTime.getExpectBeginDateTime());
                productionSchedulesEmployee.setExpectEndDateTime(productionScheduleTime.getExpectEndDateTime());
            } else {
                productionSchedulesEmployee.setExpectBeginDateTime(LocalDateTime.MAX);
                productionSchedulesEmployee.setExpectEndDateTime(LocalDateTime.MAX);
            }
        }

        // 创建一个自定义的Comparator，按照单量少升序排序，后按照开始时间升序排序，如果开始时间相同则按结束时间升序排序
        Comparator<ProcessPlanEmployeeBo> sortByCompleteCountAndTime = Comparator
                .comparing(ProcessPlanEmployeeBo::getCompletePlanCount)
                .thenComparing(ProcessPlanEmployeeBo::getExpectBeginDateTime)
                .thenComparing(ProcessPlanEmployeeBo::getExpectEndDateTime);
        if (CollectionUtils.isNotEmpty(productionSchedulesEmployees)) {
            productionSchedulesEmployees.sort(sortByCompleteCountAndTime);
        }
        this.appendEmployeeInfoLog(processOrderLogs, productionSchedulesEmployees);
        return productionSchedulesEmployees;
    }

    /**
     * 根据给定的查询参数，获取与工序计划相关的产能池信息、员工产能池信息、员工职级信息和员工单量信息。
     *
     * @param param 查询参数
     * @return 包含关联信息的 ProcessPlanRelateBo 对象
     */
    public ProcessPlanRelateBo getProcessOrderRelateProcessPlanInfoBo(ProcessPlanRelateQueryParamsBo param) {
        // 创建用于存储关联信息的 ProcessPlanRelateBo 对象
        ProcessPlanRelateBo processPlanRelateBos = new ProcessPlanRelateBo();

        // 获取查询参数中的产能池编号、工序ID集合和工序计划日期集合
        final String productionPoolCode = param.getProductionPoolCode();
        final Set<Long> processIds = param.getProcessIds();
        final TreeSet<LocalDate> processPlanDates = param.getProcessPlanDates();
        final Set<String> employeeNos;

        // 产能池人员
        final List<EmployeeProcessAbilityBo> employeeProcessAbilityBos = this.getEmployeeProcessAbilityBos(productionPoolCode, processIds, processPlanDates);
        employeeNos = employeeProcessAbilityBos.stream().map(EmployeeProcessAbilityBo::getEmployeeNo).collect(Collectors.toSet());
        processPlanRelateBos.setEmployeeProcessAbilityBos(employeeProcessAbilityBos);

        // 排产计划
        List<ProcessProcedureEmployeePlanPo> processProcedureEmployeePlanPos
                = Optional.ofNullable(processProcedureEmployeePlanDao.getProcessProcedureEmployeePlanPos(productionPoolCode, employeeNos, processPlanDates)).orElse(Lists.newArrayList());
        final List<ProcessProcedureEmployeePlanBo> processProcedureEmployeePlanBos = ProcessProcedureEmployeePlanConverter.toBos(processProcedureEmployeePlanPos);
        processPlanRelateBos.setProcessProcedureEmployeePlanBos(processProcedureEmployeePlanBos);

        // 停工时间
        final List<EmployeeRestTimeBo> employeeRestTimes = employeeRestTimeBizService.getEmployeeRestTimes(employeeNos);
        processPlanRelateBos.setEmployeeRestTimeBos(employeeRestTimes);
        return processPlanRelateBos;
    }

    /**
     * 获取员工产能池信息，包括工序关联的产能池和员工相关信息。
     *
     * @param productionPoolCode 产能池编码
     * @param processIds         工序ID集合
     * @param processPlanDates   工序计划日期集合
     * @return 员工产能池信息列表
     */
    public List<EmployeeProcessAbilityBo> getEmployeeProcessAbilityBos(String productionPoolCode, Set<Long> processIds, TreeSet<LocalDate> processPlanDates) {
        // 获取与产能池、工序、排产计划相关的产能池信息
        List<EmployeeProcessAbilityPo> processProductionCapacityPool
                = employeeProcessAbilityDao.getEmployeeProcessAbilityPoList(productionPoolCode, processIds, processPlanDates);
        // 如果没有相关产能池信息，返回空列表
        if (CollectionUtils.isEmpty(processProductionCapacityPool)) {
            return Collections.emptyList();
        }

        // 通过工序关联的产能池获取对应的员工产能池，用于扣减员工的共享产能
        final Set<String> employeeNos = processProductionCapacityPool.stream()
                .map(EmployeeProcessAbilityPo::getEmployeeNo)
                .collect(Collectors.toSet());
        List<EmployeeProcessAbilityBo> employeeProductionCapacityPool
                = employeeProcessAbilityDao.getEmployeeProcessAbilityBoList(productionPoolCode, employeeNos, processPlanDates);
        // 如果没有相关员工产能池信息，返回空列表
        if (CollectionUtils.isEmpty(employeeProductionCapacityPool)) {
            return Collections.emptyList();
        }

        // 获取员工职级 & 单量信息
        List<EmployeeLevelBo> employeeLevelBos = this.getEmployeeLevelBos(employeeNos);
        List<EmployeePlanCountBo> employeePlanCountBos = this.getEmployeePlanCountBo(employeeNos);

        // 剔除无职级的员工
        employeeProductionCapacityPool.removeIf(employeeProcessAbilityBo -> {
            final String employeeNo = employeeProcessAbilityBo.getEmployeeNo();
            EmployeeLevelBo matchLevel = employeeLevelBos.stream()
                    .filter(employeeLevelBo -> Objects.equals(employeeNo, employeeLevelBo.getEmployeeNo()))
                    .findFirst()
                    .orElse(null);
            if (Objects.isNull(matchLevel)) {
                return true;
            } else {
                employeeProcessAbilityBo.setGradeLevel(matchLevel.getGradeLevel());
                EmployeePlanCountBo matchPlanCount = employeePlanCountBos.stream()
                        .filter(employeeLevelBo -> Objects.equals(employeeNo, employeeLevelBo.getEmployeeNo()))
                        .findFirst()
                        .orElse(null);
                if (Objects.isNull(matchPlanCount)) {
                    employeeProcessAbilityBo.setCompletePlanCount(0);
                } else {
                    employeeProcessAbilityBo.setCompletePlanCount(matchPlanCount.getCompletePlanCount());
                }
                return false;
            }
        });

        return employeeProductionCapacityPool;
    }

    /**
     * 根据工序排产时间参数计算员工的排产时间信息。
     *
     * @param processEmployeePlanTimeParamBo 工序排产时间参数
     * @param processOrderLogs               用于记录排产日志的StringBuilder
     * @return 员工的排产时间信息
     */
    public EmployeePlanTimeBo getProductionScheduleTime(ProcessEmployeePlanTimeParamBo processEmployeePlanTimeParamBo,
                                                        StringBuilder processOrderLogs) {
        final String employeeNo = processEmployeePlanTimeParamBo.getEmployeeNo();
        final String employeeName = processEmployeePlanTimeParamBo.getEmployeeName();
        final LocalDateTime expectBeginTime = processEmployeePlanTimeParamBo.getExpectBeginTime();
        final List<BreakTimeSegmentBo> breakTimeSegments = processEmployeePlanTimeParamBo.getBreakTimeSegments();
        final TreeSet<LocalDate> processPlanDates = processEmployeePlanTimeParamBo.getProcessPlanDates();
        final LocalTime workStartTime = processEmployeePlanTimeParamBo.getWorkStartTime();
        final LocalTime workEndTime = processEmployeePlanTimeParamBo.getWorkEndTime();
        final Set<LocalDate> holidays = processEmployeePlanTimeParamBo.getHolidays();
        final BigDecimal totalWorkerHour = processEmployeePlanTimeParamBo.getWorkHourDuration();
        final List<ProcessProcedureEmployeePlanBo> existProductionSchedules = processEmployeePlanTimeParamBo.getExistProductionSchedules();
        final List<EmployeeRestTimeBo> configEmployeeRestTimes = processEmployeePlanTimeParamBo.getEmployeeRestTimes();
        final BigDecimal processNum = new BigDecimal(processEmployeePlanTimeParamBo.getProcessNum());
        final BigDecimal processTotalCapacity = new BigDecimal(processEmployeePlanTimeParamBo.getProcessTotalCapacity());
        final BigDecimal totalHours = processNum.multiply(totalWorkerHour).divide(processTotalCapacity, 2, RoundingMode.DOWN);
        int totalMinutes = ScmTimeUtil.convertToMinutes(totalHours);

        // 计算工序的开始时间和结束时间
        EmployeePlanTimeBo productionScheduleTime = this.calculateEmployeeProductionScheduleTime(employeeNo, employeeName, expectBeginTime,
                existProductionSchedules, configEmployeeRestTimes, totalMinutes, workStartTime, workEndTime, breakTimeSegments, holidays, processOrderLogs);

        // 校验结束时间是否超过本次排产周期的截止时间
        LocalDateTime finalExpectBeginDateTime = productionScheduleTime.getExpectBeginDateTime();
        LocalDateTime finalExpectEndDateTime = productionScheduleTime.getExpectEndDateTime();
        if (finalExpectEndDateTime.isAfter(processPlanDates.last().atTime(workEndTime))) {
            this.appendFinalPlanTimeLog(processOrderLogs, finalExpectBeginDateTime, totalMinutes, finalExpectEndDateTime);
            return null;
        } else {
            this.appendGeneratePlanTimeLog(processOrderLogs, finalExpectBeginDateTime, totalMinutes, finalExpectEndDateTime, true);
            return productionScheduleTime;
        }
    }


    /**
     * 计算员工的排产时间信息。
     *
     * @param employeeNo               员工编号
     * @param employeeName             员工姓名
     * @param expectBeginTime          预计开始时间
     * @param existProductionSchedules 工序人员已排产计划列表
     * @param configEmployeeRestTimes  工序人员休息时间段列表
     * @param totalMinutes             总计划时长（分钟）
     * @param workStartTime            工作开始时间
     * @param workEndTime              工作结束时间
     * @param breakTimeSegments        休息时间段列表
     * @param holidays                 节假日集合
     * @param processOrderLogs         用于记录排产日志的StringBuilder
     * @return 员工的排产时间信息
     */
    private EmployeePlanTimeBo calculateEmployeeProductionScheduleTime(String employeeNo,
                                                                       String employeeName,
                                                                       LocalDateTime expectBeginTime,
                                                                       List<ProcessProcedureEmployeePlanBo> existProductionSchedules,
                                                                       List<EmployeeRestTimeBo> configEmployeeRestTimes,
                                                                       int totalMinutes,
                                                                       LocalTime workStartTime,
                                                                       LocalTime workEndTime,
                                                                       List<BreakTimeSegmentBo> breakTimeSegments,
                                                                       Set<LocalDate> holidays,
                                                                       StringBuilder processOrderLogs) {
        EmployeePlanTimeBo productionScheduleTime = new EmployeePlanTimeBo(employeeNo, employeeName);

        // 根据开始时间和耗时（分钟）计算预计结束时间，如果有配置休息时间则跳过休息时间。
        LocalDateTime expectEndTime = this.calculateEndTime(expectBeginTime, totalMinutes, ChronoUnit.MINUTES, workStartTime, workEndTime, breakTimeSegments, holidays);
        this.appendGeneratePlanTimeLog(processOrderLogs, expectBeginTime, totalMinutes, expectEndTime, false);

        // 获取工序人员已排产计划
        List<ProcessProcedureEmployeePlanBo> matchEmployeeExistSchedules = existProductionSchedules.stream()
                .filter(processProcedureEmployeePlanBo -> Objects.equals(employeeNo, processProcedureEmployeePlanBo.getEmployeeNo()))
                .collect(Collectors.toList());
        // 获取工序人员休息时间段
        List<EmployeeRestTimeBo> matchRestTimes = configEmployeeRestTimes.stream()
                .filter(processProcedureEmployeePlanBo -> Objects.equals(employeeNo, processProcedureEmployeePlanBo.getEmployeeNo()))
                .collect(Collectors.toList());

        // 当前工序人员无排产计划 & 无休息时间段
        if (CollectionUtils.isEmpty(matchEmployeeExistSchedules) && CollectionUtils.isEmpty(matchRestTimes)) {
            productionScheduleTime.setExpectBeginDateTime(expectBeginTime);
            productionScheduleTime.setExpectEndDateTime(expectEndTime);
        } else {
            // 当前工序人员有排产计划/休息时间段，需要重新计算计划开始时间和结束时间
            final List<ProcessPlanTimeBo> cannotScheduleTime = Stream.concat(
                    matchEmployeeExistSchedules.stream().map(employeePlan -> {
                        ProcessPlanTimeBo processPlanTimeBo = new ProcessPlanTimeBo();
                        processPlanTimeBo.setExpectBeginDateTime(employeePlan.getExpectBeginDateTime());
                        processPlanTimeBo.setExpectEndDateTime(employeePlan.getExpectEndDateTime());
                        return processPlanTimeBo;
                    }),
                    matchRestTimes.stream().map(restTimeBo -> {
                        ProcessPlanTimeBo processPlanTimeBo = new ProcessPlanTimeBo();
                        processPlanTimeBo.setExpectBeginDateTime(restTimeBo.getRestStartTime());
                        processPlanTimeBo.setExpectEndDateTime(restTimeBo.getRestEndTime());
                        return processPlanTimeBo;
                    })
            ).collect(Collectors.toList());

            // 根据冲突计划重新计算开始时间和结束时间
            ProcessPlanTimeBo newProductionScheduleTime = this.recalculateTime(expectBeginTime, expectEndTime,
                    totalMinutes, ChronoUnit.MINUTES, workStartTime,
                    workEndTime, breakTimeSegments, holidays, cannotScheduleTime, processOrderLogs);

            productionScheduleTime.setExpectBeginDateTime(newProductionScheduleTime.getExpectBeginDateTime());
            productionScheduleTime.setExpectEndDateTime(newProductionScheduleTime.getExpectEndDateTime());
        }
        return productionScheduleTime;
    }


    /**
     * 获取员工职级信息
     *
     * @param employeeNos
     * @return
     */
    public List<EmployeeLevelBo> getEmployeeLevelBos(Set<String> employeeNos) {
        if (CollectionUtils.isEmpty(employeeNos)) {
            throw new ParamIllegalException("获取员工职级异常，查询实体为空");
        }
        List<EmployeeGradeRelationPo> employeeGradeRelationPos = employeeGradeRelationDao.getByEmployees(employeeNos);
        if (CollectionUtils.isEmpty(employeeGradeRelationPos)) {
            return Collections.emptyList();
        }
        Set<Long> gradeIds = employeeGradeRelationPos.stream().map(EmployeeGradeRelationPo::getEmployeeGradeId).collect(Collectors.toSet());
        List<EmployeeGradePo> employeeGradePos = employeeGradeDao.getByIds(gradeIds);
        if (CollectionUtils.isEmpty(employeeGradePos)) {
            return Collections.emptyList();
        }
        return employeeGradeRelationPos.stream().map(employeeGradeRelationPo -> {
            List<EmployeeGradePo> match = employeeGradePos.stream().filter(employeeGradePo -> Objects.equals(employeeGradeRelationPo.getEmployeeGradeId(), employeeGradePo.getEmployeeGradeId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(match)) {
                return null;
            }
            EmployeeGradePo employeeGradePo = match.stream().max(Comparator.comparing(EmployeeGradePo::getGradeLevel)).orElse(null);
            if (Objects.isNull(employeeGradePo)) {
                return null;
            }

            EmployeeLevelBo employeeLevelBo = new EmployeeLevelBo();
            employeeLevelBo.setEmployeeNo(employeeGradeRelationPo.getEmployeeNo());
            employeeLevelBo.setGradeType(employeeGradePo.getGradeType());
            employeeLevelBo.setGradeName(employeeGradePo.getGradeName());
            employeeLevelBo.setGradeLevel(employeeGradePo.getGradeLevel());
            return employeeLevelBo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<EmployeePlanCountBo> getEmployeePlanCountBo(Set<String> employeeNos) {
        List<EmployeePlanCountBo> employeePlanCountBos = Optional.ofNullable(processProcedureEmployeePlanDao.getEmployeePlanCount(employeeNos)).orElse(Collections.emptyList());
        return employeeNos.stream().map(employeeNo -> {
            EmployeePlanCountBo bo = new EmployeePlanCountBo();
            bo.setEmployeeNo(employeeNo);
            EmployeePlanCountBo match = employeePlanCountBos.stream().filter(employeePlanCountBo -> Objects.equals(employeeNo, employeePlanCountBo.getEmployeeNo())).findFirst().orElse(null);
            if (Objects.isNull(match)) {
                bo.setCompletePlanCount(0);
            } else {
                bo.setCompletePlanCount(match.getCompletePlanCount());
            }
            return bo;
        }).collect(Collectors.toList());
    }

    /**
     * 计算结束时间，考虑工作时间、休息时间和假期。
     *
     * @param startTime         开始时间
     * @param amountToAdd       要添加的时间数量
     * @param chronoUnit        时间单位（例如：ChronoUnit.HOURS）
     * @param workStartTime     工作开始时间
     * @param workEndTime       工作结束时间
     * @param breakTimeSegments 休息时间段列表
     * @param holidays          假期日期集合
     * @return 计算后的结束时间
     */
    public LocalDateTime calculateEndTime(LocalDateTime startTime, long amountToAdd, ChronoUnit chronoUnit,
                                          LocalTime workStartTime, LocalTime workEndTime,
                                          List<BreakTimeSegmentBo> breakTimeSegments,
                                          Set<LocalDate> holidays) {
        Duration duration = Duration.ofHours(0).plus(amountToAdd, chronoUnit);
        if (CollectionUtils.isNotEmpty(breakTimeSegments)) {
            List<TimeRangeBo> breakTimeRanges = breakTimeSegments.stream().map(breakTimeSegmentBo ->
                    new TimeRangeBo(breakTimeSegmentBo.getStartTime(), breakTimeSegmentBo.getEndTime())).collect(Collectors.toList());

            TreeSet<TimeRangeBo> workingTimes = ScmTimeUtil.calculateWorkTime(workStartTime, workEndTime, breakTimeRanges);
            return ScmTimeUtil.calculateEndTime(startTime, 0, duration.toMinutes(), workingTimes, holidays);
        } else {
            TreeSet<TimeRangeBo> workingTimes = ScmTimeUtil.calculateWorkTime(workStartTime, workEndTime, Collections.emptyList());
            return ScmTimeUtil.calculateEndTime(startTime, 0, duration.toMinutes(), workingTimes, holidays);
        }
    }

    /**
     * 执行计划后续执行操作。
     *
     * @param processPlanAfterBo 加工计划后操作的业务对象。
     */
    public void doAfterProcessPlan(ProcessPlanAfterBo processPlanAfterBo) {
        // 获取 ProcessPlanAfterBo 中的元素
        final List<EmployeeProcessAbilityBo> employeeProcessAbilityBoList = processPlanAfterBo.getProductionCapacityPool();
        final List<ProcessProcedureEmployeePlanBo> processProcedureEmployeePlanPoList = processPlanAfterBo.getNewProductionSchedule();
        ProcessOrderPo processOrderPo = processPlanAfterBo.getProcessOrderPo();

        // 更新产能信息
        List<EmployeeProcessAbilityPo> updateEmployeeProcessAbilityPos = EmployeeProcessAbilityConverter.convertToPo(employeeProcessAbilityBoList);
        if (CollectionUtils.isNotEmpty(updateEmployeeProcessAbilityPos)) {
            employeeProcessAbilityDao.updateBatchByIdVersion(updateEmployeeProcessAbilityPos);
        }

        // 保存工序人员排产计划
        List<ProcessProcedureEmployeePlanBo> insertProcessProcedureEmployeePlanBo
                = processProcedureEmployeePlanPoList.stream()
                .filter(processProcedureEmployeePlanBo -> Objects.isNull(processProcedureEmployeePlanBo.getProcessProcedureEmployeePlanId())).collect(Collectors.toList());
        List<ProcessProcedureEmployeePlanPo> insertProcessProcedureEmployeePlanPos = ProcessProcedureEmployeePlanConverter.bosToPos(insertProcessProcedureEmployeePlanBo);
        if (CollectionUtils.isNotEmpty(insertProcessProcedureEmployeePlanPos)) {
            processProcedureEmployeePlanDao.insertBatch(insertProcessProcedureEmployeePlanPos);
        }

        // 更新加工单排产时间 & 开始时间 & 结束时间
        processOrderPo = processOrderBaseService.refreshProcessOrderTime(processOrderPo, insertProcessProcedureEmployeePlanBo);
        processOrderPo.setNeedProcessPlan(NeedProcessPlan.TRUE);
        processOrderBaseService.changeStatus(processOrderPo, ProcessOrderStatus.WAIT_PRODUCE, new OperatorUserBo());
        processOrderDao.updateByIdVersion(processOrderPo);
    }

    /**
     * 取消排产
     *
     * @param cancelProcessPlanBo
     */
    public void cancelProcessPlan(CancelProcessPlanBo cancelProcessPlanBo) {
        final String processOrderNo = cancelProcessPlanBo.getProcessOrderNo();
        final List<ProcessProcedureEmployeePlanPo> delPlanPos = processProcedureEmployeePlanDao.getByProcessOrderNo(processOrderNo);
        final ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new ParamIllegalException("加工单信息不存在,请刷新页面");
        }

        if (CollectionUtils.isEmpty(delPlanPos)) {
            ProcessOrderPo newProcessOrderPo = processOrderBaseService.refreshProcessOrderTime(processOrderPo, Collections.emptyList());
            newProcessOrderPo.setProcessPlanDelay(ProcessPlanDelay.FALSE);
            processOrderBaseService.changeStatus(newProcessOrderPo, ProcessOrderStatus.WAIT_PLAN, new OperatorUserBo());
            return;
        }

        // 删除工序人员排产信息
        List<Long> delPlanIds = delPlanPos.stream().map(ProcessProcedureEmployeePlanPo::getProcessProcedureEmployeePlanId).collect(Collectors.toList());
        delPlanIds.forEach(processProcedureEmployeePlanDao::removeById);

        // 清空加工单的工序开始时间和结束时间 & 状态=待排产
        ProcessOrderPo newProcessOrderPo = processOrderBaseService.refreshProcessOrderTime(processOrderPo, Collections.emptyList());
        newProcessOrderPo.setProcessPlanDelay(ProcessPlanDelay.FALSE);
        newProcessOrderPo.setNeedProcessPlan(NeedProcessPlan.FALSE);
        processOrderBaseService.changeStatus(newProcessOrderPo, ProcessOrderStatus.WAIT_PLAN, new OperatorUserBo());
    }

    /**
     * 根据排产池池配置获取加工排产日期集合。
     *
     * @param productionPoolConfig 生产池配置信息。
     * @return 加工计划日期的 TreeSet 集合。
     */
    public TreeSet<LocalDate> getProcessPlanDate(ProductionPoolConfigBo productionPoolConfig) {
        TreeSet<LocalDate> processPlanDates = Sets.newTreeSet();
        int processPlanPeriod = productionPoolConfig.getProductionCycle();
        if (processPlanPeriod <= 0) {
            throw new ParamIllegalException("请配置合理的排产周期！当前排产周期天数：{}", processPlanPeriod);
        }

        final Set<LocalDate> holidays = productionPoolConfig.getHolidays();
        LocalDate processPlanDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
        while (processPlanDates.size() != processPlanPeriod) {
            if (!holidays.contains(processPlanDate)) {
                processPlanDates.add(processPlanDate);
            }
            processPlanDate = processPlanDate.plus(1, ChronoUnit.DAYS);
        }
        return processPlanDates;
    }

    /**
     * 构建员工产能池。
     *
     * @param employeeNos 员工编号的集合。
     * @return 员工生产能力池的列表。
     */
    public List<EmployeeProcessAbilityPo> buildProductionSchedulePool(Set<String> employeeNos) {
        if (CollectionUtils.isEmpty(employeeNos)) {
            throw new BizException("初始化产能池失败，产能池员工未配置");
        }
        return employeeProcessAbilityDao.getEmployeeProcessAbilityPoList(employeeNos);
    }

    /**
     * 获取人员工序能力信息列表。
     *
     * @param employeeNos 员工编号的集合。
     * @return 人员工序能力信息的列表。
     */
    public List<EmployeeProcessAbilityPo> getEmployeeProcessAbilities(Set<String> employeeNos) {
        if (CollectionUtils.isEmpty(employeeNos)) {
            throw new BizException("获取人员工序能力信息失败，未指定员工编号");
        }
        return employeeProcessAbilityDao.getEmployeeProcessAbilityPoList(employeeNos);
    }


    /**
     * 扣除员工产能。
     *
     * @param processProcedureEmployeePlan 加工工序员工计划信息。
     * @param productionPoolConfig         生产池配置信息。
     * @param capacityPool                 员工生产能力池的列表。
     * @return 更新后的员工生产能力池列表。
     */
    public List<EmployeeProcessAbilityBo> deductCapacity(ProcessProcedureEmployeePlanBo processProcedureEmployeePlan,
                                                         ProductionPoolConfigBo productionPoolConfig,
                                                         List<EmployeeProcessAbilityBo> capacityPool,
                                                         StringBuilder processOrderLogs) {
        if (Objects.isNull(productionPoolConfig)) {
            throw new BizException("扣减产能失败！产能池配置信息缺失");
        }
        if (Objects.isNull(processProcedureEmployeePlan)) {
            throw new BizException("扣减产能失败！排产计划信息缺失");
        }
        if (CollectionUtils.isEmpty(capacityPool)) {
            throw new BizException("扣减产能失败！产能池为空");
        }

        final BigDecimal workHourDuration = productionPoolConfig.getWorkHourDuration();
        final BigDecimal workMinutesDuration = workHourDuration.multiply(new BigDecimal(60));
        final String processOrderNo = processProcedureEmployeePlan.getProcessOrderNo();
        final Long processId = processProcedureEmployeePlan.getProcessId();
        final String employeeNo = processProcedureEmployeePlan.getEmployeeNo();
        final LocalDateTime expectBeginTime = processProcedureEmployeePlan.getExpectBeginDateTime();
        final LocalDateTime expectEndTime = processProcedureEmployeePlan.getExpectEndDateTime();

        // 区间内每日时长（分钟）
        TreeSet<DailyWorkHoursBo> dailyWorkHoursBos = calculateWorkDuration(expectBeginTime, expectEndTime, productionPoolConfig);
        for (DailyWorkHoursBo dailyWorkHoursBo : dailyWorkHoursBos) {
            final LocalDate processPlanDate = dailyWorkHoursBo.getProcessPlanDate();
            long durationMinutes = dailyWorkHoursBo.getDurationMinutes();

            // 匹配到工序人员 & 匹配人员能做的所有工序（用于扣减共享产能）
            EmployeeProcessAbilityBo matchProcessEmployee = capacityPool.stream().filter(capacity ->
                    Objects.equals(processPlanDate, capacity.getValidityTime().toLocalDate())
                            && Objects.equals(processId, capacity.getProcessId())
                            && Objects.equals(employeeNo, capacity.getEmployeeNo())).findFirst().orElse(null);
            if (Objects.isNull(matchProcessEmployee)) {
                log.info("扣减产能失败！无法根据加工单号：{} 员工编号：{} 工序id：{} 匹配到对应的产能池，本次跳过扣减", processOrderNo, employeeNo, processId);
                return capacityPool;
            }
            List<EmployeeProcessAbilityBo> matchEmployee = capacityPool.stream().filter(capacity ->
                    Objects.equals(processPlanDate, capacity.getValidityTime().toLocalDate())
                            && Objects.equals(employeeNo, capacity.getEmployeeNo())
            ).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(matchEmployee)) {
                log.info("扣减产能失败！无法根据加工单号：{} 员工编号：{} 匹配到对应的产能池，本次跳过扣减", processOrderNo, employeeNo);
                return capacityPool;
            }

            // 匹配到当前工序的总产能
            BigDecimal currentProcessCapacity = new BigDecimal(Objects.isNull(matchProcessEmployee.getTotalProcessedNum()) ? 0 : matchProcessEmployee.getTotalProcessedNum());
            BigDecimal currentProcessNum = new BigDecimal(durationMinutes).multiply(currentProcessCapacity).divide(workMinutesDuration, 0, RoundingMode.CEILING);
            matchEmployee.forEach(matchEm -> {
                BigDecimal availableProcessedNum = new BigDecimal(Objects.isNull(matchEm.getAvailableProcessedNum()) ? 0 : matchEm.getAvailableProcessedNum());
                BigDecimal totalProcessedNum = new BigDecimal(Objects.isNull(matchEm.getTotalProcessedNum()) ? 0 : matchEm.getTotalProcessedNum());

                // 计算本次需要扣减的产能数
                BigDecimal subProcessNum = ((totalProcessedNum.multiply(currentProcessNum)).divide(currentProcessCapacity, 0, RoundingMode.CEILING));
                BigDecimal finalAvailableProcessedNum = availableProcessedNum.subtract(subProcessNum);
                if (finalAvailableProcessedNum.compareTo(BigDecimal.ZERO) <= 0) {
                    matchEm.setAvailableProcessedNum(0);
                } else {
                    matchEm.setAvailableProcessedNum(finalAvailableProcessedNum.intValue());
                }
                appendDeductCapacityLog(processOrderLogs, matchEm, availableProcessedNum, subProcessNum);
            });
        }
        processOrderLogs.append(System.lineSeparator());
        return capacityPool;
    }

    /**
     * 计算每日工作时长。
     *
     * @param expectedStartTime    预期开始时间
     * @param expectedEndTime      预期结束时间
     * @param productionPoolConfig 产能池配置信息
     * @return 包含每日工作小时数的TreeSet集合
     */
    public TreeSet<DailyWorkHoursBo> calculateWorkDuration(LocalDateTime expectedStartTime,
                                                           LocalDateTime expectedEndTime,
                                                           ProductionPoolConfigBo productionPoolConfig) {
        final Set<LocalDate> holidays = productionPoolConfig.getHolidays();
        final LocalTime workStart = productionPoolConfig.getStartTime();
        final LocalTime workEnd = productionPoolConfig.getEndTime();
        final List<BreakTimeSegmentBo> breakTimeSegments = productionPoolConfig.getBreakTimeSegments();
        final BreakTimeSegmentBo breakTimeSegmentBo = breakTimeSegments.stream().findFirst().orElse(null);
        final LocalTime lunchStart = Objects.isNull(breakTimeSegmentBo) ? null : breakTimeSegmentBo.getStartTime();
        final LocalTime lunchEnd = Objects.isNull(breakTimeSegmentBo) ? null : breakTimeSegmentBo.getEndTime();

        TreeSet<WorkDurationBo> workDurationBos
                = ScmTimeUtil.calculateDailyWorkDuration(expectedStartTime, expectedEndTime, workStart, workEnd, lunchStart, lunchEnd, holidays);
        if (CollectionUtils.isEmpty(workDurationBos)) {
            return new TreeSet<>(Collections.emptySet());
        }
        return workDurationBos.stream().map(workDurationBo -> {
            DailyWorkHoursBo dailyWorkHoursBo = new DailyWorkHoursBo();
            dailyWorkHoursBo.setProcessPlanDate(workDurationBo.getDate());
            dailyWorkHoursBo.setDurationMinutes(workDurationBo.getDuration().toMinutes());
            return dailyWorkHoursBo;
        }).collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DailyWorkHoursBo::getProcessPlanDate))));
    }

    /**
     * 计算下一道工序的开始时间。
     *
     * @param expectedStartTime 预期工序开始时间
     * @param setupDuration     工序整备时长
     * @param workStartTime     工作开始时间
     * @param workEndTime       工作结束时间
     * @param breakTimeSegments 多个休息时间段的列表
     * @param holidays          节假日的集合，需要跳过的日期
     * @return 下一道工序的开始时间
     */
    public LocalDateTime calculateNextStartTime(LocalDateTime expectedStartTime, Integer setupDuration, LocalTime workStartTime,
                                                LocalTime workEndTime, List<BreakTimeSegmentBo> breakTimeSegments, Set<LocalDate> holidays) {
        // 根据上班时间、结束时间、多段休息时间段获取工作时间
        List<TimeRangeBo> breakTimeRanges
                = breakTimeSegments.stream().map(breakTimeSegmentBo -> new TimeRangeBo(breakTimeSegmentBo.getStartTime(), breakTimeSegmentBo.getEndTime())).collect(Collectors.toList());
        TreeSet<TimeRangeBo> timeRangeBos = ScmTimeUtil.calculateWorkTime(workStartTime, workEndTime, breakTimeRanges);

        return ScmTimeUtil.calculateNextStartTime(expectedStartTime, setupDuration, holidays, timeRangeBos);
    }

    /**
     * @Description 加工单排产日志记录
     * @author yanjiawei
     * @Date 2023/9/3 23:44
     */
    public void logProcessPlanOrders(String productionPoolCode, OverPlan overPlan, Set<ProcessOrderType> orderTypes,
                                     List<ProcessPlanOrderBo> ordersToProcessPlan, String bizTrace) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(System.lineSeparator());
        logBuilder.append(StrUtil.format("【筛选加工单并排序】 产能池编号：{}，是否超额：{}，订单类型：{} 按产能指数从高到低，相同产能指数创建时间最早排序：",
                productionPoolCode, overPlan, orderTypes));

        if (CollectionUtils.isNotEmpty(ordersToProcessPlan)) {
            for (int index = 0; index < ordersToProcessPlan.size(); index++) {
                ProcessPlanOrderBo order = ordersToProcessPlan.get(index);
                logBuilder.append(System.lineSeparator());
                logBuilder.append(StrUtil.format("序号：{}.加工单号：{} 产能指数:{} 创建时间:{}",
                        (index + 1), order.getProcessOrderNo(), order.getCapacityNum(),
                        order.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
        }
        stringBuilderLoggerBaseService.logStringBuilderContent(logBuilder, bizTrace);
    }

    /**
     * 记录完成的加工单排产计划的操作日志信息。
     *
     * @param productionPoolCode  产能池编号
     * @param overPlan            是否超额
     * @param orderTypes          订单类型集合
     * @param ordersToProcessPlan 待排产的加工单列表
     * @param bizTrace            业务追踪信息
     */
    public void logCompleteProcessPlanOrders(String productionPoolCode, OverPlan overPlan, Set<ProcessOrderType> orderTypes,
                                             List<ProcessPlanOrderBo> ordersToProcessPlan, String bizTrace) {
        // 创建一个 StringBuilder 用于构建日志信息
        StringBuilder logBuilder = new StringBuilder();

        // 在日志中添加一行新行，以分隔不同的日志记录
        logBuilder.append(System.lineSeparator());

        // 格式化并记录产能池、是否超额和订单类型等信息
        logBuilder.append(StrUtil.format("【排产结果展示】 产能池编号：{}，是否超额：{}，订单类型：{} 按产能指数从高到低，相同产能指数创建时间最早排序：",
                productionPoolCode, overPlan, orderTypes));

        if (CollectionUtils.isNotEmpty(ordersToProcessPlan)) {
            // 先对列表进行排序，将 successPlan = true 的放在前面
            List<ProcessPlanOrderBo> sortedOrders = ordersToProcessPlan.stream()
                    .sorted(Comparator.comparing(ProcessPlanOrderBo::isSuccessPlan).reversed())
                    .collect(Collectors.toList());

            for (int index = 0; index < sortedOrders.size(); index++) {
                ProcessPlanOrderBo order = sortedOrders.get(index);
                logBuilder.append(System.lineSeparator());
                logBuilder.append(StrUtil.format("序号：{}.加工单号：{} 产能指数:{} 创建时间:{} 成功排产：{}",
                        (index + 1), order.getProcessOrderNo(), order.getCapacityNum(),
                        order.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), order.isSuccessPlan()));
            }
        }

        // 使用 stringBuilderLoggerBaseService 记录日志内容，并传入业务追踪信息
        stringBuilderLoggerBaseService.logStringBuilderContent(logBuilder, bizTrace);
    }


    /**
     * 将生产计划日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs    用于存储日志的 StringBuilder 对象
     * @param productionSchedules 生产计划列表
     * @param title               日志标题
     */
    public void appendProductionSchedulesLog(StringBuilder processOrderLogs, List<ProcessProcedureEmployeePlanBo> productionSchedules, String title) {
        // 在 StringBuilder 中追加标题和新行
        processOrderLogs.append(System.lineSeparator()).append(title).append(System.lineSeparator());

        // 遍历生产计划列表并生成相应的日志信息
        for (int i = 0; i < productionSchedules.size(); i++) {
            ProcessProcedureEmployeePlanBo schedule = productionSchedules.get(i);
            processOrderLogs.append(String.format("序号：%d，加工单号：%s，工序id：%s，工序名称：%s，员工编号：%s，员工名称：%s，开始时间：%s，结束时间：%s",
                            i + 1, schedule.getProcessOrderNo(), schedule.getProcessId(),
                            schedule.getProcessName(), schedule.getEmployeeNo(), schedule.getEmployeeName(),
                            schedule.getExpectBeginDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            schedule.getExpectEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                    .append(System.lineSeparator());
        }

        // 在 StringBuilder 中追加一个新行
        processOrderLogs.append(System.lineSeparator());
    }

    /**
     * 将扣减产能日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs      用于存储日志的 StringBuilder 对象
     * @param matchEm               匹配的员工工序信息
     * @param availableProcessedNum 原剩余产能数
     * @param subProcessNum         本次产能数
     */
    public void appendDeductCapacityLog(StringBuilder processOrderLogs, EmployeeProcessAbilityBo matchEm, BigDecimal availableProcessedNum, BigDecimal subProcessNum) {
        // 在 StringBuilder 中追加一个新行，并格式化扣减产能的日志消息
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【扣减产能】员工编号：{}，员工名称：{}，工序id：{}，工序名称：{}，原剩余产能数：{}，本次产能数：{}，扣减后剩余产能数：{}",
                        matchEm.getEmployeeNo(), matchEm.getEmployeeName(), matchEm.getProcessId(), matchEm.getProcessName(),
                        availableProcessedNum.intValue(), subProcessNum.intValue(), matchEm.getAvailableProcessedNum()));
    }


    /**
     * 将更新下一道工序开始时间的操作日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs  用于存储日志的 StringBuilder 对象
     * @param expectEndDateTime 预计结束时间
     * @param setupDuration     整备时长（分钟）
     * @param expectBeginTime   预计下一道工序开始时间
     */
    public void appendUpdateNextProcessStartTimeLog(StringBuilder processOrderLogs, LocalDateTime expectEndDateTime, int setupDuration, LocalDateTime expectBeginTime) {
        // 在 StringBuilder 中追加一个新行，并格式化更新下一道工序开始时间的日志消息
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【更新下一道工序开始时间】当前工序结束时间：{} 整备时长（分钟）：{} 下一道工序开始时间：{}",
                        expectEndDateTime, setupDuration, expectBeginTime))
                .append(System.lineSeparator());
    }

    /**
     * 将筛选工序员工失败的操作日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param employeeNo       员工编号
     * @param employeeName     员工名称
     */
    public void appendFailEmployeeLog(StringBuilder processOrderLogs, String employeeNo, String employeeName) {
        // 格式化筛选工序员工失败的日志消息，并追加到 StringBuilder 中
        processOrderLogs.append(StrUtil.format("【筛选工序员工】选中员工编号：{}，员工名称：{} 失败！", employeeNo, employeeName))
                .append(System.lineSeparator());
    }

    /**
     * 将更新员工已排产单量的操作日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param employeeNo       员工编号
     */
    public void appendUpdateEmployeeProcessedCountLog(StringBuilder processOrderLogs, String employeeNo) {
        // 在 StringBuilder 中追加一个新行，并格式化更新员工已排产单量的日志消息
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【更新员工已排产单量】员工编号：{} +1", employeeNo));
    }

    /**
     * 将开始执行加工单排产计划的操作日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param processOrderNo   加工单号
     * @param strategy         执行策略
     */
    public void appendStartProcessPlanLog(StringBuilder processOrderLogs, String processOrderNo, String strategy) {
        // 在 StringBuilder 中追加一个新行，并格式化开始执行加工单排产计划的日志消息
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【开始执行加工单排产计划】{} 策略 加工单号：{}", strategy, processOrderNo));
    }


    /**
     * 将加工单排产计划执行结果追加到日志中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param processOrderNo   加工单号
     * @param result           执行结果
     */
    public void appendProcessPlanResultLog(StringBuilder processOrderLogs, String processOrderNo, String result) {
        processOrderLogs.append(System.lineSeparator()).append(StrUtil.format("【结束执行加工单排产计划】{} 加工单号：{}", result, processOrderNo));
    }

    /**
     * 将筛选工序员工的日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param processId        工序ID
     * @param processName      工序名称
     * @param totalProcessNum  加工数量
     */
    public void appendFilterEmployeeLog(StringBuilder processOrderLogs, Long processId, String processName, int totalProcessNum) {
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【筛选工序员工】根据工序id：{}，工序名称：{}，加工数量：{}，匹配员工：",
                        processId, processName, totalProcessNum));
    }

    /**
     * 将选中的工序员工的日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param employeeNo       员工编号
     * @param employeeName     员工名称
     */
    public void appendSelectEmployeeLog(StringBuilder processOrderLogs, String employeeNo, String employeeName) {
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【筛选工序员工】选中员工编号：{}，员工名称：{}", employeeNo, employeeName));
    }

    /**
     * 将生成计划时间的日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param startTime        预计开始时间
     * @param totalMinutes     耗时（分钟）
     * @param endTime          预计结束时间
     * @param isFinal          是否最终计算
     */
    public void appendGeneratePlanTimeLog(StringBuilder processOrderLogs, LocalDateTime startTime, int totalMinutes, LocalDateTime endTime, boolean isFinal) {
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【生成计划时间】 根据预计开始时间：{}，耗时（分钟）：{}，{}{}",
                        startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        totalMinutes,
                        isFinal ? "最终计算得到预计结束时间：" : "初步计算得到预计结束时间：",
                        endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .append(System.lineSeparator());
    }

    /**
     * 将生成最终计划时间的操作日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs         用于存储日志的 StringBuilder 对象
     * @param finalExpectBeginDateTime 最终预计开始时间
     * @param totalMinutes             预计耗时（分钟）
     * @param finalExpectEndDateTime   最终预计结束时间
     */
    public void appendFinalPlanTimeLog(StringBuilder processOrderLogs, LocalDateTime finalExpectBeginDateTime, int totalMinutes, LocalDateTime finalExpectEndDateTime) {
        // 在 StringBuilder 中追加一个新行，并格式化生成最终计划时间的日志消息
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【生成计划时间】 根据预计开始时间：{}，耗时（分钟）：{}，得到最终预计结束时间：{}，当前已超过排产周期的截止时间！",
                        finalExpectBeginDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        totalMinutes,
                        finalExpectEndDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .append(System.lineSeparator());
    }

    /**
     * 将生成计划时间冲突的操作日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param expectBeginTime  预计开始时间
     * @param expectEndTime    预计结束时间
     * @param existBeginTime   不能排产开始时间
     * @param existEndTime     不能排产结束时间
     */
    public void appendConflictPlanTimeLog(StringBuilder processOrderLogs, LocalDateTime expectBeginTime, LocalDateTime expectEndTime, LocalDateTime existBeginTime, LocalDateTime existEndTime) {
        // 在 StringBuilder 中追加一个新行，并格式化生成计划时间冲突的日志消息
        processOrderLogs.append(System.lineSeparator())
                .append(StrUtil.format("【生成计划时间】 排产计划（{} ~ {}） 与 （{} ~ {}）产生冲突！重新计算新的预计开始时间和预计结束时间",
                        expectBeginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        expectEndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        existBeginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        existEndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
    }

    /**
     * 将员工信息的操作日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param employees        员工信息列表
     */
    public void appendEmployeeInfoLog(StringBuilder processOrderLogs, List<ProcessPlanEmployeeBo> employees) {
        int serialNumber = 1;
        for (ProcessPlanEmployeeBo employee : employees) {
            processOrderLogs.append(System.lineSeparator());
            processOrderLogs.append(StrUtil.format("序号：{}，员工编号：{}，员工名称：{}，总产能数：{}，剩余产能数：{}，已排单量：{}，职级系数：{}，开始时间: {}， 结束时间: {}；",
                    serialNumber, employee.getEmployeeNo(), employee.getEmployeeName(), employee.getProcessTotalCapacity(),
                    employee.getAvailableProcessedNum(), employee.getCompletePlanCount(), employee.getGradeLevel(),
                    employee.getExpectBeginDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    employee.getExpectEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            serialNumber++;
        }
    }

    /**
     * 将筛选工序员工产能信息的操作日志信息追加到 StringBuilder 中。
     *
     * @param processOrderLogs 用于存储日志的 StringBuilder 对象
     * @param employees        筛选出的员工产能信息列表
     */
    public void appendFilterEmployeeProcessAbilityInfoLog(StringBuilder processOrderLogs, List<EmployeeProcessAbilityBo> employees) {
        // 在 StringBuilder 中追加一个新行，标记筛选工序员工产能信息的日志消息
        processOrderLogs.append(System.lineSeparator()).append("【筛选工序员工】 根据单量少、职级低优先级排序：");
        int serialNumber = 1;
        for (EmployeeProcessAbilityBo employee : employees) {
            processOrderLogs.append(System.lineSeparator());
            processOrderLogs.append(StrUtil.format("序号：{}，员工编号：{}，员工名称：{}，工序id：{}，工序名称：{}，总产能数：{}，剩余产能数：{}，已排单量：{}，职级系数：{}，产能日期：{};",
                    serialNumber, employee.getEmployeeNo(), employee.getEmployeeName(), employee.getProcessId(), employee.getProcessName(),
                    employee.getTotalProcessedNum(), employee.getAvailableProcessedNum(), employee.getCompletePlanCount(),
                    employee.getGradeLevel(), employee.getValidityTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            serialNumber++;
        }
        processOrderLogs.append(System.lineSeparator());
    }

}

















