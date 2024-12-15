package com.hete.supply.scm.server.scm.process.converter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.NeedProcessPlan;
import com.hete.supply.scm.api.scm.entity.enums.ProcessPlanDelay;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessProcedureEmployeePlanPo;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.process.enums.ProcessingStatus;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年08月09日 06:37
 */
public class H5ProcessOrderOperationScanConverter {

    public static List<H5ProcessOrderOperationScanVo> toVo(ProcessOrderScanByH5Vo processOrderScanByH5Vo,
                                                           ProcessOrderPo processOrderPo,
                                                           List<ProcessProcedureEmployeePlanPo> employeePlanPos) {
        if (Objects.isNull(processOrderScanByH5Vo)) {
            return null;
        }

        final List<ProcessOrderProcedureByH5Vo> processOrderProcedures = processOrderScanByH5Vo.getProcessOrderProcedures();
        if (CollectionUtils.isEmpty(processOrderProcedures)) {
            return null;
        }

        final List<ProcessOrderItemVo> processOrderItems = processOrderScanByH5Vo.getProcessOrderItems();
        final ProcessOrderItemVo pItem = processOrderItems.stream().findFirst().orElse(null);
        final String itemSku = Objects.nonNull(pItem) ? pItem.getSku() : "";
        final int processNum = Objects.nonNull(pItem) ? pItem.getProcessNum() : 0;
        final List<Long> processOrderProcedureIds = employeePlanPos.stream()
                .map(ProcessProcedureEmployeePlanPo::getProcessOrderProcedureId).collect(Collectors.toList());
        final ProcessPlanDelay processPlanDelay = processOrderPo.getProcessPlanDelay();
        final String containerCode = processOrderPo.getContainerCode();

        // 当前正在/正要加工的工序
        final Long currentProcessOrderProcedureId = processOrderProcedures.stream()
                .filter(procedure -> StrUtil.isBlank(procedure.getCompleteUsername()))
                .min(Comparator.comparingInt(ProcessOrderProcedureByH5Vo::getSort))
                .map(ProcessOrderProcedureByH5Vo::getProcessOrderProcedureId).orElse(null);

        List<H5ProcessOrderOperationScanVo> h5ProcessOrderOperationScanVos = processOrderProcedures.stream().map(processOrderProcedure -> {
            H5ProcessOrderOperationScanVo newVo = new H5ProcessOrderOperationScanVo();
            final Long processOrderProcedureId = processOrderProcedure.getProcessOrderProcedureId();
            // 设置加工单ID
            newVo.setProcessOrderId(processOrderScanByH5Vo.getProcessOrderId());
            // 设置加工单号
            newVo.setProcessOrderNo(processOrderScanByH5Vo.getProcessOrderNo());
            // 设置加工单状态
            newVo.setProcessOrderStatus(processOrderScanByH5Vo.getProcessOrderStatus());
            // 设置SKU
            newVo.setSku(itemSku);
            // 设置SPU
            newVo.setSpu(processOrderScanByH5Vo.getSpu());
            // 设置加工数量
            newVo.setProcessNum(processNum);
            // 设置文件编码列表
            newVo.setFileCodeList(processOrderScanByH5Vo.getFileCodeList());
            // 设置最大可用收货数量
            newVo.setMaxAvailableReceiptNum(processOrderScanByH5Vo.getMaxAvailableReceiptNum());
            // 设置容器编码
            newVo.setContainerCode(containerCode);
            // 设置加工单项列表
            newVo.setProcessOrderItems(processOrderScanByH5Vo.getProcessOrderItems());
            // 设置版本号
            newVo.setVersion(processOrderScanByH5Vo.getVersion());
            // 设置加工单工序ID
            newVo.setProcessOrderProcedureId(processOrderProcedure.getProcessOrderProcedureId());
            // 设置是否延误
            newVo.setProcessPlanDelay(processPlanDelay);
            // 提成
            newVo.setCommission(new BigDecimal(processNum).multiply(processOrderProcedure.getCommission()));
            newVo.setSort(processOrderProcedure.getSort());

            // 工序扫码信息
            H5ProcessOperationScanVo newProcedureVo = new H5ProcessOperationScanVo();
            // 设置加工单工序ID
            newProcedureVo.setProcessOrderProcedureId(processOrderProcedure.getProcessOrderProcedureId());
            // 设置容器编码
            newProcedureVo.setContainerCode(containerCode);
            // 设置加工ID
            newProcedureVo.setProcessId(processOrderProcedure.getProcessId());
            // 设置加工编码
            newProcedureVo.setProcessCode(processOrderProcedure.getProcessCode());
            // 设置加工标签
            newProcedureVo.setProcessLabel(processOrderProcedure.getProcessLabel());
            // 设置加工第二名称
            newProcedureVo.setProcessSecondName(processOrderProcedure.getProcessSecondName());
            // 设置加工名称
            newProcedureVo.setProcessName(processOrderProcedure.getProcessName());
            // 设置收货数量
            newProcedureVo.setReceiptNum(processOrderProcedure.getReceiptNum());
            // 设置良品数量
            newProcedureVo.setQualityGoodsCnt(processOrderProcedure.getQualityGoodsCnt());
            // 设置次品数量
            newProcedureVo.setDefectiveGoodsCnt(processOrderProcedure.getDefectiveGoodsCnt());
            // 设置收货时间
            newProcedureVo.setReceiptTime(processOrderProcedure.getReceiptTime());
            // 设置收货操作人员
            newProcedureVo.setReceiptUsername(processOrderProcedure.getReceiptUsername());
            newProcedureVo.setReceiptUser(processOrderProcedure.getReceiptUser());
            newProcedureVo.setProcessingTime(processOrderProcedure.getProcessingTime());
            newProcedureVo.setProcessingUsername(processOrderProcedure.getProcessingUsername());
            // 设置完成时间
            newProcedureVo.setCompleteTime(processOrderProcedure.getCompleteTime());
            // 设置完成操作人员
            newProcedureVo.setCompleteUsername(processOrderProcedure.getCompleteUsername());
            newProcedureVo.setActualStartTime(processOrderProcedure.getReceiptTime());
            newProcedureVo.setActualEndTime(processOrderProcedure.getCompleteTime());
            // 设置排序
            newProcedureVo.setSort(processOrderProcedure.getSort());
            // 设置版本号
            newProcedureVo.setVersion(processOrderProcedure.getVersion());
            // 设置提成
            newProcedureVo.setCommission(new BigDecimal(processNum).multiply(processOrderProcedure.getCommission()));


            // 排产信息
            ProcessProcedureEmployeePlanPo matchEmployeePlan = employeePlanPos.stream()
                    .filter(employeePlanPo -> Objects.equals(processOrderProcedureId, employeePlanPo.getProcessOrderProcedureId()))
                    .findFirst().orElse(null);
            if (Objects.nonNull(matchEmployeePlan)) {
                newProcedureVo.setEmployeeNo(matchEmployeePlan.getEmployeeNo());
                newProcedureVo.setEmployeeName(matchEmployeePlan.getEmployeeName());
                newProcedureVo.setEstimatedStartTime(matchEmployeePlan.getExpectBeginTime());
                newProcedureVo.setEstimatedEndTime(matchEmployeePlan.getExpectEndTime());
                newProcedureVo.setStartDelayStatus(ScmTimeUtil.checkStartDelay(newProcedureVo.getActualStartTime(), newProcedureVo.getEstimatedStartTime()));
                newProcedureVo.setEndDelayStatus(ScmTimeUtil.checkEndDelay(newProcedureVo.getActualEndTime(), newProcedureVo.getEstimatedEndTime()));
            }

            if (StrUtil.isBlank(processOrderProcedure.getReceiptUsername())) {
                if (Objects.equals(processOrderProcedure.getProcessOrderProcedureId(), currentProcessOrderProcedureId)) {
                    newVo.setProcessStatus(ProcessingStatus.AWAITING_RECEIPT);
                    newProcedureVo.setProcessStatus(ProcessingStatus.AWAITING_RECEIPT);
                } else {
                    newVo.setProcessStatus(ProcessingStatus.WAITING);
                    newProcedureVo.setProcessStatus(ProcessingStatus.WAITING);
                }
            } else {
                newVo.setProcessStatus(ProcessingStatus.AWAITING_START);
                newProcedureVo.setProcessStatus(ProcessingStatus.AWAITING_START);
            }
            if (StrUtil.isNotBlank(processOrderProcedure.getProcessingUser())) {
                newVo.setProcessStatus(ProcessingStatus.PROCESSING);
                newProcedureVo.setProcessStatus(ProcessingStatus.PROCESSING);
            }
            if (StrUtil.isNotBlank(processOrderProcedure.getCompleteUsername())) {
                newVo.setProcessStatus(ProcessingStatus.COMPLETED);
                newProcedureVo.setProcessStatus(ProcessingStatus.COMPLETED);
            }

            newVo.setProcessOrderProcedure(newProcedureVo);
            return newVo;
        }).collect(Collectors.toList());

        // 设置上一道工序的排产人
        AtomicReference<String> previousEmployeeName = new AtomicReference<>("");
        h5ProcessOrderOperationScanVos.stream().sorted(Comparator.comparing(H5ProcessOrderOperationScanVo::getSort)).forEach(
                vo -> {
                    vo.setPreviousEmployeeName(previousEmployeeName.get());
                    H5ProcessOperationScanVo processOrderProcedure = vo.getProcessOrderProcedure();
                    if (Objects.nonNull(processOrderProcedure)) {
                        previousEmployeeName.set(processOrderProcedure.getEmployeeName());
                    }
                });
        return h5ProcessOrderOperationScanVos.stream().filter(h5ProcessOrderOperationScanVo ->
                processOrderProcedureIds.contains(h5ProcessOrderOperationScanVo.getProcessOrderProcedureId())).collect(Collectors.toList());
    }

