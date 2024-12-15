package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmAmountCalculateUtil;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.CommissionAmountBo;
import com.hete.supply.scm.server.scm.entity.po.EmployeeProcessAbilityPo;
import com.hete.supply.scm.server.scm.process.converter.CommissionDetailConverter;
import com.hete.supply.scm.server.scm.process.converter.ProcessCommissionRuleConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessCompositePo;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.enums.CommissionCategory;
import com.hete.supply.scm.server.scm.process.enums.ProcessStage;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工序扫码单基础类
 *
 * @author ChenWenLong
 * @date 2022/11/15 10:08
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProcessOrderScanBaseService {

    private final ProcessOrderScanDao processOrderScanDao;
    private final ProcessOrderProcedureDao processOrderProcedureDao;
    private final ProcessOrderItemDao processOrderItemDao;
    private final ProcessDao processDao;
    private final ProcessUserDao processUserDao;
    private final ProcessOrderDao processOrderDao;
    private final ProcessProcedureEmployeePlanDao processProcedureEmployeePlanDao;
    private final ProcessPlanBaseService processPlanBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final ProcessMaterialReceiptDao processMaterialReceiptDao;
    private final ScanCommissionDetailDao scanCommissionDetailDao;
    private final ProcessBaseService processBaseService;
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private final ProcessOrderScanRelateDao processOrderScanRelateDao;
    private final ProcessCompositeDao processCompositeDao;
    private final CommissionDetailCalculationStrategyFactory commDetCalcStrgyFactory;

    /**
     * 通过用户编码和时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/21 20:02
     */
    public List<ProcessOrderScanPo> getByCompleteUserAndTime(String completeUser,
                                                             LocalDateTime completeTime,
                                                             LocalDateTime completeTimeStart,
                                                             LocalDateTime completeTimeEnd) {
        return processOrderScanDao.getByCompleteUserAndTime(completeUser, completeTime, completeTimeStart,
                completeTimeEnd, ScmConstant.SYSTEM_USER);
    }

    /**
     * 完成扫码
     *
     * @param processOrderNo
     */
    @Transactional(rollbackFor = Exception.class)
    public void completeScan(String processOrderNo) {
        // 需要完成的一级工序
        ArrayList<ProcessFirst> processFirsts = new ArrayList<>();
        processFirsts.add(ProcessFirst.WAIT_HANDLE);
        processFirsts.add(ProcessFirst.HANDLING);
        processFirsts.add(ProcessFirst.HANDLED);

        if (CollectionUtils.isNotEmpty(processFirsts)) {
            ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
            // 加工成品详情
            List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(processOrderNo);
            Optional<ProcessOrderItemPo> first = processOrderItemPos.stream()
                    .filter(item -> BooleanType.TRUE.equals(item.getIsFirst()))
                    .findFirst();
            if (first.isEmpty()) {
                throw new BizException("加工单产品详情不存在");
            }

            // 加工工序
            List<ProcessOrderProcedurePo> processOrderProcedurePos = processOrderProcedureDao.getByProcessOrderNo(
                    processOrderNo);


            // 存在加工工序
            if (CollectionUtils.isNotEmpty(processOrderProcedurePos)) {

                List<Long> needProcessIds = processOrderProcedurePos.stream()
                        .map(ProcessOrderProcedurePo::getProcessId)
                        .collect(Collectors.toList());
                // 工序
                List<ProcessPo> processPos = processDao.getByProcessIds(needProcessIds);
                Map<Long, ProcessPo> groupedProcessPos = processPos.stream()
                        .collect(Collectors.toMap(ProcessPo::getProcessId, item -> item,
                                (exitingItem, newItem) -> exitingItem));

                // 判断是否有权限操作
                List<ProcessUserPo> processUserPos = processUserDao.getByProcessIds(needProcessIds);
                if (CollectionUtils.isEmpty(processUserPos)) {
                    throw new ParamIllegalException("您不允许完成加工");
                }
                Map<Long, List<ProcessUserPo>> groupedUserPos = processUserPos.stream()
                        .collect(Collectors.groupingBy(ProcessUserPo::getProcessId));

                processOrderProcedurePos.forEach(po -> {
                    ProcessPo processPo = groupedProcessPos.get(po.getProcessId());
                    Assert.notNull(processPo, () -> new BizException("工序不存在"));
                    String processName = processPo.getProcessLabel()
                            .getRemark() + "_" + processPo.getProcessSecondName();

                    List<ProcessUserPo> newProcessUserPoList = groupedUserPos.get(po.getProcessId());
                    Assert.notEmpty(newProcessUserPoList,
                            () -> new ParamIllegalException("您不允许完成工序：{}", processName));
                    List<String> userCodes = newProcessUserPoList.stream()
                            .map(ProcessUserPo::getUserCode)
                            .collect(Collectors.toList());
                    if (!userCodes.contains(GlobalContext.getUserKey())) {
                        throw new ParamIllegalException("您不允许完成工序：{}", processName);
                    }
                });


                // 加工扫码记录
                List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.getByProcessOrderNo(processOrderNo);
                Map<Long, ProcessOrderScanPo> groupedScanPo = processOrderScanPos.stream()
                        .collect(Collectors.toMap(ProcessOrderScanPo::getProcessOrderProcedureId, item -> item,
                                (exitingItem, newItem) -> exitingItem));

                List<ProcessOrderScanPo> newProcessOrderScanPos = processOrderProcedurePos.stream()
                        .filter(item -> {
                            // 已经完成的扫码，不需要进行处理
                            ProcessOrderScanPo processOrderScanPo = groupedScanPo.get(
                                    item.getProcessOrderProcedureId());
                            return null == processOrderScanPo || null == processOrderScanPo.getCompleteTime();
                        })
                        .map(it -> {
                            ProcessOrderScanPo newProcessOrderScanPo = new ProcessOrderScanPo();

                            Optional<ProcessPo> processPoFirst = processPos.stream()
                                    .filter(it2 -> it2.getProcessId()
                                            .equals(it.getProcessId()))
                                    .findFirst();
                            ProcessPo processPo = processPoFirst.get();

                            // 解析接货数
                            Integer receiptNum = 0;
                            Optional<ProcessOrderScanPo> firstScanOptional = processOrderScanPos.stream()
                                    .min(Comparator.comparing(ProcessOrderScanPo::getCreateTime));
                            if (firstScanOptional.isPresent()) {
                                ProcessOrderScanPo firstProcessOrderScanPo = firstScanOptional.get();
                                receiptNum
                                        =
                                        firstProcessOrderScanPo.getReceiptNum() + processOrderPo.getAvailableProductNum();
                            } else {
                                Optional<ProcessOrderItemPo> firstOrderItemPo = processOrderItemPos.stream()
                                        .findFirst();
                                if (firstOrderItemPo.isPresent()) {
                                    receiptNum = firstOrderItemPo.get()
                                            .getProcessNum();
                                }
                            }

                            ProcessOrderScanPo processOrderScanPo = groupedScanPo.get(it.getProcessOrderProcedureId());

                            newProcessOrderScanPo.setProcessOrderProcedureId(it.getProcessOrderProcedureId());
                            newProcessOrderScanPo.setProcessOrderNo(it.getProcessOrderNo());
                            newProcessOrderScanPo.setProcessFirst(processPo.getProcessFirst());
                            newProcessOrderScanPo.setProcessCode(processPo.getProcessCode());
                            newProcessOrderScanPo.setProcessName(processPo.getProcessName());
                            newProcessOrderScanPo.setProcessCommission(processPo.getCommission());
                            newProcessOrderScanPo.setExtraCommission(processPo.getExtraCommission());
                            newProcessOrderScanPo.setReceiptNum(receiptNum);
                            newProcessOrderScanPo.setPlatform(processOrderPo.getPlatform());
                            newProcessOrderScanPo.setOrderTime(processOrderPo.getCreateTime());
                            newProcessOrderScanPo.setOrderUser(processOrderPo.getCreateUser());
                            newProcessOrderScanPo.setOrderUsername(processOrderPo.getCreateUsername());
                            newProcessOrderScanPo.setReceiptTime(new DateTime().toLocalDateTime());
                            newProcessOrderScanPo.setReceiptUser(GlobalContext.getUserKey());
                            newProcessOrderScanPo.setReceiptUsername(GlobalContext.getUsername());
                            newProcessOrderScanPo.setCompleteTime(new DateTime().toLocalDateTime());
                            newProcessOrderScanPo.setCompleteUser(GlobalContext.getUserKey());
                            newProcessOrderScanPo.setCompleteUsername(GlobalContext.getUsername());
                            newProcessOrderScanPo.setQualityGoodsCnt(receiptNum);
                            newProcessOrderScanPo.setDefectiveGoodsCnt(0);
                            // 已经存在扫码记录
                            if (null != processOrderScanPo) {
                                newProcessOrderScanPo.setProcessOrderScanId(processOrderScanPo.getProcessOrderScanId());
                                newProcessOrderScanPo.setReceiptNum(processOrderScanPo.getReceiptNum());
                                newProcessOrderScanPo.setReceiptUsername(processOrderScanPo.getReceiptUsername());
                                newProcessOrderScanPo.setReceiptUser(processOrderScanPo.getReceiptUser());
                                newProcessOrderScanPo.setReceiptTime(processOrderScanPo.getReceiptTime());
                            }
                            newProcessOrderScanPo.setCompleteTime(new DateTime().toLocalDateTime());
                            newProcessOrderScanPo.setCompleteUser(GlobalContext.getUserKey());
                            newProcessOrderScanPo.setCompleteUsername(GlobalContext.getUsername());
                            newProcessOrderScanPo.setQualityGoodsCnt(receiptNum);
                            newProcessOrderScanPo.setDefectiveGoodsCnt(0);

                            return newProcessOrderScanPo;

                        })
                        .collect(Collectors.toList());

                CompareResult<ProcessOrderScanPo> result = DataCompareUtil.compare(newProcessOrderScanPos,
                        processOrderScanPos,
                        ProcessOrderScanPo::getProcessOrderScanId);
                processOrderScanDao.insertBatch(result.getNewItems());
                processOrderScanDao.updateBatchByIdVersion(result.getExistingItems());

            }

        }

    }

    /**
     * 确认接货
     *
     * @param processOrderProcedureId
     * @param receiptNum
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessOrderPo confirmReceive(Long processOrderProcedureId, Integer receiptNum) {
        ProcessOrderProcedurePo queryProcessOrderProcedurePo = ParamValidUtils.requireNotNull(
                processOrderProcedureDao.getById(processOrderProcedureId),
                "加工工序不存在"
        );

        final String processOrderNo = queryProcessOrderProcedurePo.getProcessOrderNo();
        ProcessOrderPo processOrderPo = ParamValidUtils.requireNotNull(
                processOrderDao.getByProcessOrderNo(processOrderNo),
                StrUtil.format("加工单{}信息不存在", processOrderNo)
        );

        Integer sort = processOrderPo.getProcessOrderStatus().getSort();
        if (!ProcessOrderStatus.WAIT_PRODUCE.getSort().equals(sort) && !ProcessOrderStatus.REWORKING.getSort().equals(sort) && !ProcessOrderStatus.PROCESSING.getSort().equals(sort)) {
            throw new ParamIllegalException("只能待投产/加工中/返工中的单才能接货");
        }

        List<ProcessDeliveryOrderVo> processDeliveryOrderVoList
                = wmsRemoteService.getProcessDeliveryOrder(processOrderNo, WmsEnum.DeliveryType.PROCESS);
        List<ProcessMaterialReceiptPo> processMaterialReceiptPoList
                = processMaterialReceiptDao.getByProcessOrderNo(processOrderNo);

        int pendingDeliveryCount = (int) processDeliveryOrderVoList.stream()
                .filter(it -> !it.getDeliveryState()
                        .equals(WmsEnum.DeliveryState.SIGNED_OFF) && !it.getDeliveryState()
                        .equals(WmsEnum.DeliveryState.FINISHED) && !it.getDeliveryState()
                        .equals(WmsEnum.DeliveryState.CANCELING) && !it.getDeliveryState()
                        .equals(WmsEnum.DeliveryState.CANCELED))
                .count();
        int waitReceiptCount = (int) processMaterialReceiptPoList.stream()
                .filter(it -> ProcessMaterialReceiptStatus.WAIT_RECEIVE.equals(it.getProcessMaterialReceiptStatus()))
                .count();
        if (pendingDeliveryCount == 0 && waitReceiptCount == 0) {
            processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.TRUE);
        }

        IsReceiveMaterial isReceiveMaterial = processOrderPo.getIsReceiveMaterial();
        if (Objects.equals(IsReceiveMaterial.FALSE, isReceiveMaterial)) {
            throw new ParamIllegalException("加工单原料未收货，不可接货");
        }

        ProcessOrderScanPo processOrderScanPo = processOrderScanDao.getByProcessOrderProcedureId(processOrderProcedureId);
        if (null != processOrderScanPo) {
            throw new ParamIllegalException("此加工工序已接货");
        }

        List<ProcessOrderScanPo> processOrderScanPoList
                = Optional.ofNullable(processOrderScanDao.getByProcessOrderNo(processOrderNo)).orElse(Collections.emptyList());

        Integer maxAvailableReceiptNum = 0;
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        Optional<ProcessOrderScanPo> firstScanOptional = processOrderScanPoList.stream().min(Comparator.comparing(ProcessOrderScanPo::getCreateTime));
        if (firstScanOptional.isPresent()) {
            ProcessOrderScanPo firstProcessOrderScanPo = firstScanOptional.get();
            maxAvailableReceiptNum = firstProcessOrderScanPo.getReceiptNum() + processOrderPo.getAvailableProductNum();
            if (!maxAvailableReceiptNum.equals(receiptNum)) {
                throw new ParamIllegalException("接货数必须等于：{}", maxAvailableReceiptNum);
            }
        } else {
            Optional<ProcessOrderItemPo> firstOrderItemPo = processOrderItemPos.stream().findFirst();
            if (firstOrderItemPo.isPresent()) {
                maxAvailableReceiptNum = firstOrderItemPo.get().getProcessNum();
            }
            if (receiptNum > maxAvailableReceiptNum) {
                throw new ParamIllegalException("最大接货数：{}", maxAvailableReceiptNum);
            }
        }

        List<ProcessOrderProcedurePo> processOrderProcedurePos
                = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        List<ProcessOrderScanPo> processOrderScanPos
                = Optional.ofNullable(processOrderScanDao.getByProcessOrderNo(processOrderNo)).orElse(new ArrayList<>());
        // 过滤中加工单中未完成的扫码记录
        long imCompleteCount = processOrderScanPos.stream().filter(it -> null == it.getCompleteTime()).count();
        if (imCompleteCount > 0) {
            throw new ParamIllegalException("必须完成上一道工序才能接货");
        }
        // 过滤需要进行扫码的工序
        List<ProcessOrderProcedurePo> needScanProcedurePos = processOrderProcedurePos.stream()
                .filter(it -> {
                    List<Long> processOrderProcedureIds = processOrderScanPos.stream()
                            .map(ProcessOrderScanPo::getProcessOrderProcedureId)
                            .collect(Collectors.toList());
                    return !processOrderProcedureIds.contains(it.getProcessOrderProcedureId());
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(needScanProcedurePos)) {
            throw new BizException("没有需要进行扫码的工序");
        }
        ProcessOrderProcedurePo processOrderProcedurePo = needScanProcedurePos.stream().findFirst().get();
        if (!processOrderProcedureId.equals(processOrderProcedurePo.getProcessOrderProcedureId())) {
            throw new ParamIllegalException("请按照工序顺序扫码");
        }

        ProcessPo processPo = processDao.getByProcessCode(processOrderProcedurePo.getProcessCode());
        if (null == processPo) {
            throw new BizException("工序不存在");
        }
        // 当订单需要排产，校验排产的员工是否等于当前用户
        if (NeedProcessPlan.TRUE.equals(processOrderPo.getNeedProcessPlan())) {
            ProcessProcedureEmployeePlanPo processProcedureEmployeePlanPo
                    = processProcedureEmployeePlanDao.getByProcessOrderProcedureId(processOrderProcedureId);
            if (Objects.isNull(processProcedureEmployeePlanPo)) {
                throw new BizException("当前加工单是排产订单，需有排产计划方可进行接货");
            }
            if (!Objects.equals(GlobalContext.getUserKey(), processProcedureEmployeePlanPo.getEmployeeNo())) {
                throw new BizException("该排产加工单已有对应接货人，您不允许接货！");
            }
            LocalDateTime now = new DateTime().toLocalDateTime();
            processProcedureEmployeePlanPo.setActBeginTime(now);
            log.info("开始接货！更新排产加工单工序的实际开始时间，加工单工序Id：{}，实际开始加工时间：{}",
                    processOrderProcedureId, now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            processProcedureEmployeePlanDao.updateById(processProcedureEmployeePlanPo);

            // 校验排产订单是否逾期
            LocalDateTime actBeginTime = processProcedureEmployeePlanPo.getActBeginTime();
            if (actBeginTime.isAfter(now)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedExpectedStartTime = actBeginTime.format(formatter);
                String formattedActualStartTime = now.format(formatter);

                log.info("确认接货完成！更新排产加工单是否延期：{}，加工单号：{}，预计开始时间：{}，实际开始时间：{}",
                        ProcessPlanDelay.TRUE, processOrderNo, formattedExpectedStartTime, formattedActualStartTime);
                processOrderPo.setProcessPlanDelay(ProcessPlanDelay.TRUE);
                processOrderDao.updateById(processOrderPo);
            }
        }

        ProcessOrderScanPo newProcessOrderScanPo = new ProcessOrderScanPo();
        newProcessOrderScanPo.setProcessOrderProcedureId(processOrderProcedureId);
        newProcessOrderScanPo.setProcessOrderNo(processOrderNo);
        newProcessOrderScanPo.setProcessFirst(processPo.getProcessFirst());
        newProcessOrderScanPo.setProcessCode(processPo.getProcessCode());
        newProcessOrderScanPo.setProcessName(processPo.getProcessName());
        newProcessOrderScanPo.setProcessCommission(processPo.getCommission());
        newProcessOrderScanPo.setExtraCommission(processPo.getExtraCommission());
        newProcessOrderScanPo.setReceiptNum(receiptNum);
        newProcessOrderScanPo.setPlatform(processOrderPo.getPlatform());
        newProcessOrderScanPo.setOrderTime(processOrderPo.getCreateTime());
        newProcessOrderScanPo.setOrderUser(processOrderPo.getCreateUser());
        newProcessOrderScanPo.setOrderUsername(processOrderPo.getCreateUsername());
        newProcessOrderScanPo.setReceiptTime(new DateTime().toLocalDateTime());
        newProcessOrderScanPo.setReceiptUser(GlobalContext.getUserKey());
        newProcessOrderScanPo.setReceiptUsername(GlobalContext.getUsername());
        processOrderScanDao.insert(newProcessOrderScanPo);

        // 保存组合工序关联扫码记录信息
        saveProcessOrderScanRelation(newProcessOrderScanPo.getProcessOrderScanId());

        // 更新当前工序
        processOrderPo.setCurrentProcessLabel(processPo.getProcessLabel());
        processOrderDao.updateByIdVersion(processOrderPo);

        return processOrderPo;
    }

    /**
     * @Description 工序操作环节校验
     * @author yanjiawei
     * @Date 2023/8/30 00:09
     */
    public void validateProcessStage(@NotNull Long processOrderProcedureId, ProcessStage desiredProcessStage) {
        final String currentUserKey = GlobalContext.getUserKey();
        if (StrUtil.isBlank(currentUserKey)) {
            throw new BizException("请重新登录！");
        }

        ProcessOrderProcedurePo processOrderProcedurePo = processOrderProcedureDao.getById(processOrderProcedureId);
        if (Objects.isNull(processOrderProcedurePo)) {
            throw new ParamIllegalException("加工工序不存在！");
        }
        final Long processId = processOrderProcedurePo.getProcessId();
        final String processName = processOrderProcedurePo.getProcessName();

        List<EmployeeProcessAbilityPo> employeeProcessAbilityPoList
                = processPlanBaseService.getEmployeeProcessAbilities(Collections.singleton(currentUserKey));
        if (CollectionUtils.isEmpty(employeeProcessAbilityPoList) || employeeProcessAbilityPoList.stream()
                .noneMatch(ability -> ability.getProcessId()
                        .equals(processId))) {
            throw new ParamIllegalException(
                    StrUtil.format("您不能操作工序：{}，请联系相关业务人员到{}进行工序职级配置或者到{}检查工序是否启用！",
                            processName, "SPM系统-职级管理/员工管理", "SCM系统-基础设置/工序列表"));
        }

        if (Objects.equals(desiredProcessStage, ProcessStage.RECEIVING)) {
            // 校验是否为排产订单
            ProcessProcedureEmployeePlanPo plan = processProcedureEmployeePlanDao.getByProcessOrderProcedureId(
                    processOrderProcedureId);
            if (Objects.nonNull(plan) && !Objects.equals(currentUserKey, plan.getEmployeeNo())) {
                throw new ParamIllegalException("您不允许接货，当前工序为排产订单，已有对应工序接货人。");
            }
        }
    }

    /**
     * 通过扫码记录ID列表查询提成信息的服务方法。
     *
     * @param processOrderScanIds 扫码记录ID列表
     * @return ScanCommissionSummaryBo列表，包含总提成和提成详情
     */
    public List<CommissionBo> getCommissions(Collection<Long> processOrderScanIds) {
        if (CollectionUtil.isEmpty(processOrderScanIds)) {
            return Collections.emptyList();
        }

        List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.getByIds(processOrderScanIds);
        if (CollectionUtil.isEmpty(processOrderScanPos)) {
            return Collections.emptyList();
        }
        List<ScanCommissionDetailPo> scanCommissionDetailPos = scanCommissionDetailDao.listByProcessOrderScanIds(
                processOrderScanIds);

        return processOrderScanPos.stream()
                .map(processOrderScanPo -> {
                    Long processOrderScanId = processOrderScanPo.getProcessOrderScanId();
                    BigDecimal baseCommission = processOrderScanPo.getProcessCommission();
                    Integer qualityGoodsCnt = processOrderScanPo.getQualityGoodsCnt();

                    CommissionBo resultBo = new CommissionBo();
                    resultBo.setProcessOrderScanId(processOrderScanId);

                    List<ScanCommissionDetailPo> matchCommissionDetail = scanCommissionDetailPos.stream()
                            .filter(scanCommissionDetailPo -> Objects.equals(processOrderScanId,
                                    scanCommissionDetailPo.getProcessOrderScanId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(matchCommissionDetail)) {
                        List<CommissionDetailBo> commissionDetails
                                = CommissionDetailConverter.convertToCommissionDetailBoList(matchCommissionDetail);
                        resultBo.setCommissionDetails(commissionDetails);
                    }

                    BigDecimal totalCommission = calculateTotalCommission(resultBo.getCommissionDetails(),
                            baseCommission, qualityGoodsCnt);
                    resultBo.setTotalCommission(totalCommission);
                    return resultBo;
                })
                .collect(Collectors.toList());
    }

    public BigDecimal calculateTotalCommission(List<CommissionDetailBo> commissionDetails,
                                               BigDecimal basePrice,
                                               Integer qualityGoodsCnt) {
        if (CollectionUtils.isNotEmpty(commissionDetails)) {
            // 如果存在提成明细，计算提成总额
            return commissionDetails.stream()
                    .map(CommissionDetailBo::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            // 如果不存在提成明细，使用基础单价和正品数量计算总提成
            return ScmAmountCalculateUtil.calculateAmountWithHalfUp(qualityGoodsCnt, basePrice, 2);
        }
    }

    /**
     * 此方法根据提供的参数和业务规则计算并插入提成详情。使用 @RedisLock 注解确保在分布式环境中对于给定的 processOrderScanId 的原子执行。
     *
     * @param processOrderScanId 要创建提成详情的工序扫码ID。
     */
    public void createCommissionDetails(Long processOrderScanId) {
        ProcessOrderScanPo processOrderScanPo = processOrderScanDao.getById(processOrderScanId);
        if (Objects.isNull(processOrderScanPo)) {
            throw new ParamIllegalException("工序扫码信息不存在，请刷新页面");
        }

        // 从 ProcessOrderScanPo 中提取相关信息
        String processCode = processOrderScanPo.getProcessCode();
        ProcessPo processPo = processDao.getByProcessCode(processCode);
        if (Objects.isNull(processPo)) {
            throw new ParamIllegalException("工序信息不存在，请刷新页面");
        }

        ProcessType processType = processPo.getProcessType();
        CommissionDetailsCalculationStrategy strategy = commDetCalcStrgyFactory.getStrategy(processType);
        strategy.createCommissionDetails(processOrderScanId);
    }


    /**
     * @Description 初始化X月份扫码记录提成（与createCommissionDetails代码存在大量重复，由于代码只执行一次，暂不抽取和优化）
     * @author yanjiawei
     * @Date 2023/12/20 16:35
     */
    public void createCommissionDetails(String processCode,
                                        List<ProcessOrderScanPo> processOrderScanList) {
        ProcessOrderScanPo oneOfScan = processOrderScanList.stream()
                .filter(scan -> StrUtil.isNotBlank(scan.getProcessName()))
                .findFirst()
                .orElse(null);
        String processName = Objects.nonNull(oneOfScan) ? oneOfScan.getProcessName() : "";

        List<ProcessWithCommissionRuleBo> processWithCommissionRules = processBaseService.getProcessWithCommissionRule(
                Collections.singletonList(processCode));
        ProcessWithCommissionRuleBo processWithCommissionRuleBo = processWithCommissionRules.stream()
                .filter(processWithCommissionRule -> Objects.equals(processCode,
                        processWithCommissionRule.getProcessCode()))
                .findFirst()
                .orElse(null);
        // 如果提成规则为 null 或为空，则记录警告
        if (Objects.isNull(processWithCommissionRuleBo) || CollectionUtils.isEmpty(
                processWithCommissionRuleBo.getRules())) {
            throw new BizException("初始化失败，无工序信息/规则信息，请配置后执行刷新！工序名称:{}", processName);
        }

        ProcessBo process = processWithCommissionRuleBo.getProcess();
        ProcessType processType = process.getProcessType();
        List<Long> processOrderScanIds = processOrderScanList.stream()
                .map(ProcessOrderScanPo::getProcessOrderScanId)
                .collect(Collectors.toList());

        List<ScanCommissionDetailPo> existCommissionDetails = scanCommissionDetailDao.listByProcessOrderScanIds(
                processOrderScanIds);
        if (CollectionUtils.isNotEmpty(existCommissionDetails)) {
            existCommissionDetails.forEach(
                    existCommissionDetail -> existCommissionDetail.setDelTimestamp(DateUtil.current()));
            scanCommissionDetailDao.updateBatchByIdVersion(existCommissionDetails);
        }

        for (ProcessOrderScanPo processOrderScanPo : processOrderScanList) {
            CommissionDetailsCalculationStrategy strategy = commDetCalcStrgyFactory.getStrategy(processType);
            Long processOrderScanId = processOrderScanPo.getProcessOrderScanId();
            strategy.createCommissionDetails(processOrderScanId);
        }
    }

    /**
     * @Description 根据起始值和结束值以及区间，求起始值到结束值在区间内落点个数
     * @author yanjiawei
     * @Date 2023/12/16 22:36
     */
    private int calculateSectionCount(int beforeTotal,
                                      int currentTotal,
                                      int sectionBegin,
                                      int sectionEnd) {
        int intersectionStart = Math.max(beforeTotal, sectionBegin);
        int intersectionEnd = Math.min(currentTotal, sectionEnd);

        // 如果交集的起始值小于等于结束值，说明存在交集
        if (intersectionStart <= intersectionEnd) {
            return intersectionEnd - intersectionStart + 1;
        } else {
            return 0;
        }
    }

    /**
     * 计算特定提成类别和属性的总提成金额和数量。
     *
     * @param commissionDetails   要计算的提成详情列表。
     * @param commissionCategory  要筛选的提成类别。
     * @param commissionAttribute 要筛选的提成属性。
     * @return 包含总提成金额和数量的 {@link CommissionAmountBo} 对象。
     */
    public CommissionAmountBo calculateCommissionAmount(List<CommissionDetailBo> commissionDetails,
                                                        CommissionCategory commissionCategory,
                                                        CommissionAttribute commissionAttribute) {
        CommissionAmountBo commissionAmount = new CommissionAmountBo();
        if (CollectionUtils.isEmpty(commissionDetails)) {
            return commissionAmount;
        }

        BigDecimal totalAmount = commissionDetails.stream()
                .filter(detail -> Objects.equals(commissionCategory, detail.getCommissionCategory()) && Objects.equals(
                        commissionAttribute, detail.getCommissionAttribute()))
                .map(CommissionDetailBo::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalQuantity = commissionDetails.stream()
                .filter(detail -> Objects.equals(commissionCategory, detail.getCommissionCategory()) && Objects.equals(
                        commissionAttribute, detail.getCommissionAttribute()))
                .mapToInt(CommissionDetailBo::getQuantity)
                .sum();

        commissionAmount.setTotalQuantity(totalQuantity);
        commissionAmount.setTotalAmount(totalAmount);
        return commissionAmount;
    }

    /**
     * 重新计算提成明细，删除指定的扫码记录，并重新计算剩余扫码记录的提成明细。
     *
     * @param delScanId 待删除的扫码记录ID
     */
    public void reCreateCommissionDetails(Long delScanId) {
        // 获取待删除的扫码记录
        ProcessOrderScanPo delProcessOrderScanPo = processOrderScanDao.getById(delScanId);
        if (Objects.isNull(delProcessOrderScanPo)) {
            // 若待删除的扫码记录不存在，则直接返回
            log.info("重新计算提成失败，工序扫码记录不存在,delScanId:{}", delScanId);
            return;
        }

        String processCode = delProcessOrderScanPo.getProcessCode();
        String completeUser = delProcessOrderScanPo.getCompleteUser();

        // 计算当前月的开始和结束时间
        LocalDateTime completeBeginTime = TimeUtil.convertZone(DateUtil.beginOfMonth(new Date())
                        .toLocalDateTime(), TimeZoneId.CN,
                TimeZoneId.UTC);
        LocalDateTime completeEndTime = TimeUtil.convertZone(DateUtil.endOfMonth(new Date())
                .toLocalDateTime(), TimeZoneId.CN, TimeZoneId.UTC);

        // 获取当前工序和员工在当前月的所有扫码记录
        Set<String> relProcCodes = processBaseService.getRelProcCodes(processCode);
        List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.getProcessOrderScanList(relProcCodes,
                completeUser,
                completeBeginTime,
                completeEndTime);
        if (CollectionUtils.isEmpty(processOrderScanPos)) {
            // 若当前月没有其他扫码记录，则直接返回
            log.info("重新计算提成失败，当前月无扫码记录,completeUser:{},completeBeginTime:{},completeEndTime:{}",
                    completeUser, completeBeginTime, completeEndTime);
            return;
        }

        // 获取需要重新计算的提成明细并删除
        List<Long> delCommissionScanIds = processOrderScanPos.stream()
                .map(ProcessOrderScanPo::getProcessOrderScanId)
                .collect(Collectors.toList());
        List<ScanCommissionDetailPo> scanCommissionDetailPos = scanCommissionDetailDao.listByProcessOrderScanIds(
                delCommissionScanIds);

        if (CollectionUtils.isNotEmpty(scanCommissionDetailPos)) {
            // 删除需要重新计算的提成明细
            List<Long> delScanCommissionDetailId = scanCommissionDetailPos.stream()
                    .map(ScanCommissionDetailPo::getScanCommissionDetailId)
                    .collect(Collectors.toList());
            scanCommissionDetailDao.removeBatchByIds(delScanCommissionDetailId);
        }

        // 有序计算剩余扫码记录的提成明细
        processOrderScanPos.removeIf(
                processOrderScanPo -> Objects.equals(delScanId, processOrderScanPo.getProcessOrderScanId()));
        for (ProcessOrderScanPo processOrderScanPo : processOrderScanPos) {
            Long processOrderScanId = processOrderScanPo.getProcessOrderScanId();
            createCommissionDetails(processOrderScanId);
        }
    }


    /**
     * 创建扫码提成详情列表
     *
     * @param processOrderScanId          扫码工序ID
     * @param processCode                 工序编码
     * @param baseCommission              基础提成
     * @param beforeTotalQualityGoodsCnt  过去累计完成正品数
     * @param currentTotalQualityGoodsCnt 当前累计完成正品数
     * @param completeUsername            完成人名称
     * @param rules                       阶梯提成规则集合
     * @return 扫码提成详情列表
     */
    public List<ScanCommissionDetailPo> createScanCommissionDetails(Long processOrderScanId,
                                                                    String processCode,
                                                                    BigDecimal baseCommission,
                                                                    BigDecimal extraCommission,
                                                                    int beforeTotalQualityGoodsCnt,
                                                                    int currentTotalQualityGoodsCnt,
                                                                    String completeUsername,
                                                                    TreeSet<ProcessCommissionRuleBo> rules) {
        processBaseService.validateRules(ProcessCommissionRuleConverter.toPos(rules), errorMessage -> {
            throw new BizException(errorMessage);
        });

        // 获取区间规则最大值&最小值
        Optional<ProcessCommissionRuleBo> maxCommissionLevelObject = rules.stream()
                .max(Comparator.comparingInt(ProcessCommissionRuleBo::getCommissionLevel));
        int maxSectionEnd = maxCommissionLevelObject.map(ProcessCommissionRuleBo::getEndQuantity)
                .orElse(0);
        if (currentTotalQualityGoodsCnt > maxSectionEnd) {
            throw new BizException("计算工序提成异常！原因：{}",
                    "当前完成工序数已超过提成规则区间最大值，请联系业务人员进行合理配置！");
        }

        Optional<ProcessCommissionRuleBo> minCommissionLevelObject = rules.stream()
                .min(Comparator.comparingInt(ProcessCommissionRuleBo::getCommissionLevel));
        int minSectionEnd = minCommissionLevelObject.map(ProcessCommissionRuleBo::getStartQuantity)
                .orElse(0);
        if (beforeTotalQualityGoodsCnt + 1 < minSectionEnd) {
            throw new BizException("计算工序提成异常！原因：{}",
                    "累计完成工序数未超过最低提成规则，请联系业务人员进行合理配置！");
        }

        return rules.stream()
                .map(rule -> {
                    Integer commissionLevel = rule.getCommissionLevel();
                    BigDecimal commissionCoefficient = rule.getCommissionCoefficient();

                    // 计算并设置提成单价，保留两位小数，使用四舍五入
                    BigDecimal unitCommission = baseCommission.multiply(commissionCoefficient)
                            .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);

                    int sectionBegin = rule.getStartQuantity();
                    int sectionEnd = rule.getEndQuantity();
                    int sectionCount = calculateSectionCount(beforeTotalQualityGoodsCnt + 1,
                            currentTotalQualityGoodsCnt, sectionBegin, sectionEnd);
                    log.info(
                            "计算扫码阶梯提成：扫码工序id：{} 工序编码:{} 完成人名称：{} 规则区间：[{},{}] 系数:{}% 区间内正品数:{} 基础提成:{} 额外提成:{}",
                            processOrderScanId, processCode, completeUsername, sectionBegin, sectionEnd,
                            commissionCoefficient, sectionCount, baseCommission, extraCommission);

                    ScanCommissionDetailPo scanCommissionDetailPo = new ScanCommissionDetailPo();
                    scanCommissionDetailPo.setProcessOrderScanId(processOrderScanId);
                    scanCommissionDetailPo.setCommissionCategory(CommissionCategory.STAIR);
                    scanCommissionDetailPo.setCommissionAttribute(
                            CommissionAttribute.findByCommissionLevel(commissionLevel));

                    String commissionRule = StrUtil.format(
                            "[{},{}]【规则区间】 ({}【基础金额】 * {}%【规则系数】) + {}【额外金额】) * {}【区间数量】)",
                            sectionBegin, sectionEnd, baseCommission, commissionCoefficient, extraCommission,
                            sectionCount);
                    scanCommissionDetailPo.setCommissionRule(commissionRule);

                    scanCommissionDetailPo.setQuantity(sectionCount);
                    scanCommissionDetailPo.setUnitCommission(unitCommission);

                    BigDecimal totalAmount = baseCommission.multiply(
                                    commissionCoefficient.multiply(BigDecimal.valueOf(sectionCount)))
                            .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
                    scanCommissionDetailPo.setTotalAmount(
                            totalAmount.add(extraCommission.multiply(BigDecimal.valueOf(sectionCount))));
                    return scanCommissionDetailPo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 计算加工人力总成本的方法。
     *
     * @param processingOrderNo 加工单号
     * @return 加工人力总成本
     */
    public BigDecimal calculateLaborCost(String processingOrderNo) {
        // 初始化人力总成本为零
        BigDecimal laborCost = BigDecimal.ZERO;

        // 查询加工单信息
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processingOrderNo);
        if (Objects.isNull(processOrderPo)) {
            // 如果加工单信息为空，则返回人力总成本为零
            return laborCost;
        }
        String processOrderNo = processOrderPo.getProcessOrderNo();

        // 查询加工单关联的扫码记录
        List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.getByProcessOrderNos(
                Collections.singletonList(processOrderNo));
        if (CollectionUtils.isEmpty(processOrderScanPos)) {
            return laborCost;
        }

        List<Long> processOrderScanIds = processOrderScanPos.stream()
                .map(ProcessOrderScanPo::getProcessOrderScanId)
                .collect(Collectors.toList());
        List<ScanCommissionDetailPo> scanCommissionDetailPos = scanCommissionDetailDao.listByProcessOrderScanIds(
                processOrderScanIds);

        for (ProcessOrderScanPo processOrderScanPo : processOrderScanPos) {
            Long processOrderScanId = processOrderScanPo.getProcessOrderScanId();
            BigDecimal qualityGoodsCnt = BigDecimal.valueOf(processOrderScanPo.getQualityGoodsCnt());
            BigDecimal processCommission = processOrderScanPo.getProcessCommission();
            List<ScanCommissionDetailPo> matchScanCommission = scanCommissionDetailPos.stream()
                    .filter(scanCommissionDetail -> Objects.equals(processOrderScanId,
                            scanCommissionDetail.getProcessOrderScanId()))
                    .collect(Collectors.toList());

            // 计算阶梯总金额，如果列表为空则使用默认值
            BigDecimal totalCommissionAmount = matchScanCommission.stream()
                    .map(ScanCommissionDetailPo::getTotalAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            // 成本价 = 优先级（提成总金额>基础提成总金额）
            laborCost = laborCost.add(totalCommissionAmount.compareTo(
                    BigDecimal.ZERO) > 0 ? totalCommissionAmount : qualityGoodsCnt.multiply(processCommission)
                    .setScale(2, RoundingMode.HALF_UP));
        }

        // 返回计算得到的人力总成本
        return laborCost;
    }

    /**
     * 保存工序扫描订单关联关系。
     * 如果工序扫描订单信息不存在或相关组合工序信息不存在，则不执行保存操作。
     * 如果已存在相关关联关系，则先将其标记为已删除，然后再保存新的关联关系。
     *
     * @param processOrderScanId 工序扫描订单ID
     */
    @RedisLock(key = "#processOrderScanId", prefix = ScmRedisConstant.OPERATE_PROCESS_ORDER_SCAN_RELATE, waitTime = 1
            , leaseTime = -1)
    public void saveProcessOrderScanRelation(Long processOrderScanId) {
        // 获取工序扫码记录
        ProcessOrderScanPo processOrderScanPo = processOrderScanDao.getById(processOrderScanId);
        if (Objects.isNull(processOrderScanPo)) {
            return;
        }

        // 查询非组合工序信息
        String processCode = processOrderScanPo.getProcessCode();
        List<ProcessCompositePo> processCompositePos = processCompositeDao.listByParentProcessCodes(
                Collections.singleton(processCode));
        if (CollectionUtil.isEmpty(processCompositePos)) {
            return;
        }

        List<ProcessOrderScanRelatePo> existProcessRelates = processOrderScanRelateDao.listByProcessOrderScanId(
                processOrderScanId);
        if (CollectionUtil.isNotEmpty(existProcessRelates)) {
            existProcessRelates.forEach(existProcessRelate -> existProcessRelate.setDelTimestamp(DateUtil.current()));
            processOrderScanRelateDao.updateBatchByIdVersion(existProcessRelates);
        }

        List<String> indProcCodes = processCompositePos.stream()
                .map(ProcessCompositePo::getSubProcessCode)
                .collect(Collectors.toList());
        List<ProcessPo> indProcessPos = processDao.getByProcessCodes(indProcCodes);

        String completeUser = processOrderScanPo.getCompleteUser();
        String completeUsername = processOrderScanPo.getCompleteUsername();
        LocalDateTime completeTime = processOrderScanPo.getCompleteTime();
        Integer qualityGoodsCnt = processOrderScanPo.getQualityGoodsCnt();
        List<ProcessOrderScanRelatePo> processOrderScanRelatePos = indProcCodes.stream()
                .map(indProcCode -> {
                    ProcessOrderScanRelatePo processOrderScanRelatePo = new ProcessOrderScanRelatePo();
                    processOrderScanRelatePo.setProcessOrderScanId(processOrderScanId);
                    processOrderScanRelatePo.setProcessCode(indProcCode);

                    ProcessPo matchIndProcess = ParamValidUtils.requireNotNull(indProcessPos.stream()
                                    .filter(processPo -> Objects.equals(
                                            indProcCode,
                                            processPo.getProcessCode()))
                                    .findFirst()
                                    .orElse(null),
                            "接货失败，请确认工序关联非组合工序信息是否已正确配置！");
                    processOrderScanRelatePo.setProcessCommission(matchIndProcess.getCommission());
                    processOrderScanRelatePo.setExtraCommission(matchIndProcess.getExtraCommission());
                    processOrderScanRelatePo.setCompleteUser(completeUser);
                    processOrderScanRelatePo.setCompleteUsername(completeUsername);
                    processOrderScanRelatePo.setCompleteTime(completeTime);
                    processOrderScanRelatePo.setQualityGoodsCnt(qualityGoodsCnt);
                    return processOrderScanRelatePo;
                })
                .collect(Collectors.toList());
        processOrderScanRelateDao.insertBatch(processOrderScanRelatePos);
    }

    @RedisLock(key = "#processOrderScanId", prefix = ScmRedisConstant.OPERATE_PROCESS_ORDER_SCAN_RELATE, waitTime = 1
            , leaseTime = -1)
    public void removeProcessOrderScanRelation(Long processOrderScanId) {
        // 通过扫码记录ID找到关联扫码记录
        List<ProcessOrderScanRelatePo> processOrderScanRelatePos = processOrderScanRelateDao.listByProcessOrderScanId(
                processOrderScanId);

        processOrderScanRelatePos.forEach(
                processOrderScanRelatePo -> processOrderScanRelatePo.setDelTimestamp(DateUtil.current()));
        processOrderScanRelateDao.updateBatchByIdVersion(processOrderScanRelatePos);
    }


    @Transactional(rollbackFor = Exception.class)
    public void processOrderScanRelateInitJob() {
        List<ProcessCompositePo> processCompositePos = processCompositeDao.list();
        if (CollectionUtils.isEmpty(processCompositePos)) {
            log.info("初始化结束，不存在组合工序关联关系，请维护后执行初始化。");
            return;
        }
        Set<String> parentProcessCodes = processCompositePos.stream()
                .map(ProcessCompositePo::getParentProcessCode)
                .collect(Collectors.toSet());
        for (String parentProcessCode : parentProcessCodes) {
            List<ProcessOrderScanPo> processOrderScanPos = processOrderScanDao.listByProcessCode(parentProcessCode);
            if (CollectionUtils.isNotEmpty(processOrderScanPos)) {
                for (ProcessOrderScanPo processOrderScanPo : processOrderScanPos) {
                    Long processOrderScanId = processOrderScanPo.getProcessOrderScanId();
                    saveProcessOrderScanRelation(processOrderScanId);
                }
            }
        }
    }

    /**
     * 更新工序扫描订单关联关系的完成时间、完成人员和质量货物数量。
     * 如果指定的工序扫描订单关联关系不存在，则不执行更新操作。
     *
     * @param processOrderScanId 工序扫描订单ID
     */
    @RedisLock(key = "#processOrderScanId", prefix = ScmRedisConstant.OPERATE_PROCESS_ORDER_SCAN_RELATE, waitTime = 1
            , leaseTime = -1)
    public void updateProcessOrderScanRelation(Long processOrderScanId) {
        ProcessOrderScanPo processOrderScanPo = processOrderScanDao.getById(processOrderScanId);
        if (Objects.isNull(processOrderScanPo)) {
            return;
        }
        LocalDateTime completeTime = processOrderScanPo.getCompleteTime();
        String completeUser = processOrderScanPo.getCompleteUser();
        String completeUsername = processOrderScanPo.getCompleteUsername();
        Integer qualityGoodsCnt = processOrderScanPo.getQualityGoodsCnt();

        List<ProcessOrderScanRelatePo> existProcessOrderScanRelatePos
                = processOrderScanRelateDao.listByProcessOrderScanId(processOrderScanId);
        if (CollectionUtils.isEmpty(existProcessOrderScanRelatePos)) {
            saveProcessOrderScanRelation(processOrderScanId);
        }

        List<ProcessOrderScanRelatePo> processOrderScanRelatePos
                = processOrderScanRelateDao.listByProcessOrderScanId(processOrderScanId);
        if (CollectionUtils.isNotEmpty(processOrderScanRelatePos)) {
            processOrderScanRelatePos.forEach(processOrderScanRelatePo -> {
                processOrderScanRelatePo.setCompleteTime(completeTime);
                processOrderScanRelatePo.setCompleteUser(completeUser);
                processOrderScanRelatePo.setCompleteUsername(completeUsername);
                processOrderScanRelatePo.setQualityGoodsCnt(qualityGoodsCnt);
            });
            processOrderScanRelateDao.updateBatchByIdVersion(processOrderScanRelatePos);
        }
    }

    public void updateCommissionDetails(Long processOrderScanId) {
        ProcessOrderScanPo processOrderScanPo = processOrderScanDao.getById(processOrderScanId);
        if (Objects.isNull(processOrderScanPo)) {
            throw new ParamIllegalException("工序扫码信息不存在，请刷新页面");
        }

        // 从 ProcessOrderScanPo 中提取相关信息
        String processCode = processOrderScanPo.getProcessCode();
        ProcessPo processPo = processDao.getByProcessCode(processCode);
        if (Objects.isNull(processPo)) {
            throw new ParamIllegalException("工序信息不存在，请刷新页面");
        }

        List<ScanCommissionDetailPo> existCommissionDetails = scanCommissionDetailDao.listByProcessOrderScanId(
                processOrderScanId);
        if (CollectionUtils.isNotEmpty(existCommissionDetails)) {
            existCommissionDetails.forEach(
                    existCommissionDetail -> existCommissionDetail.setDelTimestamp(DateUtil.current()));
            scanCommissionDetailDao.updateBatchByIdVersion(existCommissionDetails);
        }

        ProcessType processType = processPo.getProcessType();
        CommissionDetailsCalculationStrategy strategy = commDetCalcStrgyFactory.getStrategy(processType);
        strategy.createCommissionDetails(processOrderScanId);
    }
}