    public static ProcessOrderScanDetailVo toDetailVo(ProcessOrderScanByH5Vo processOrderScanByH5Vo,
                                                      ProcessOrderPo processOrderPo,
                                                      List<ProcessProcedureEmployeePlanPo> employeePlanPos) {
        if (Objects.isNull(processOrderScanByH5Vo)) {
            return null;
        }
        final List<ProcessOrderProcedureByH5Vo> processOrderProcedures = processOrderScanByH5Vo.getProcessOrderProcedures();
        final List<ProcessOrderItemVo> processOrderItems = processOrderScanByH5Vo.getProcessOrderItems();
        final ProcessOrderItemVo pItem = processOrderItems.stream().findFirst().orElse(null);
        final String itemSku = Objects.nonNull(pItem) ? pItem.getSku() : "";
        final int processNum = Objects.nonNull(pItem) ? pItem.getProcessNum() : 0;
        final ProcessPlanDelay processPlanDelay = processOrderPo.getProcessPlanDelay();
        final String containerCode = processOrderPo.getContainerCode();
        final NeedProcessPlan needProcessPlan = processOrderPo.getNeedProcessPlan();
        final IsReceiveMaterial isReceiveMaterial = processOrderPo.getIsReceiveMaterial();

        // 当前正在/正要加工的工序
        final Long currentProcessOrderProcedureId = processOrderProcedures.stream()
                .filter(procedure -> StrUtil.isBlank(procedure.getCompleteUsername()))
                .min(Comparator.comparingInt(ProcessOrderProcedureByH5Vo::getSort))
                .map(ProcessOrderProcedureByH5Vo::getProcessOrderProcedureId).orElse(null);
        final BigDecimal totalCommission = processOrderProcedures.stream()
                .map(procedure -> BigDecimal.valueOf(processNum).multiply(procedure.getCommission()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        ProcessOrderScanDetailVo newVo = new ProcessOrderScanDetailVo();
        // 加工单信息
        newVo.setProcessOrderId(processOrderScanByH5Vo.getProcessOrderId());
        newVo.setProcessOrderNo(processOrderScanByH5Vo.getProcessOrderNo());
        newVo.setIsReceiveMaterial(isReceiveMaterial);
        newVo.setProcessOrderStatus(processOrderScanByH5Vo.getProcessOrderStatus());
        newVo.setSku(itemSku);
        newVo.setProcessNum(processNum);
        newVo.setCommission(totalCommission);
        newVo.setFileCodeList(processOrderScanByH5Vo.getFileCodeList());
        newVo.setLastProcessOrderScanPo(processOrderScanByH5Vo.getLastProcessOrderScanPo());
        newVo.setMaxAvailableReceiptNum(processOrderScanByH5Vo.getMaxAvailableReceiptNum());
        newVo.setContainerCode(containerCode);
        newVo.setProcessOrderItems(processOrderScanByH5Vo.getProcessOrderItems());
        newVo.setVersion(processOrderScanByH5Vo.getVersion());
        newVo.setProcessPlanDelay(processPlanDelay);
        AtomicInteger currentSort = new AtomicInteger(1);

        List<H5ProcessOperationScanVo> processOrderProceduresVos = processOrderProcedures.stream().map(processOrderProcedure -> {
            final Long processOrderProcedureId = processOrderProcedure.getProcessOrderProcedureId();
            H5ProcessOperationScanVo newProcedureVo = new H5ProcessOperationScanVo();
            newProcedureVo.setProcessOrderProcedureId(processOrderProcedureId);
            newProcedureVo.setContainerCode(containerCode);
            newProcedureVo.setProcessId(processOrderProcedure.getProcessId());
            newProcedureVo.setProcessCode(processOrderProcedure.getProcessCode());
            newProcedureVo.setProcessLabel(processOrderProcedure.getProcessLabel());
            newProcedureVo.setProcessSecondName(processOrderProcedure.getProcessSecondName());
            newProcedureVo.setProcessName(processOrderProcedure.getProcessName());
            newProcedureVo.setReceiptNum(processOrderProcedure.getReceiptNum());
            newProcedureVo.setQualityGoodsCnt(processOrderProcedure.getQualityGoodsCnt());
            newProcedureVo.setDefectiveGoodsCnt(processOrderProcedure.getDefectiveGoodsCnt());
            newProcedureVo.setReceiptTime(processOrderProcedure.getReceiptTime());
            newProcedureVo.setReceiptUser(processOrderProcedure.getReceiptUser());
            newProcedureVo.setReceiptUsername(processOrderProcedure.getReceiptUsername());
            newProcedureVo.setProcessingTime(processOrderProcedure.getProcessingTime());
            newProcedureVo.setProcessingUsername(processOrderProcedure.getProcessingUsername());
            newProcedureVo.setCompleteTime(processOrderProcedure.getCompleteTime());
            newProcedureVo.setCompleteUsername(processOrderProcedure.getCompleteUsername());
            newProcedureVo.setActualStartTime(processOrderProcedure.getReceiptTime());
            newProcedureVo.setActualEndTime(processOrderProcedure.getCompleteTime());
            newProcedureVo.setCommission(processOrderProcedure.getCommission());
            newProcedureVo.setSort(processOrderProcedure.getSort());
            newProcedureVo.setVersion(processOrderProcedure.getVersion());


            // 匹配排产计划人员
            ProcessProcedureEmployeePlanPo matchEmployeePlan = employeePlanPos.stream()
                    .filter(employeePlanPo -> Objects.equals(processOrderProcedureId, employeePlanPo.getProcessOrderProcedureId()))
                    .findFirst().orElse(null);
            if (Objects.nonNull(matchEmployeePlan)) {
                newProcedureVo.setEmployeeNo(matchEmployeePlan.getEmployeeNo());
                newProcedureVo.setEmployeeName(matchEmployeePlan.getEmployeeName());
                newProcedureVo.setEstimatedStartTime(matchEmployeePlan.getExpectBeginTime());
                newProcedureVo.setEstimatedEndTime(matchEmployeePlan.getExpectEndTime());
                newProcedureVo.setStartDelayStatus(ScmTimeUtil.checkStartDelay(newProcedureVo.getActualStartTime(), newProcedureVo.getEstimatedStartTime()));
                newProcedureVo.setEndDelayStatus(ScmTimeUtil.checkEndDelay(newProcedureVo.getActualEndTime(), newProcedureVo.getEstimatedEndTime()));
            }
            if (Objects.equals(NeedProcessPlan.FALSE, needProcessPlan)) {
                newProcedureVo.setEmployeeNo("*");
                newProcedureVo.setEmployeeName("*");
            }


            if (StrUtil.isBlank(processOrderProcedure.getReceiptUsername())) {
                if (Objects.equals(processOrderProcedure.getProcessOrderProcedureId(), currentProcessOrderProcedureId)) {
                    newProcedureVo.setProcessStatus(ProcessingStatus.AWAITING_RECEIPT);
                } else {
                    newProcedureVo.setProcessStatus(ProcessingStatus.WAITING);
                }
            } else {
                newProcedureVo.setProcessStatus(ProcessingStatus.AWAITING_START);
            }
            if (StrUtil.isNotBlank(processOrderProcedure.getProcessingUser())) {
                newProcedureVo.setProcessStatus(ProcessingStatus.PROCESSING);
            }
            if (StrUtil.isNotBlank(processOrderProcedure.getCompleteUsername())) {
                newProcedureVo.setProcessStatus(ProcessingStatus.COMPLETED);
            }
            return newProcedureVo;
        }).collect(Collectors.toList());

        processOrderProceduresVos.stream().sorted(Comparator.comparingInt(H5ProcessOperationScanVo::getSort)).forEach(processOrderProcedure -> {
            processOrderProcedure.setCurrentSort(currentSort.getAndIncrement());
        });

        newVo.setProcessOrderProcedures(processOrderProceduresVos);
        return newVo;
    }
}
