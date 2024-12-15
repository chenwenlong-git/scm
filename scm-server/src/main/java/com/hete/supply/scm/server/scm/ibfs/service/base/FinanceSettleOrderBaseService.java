package com.hete.supply.scm.server.scm.ibfs.service.base;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.mc.api.workflow.entity.dto.WorkflowTransferDto;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.ibfs.builder.FinanceSettleOrderBuilder;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceRecoOrderDao;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceSettleCarryoverOrderDao;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceSettleOrderDao;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceSettleOrderItemDao;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.CarryoverCalculationBo;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.FinanceSettleOrderTransferDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleCarryoverOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderItemPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderPo;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleOrderItemType;
import com.hete.supply.scm.server.scm.ibfs.handler.CreateSettleOrderHandler;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
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
 * Created on 2024/5/23.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinanceSettleOrderBaseService {
    private final FinanceRecoOrderDao recoOrderDao;
    private final FinanceSettleOrderDao settleOrderDao;
    private final FinanceSettleOrderItemDao settleOrderItemDao;
    private final FinanceSettleCarryoverOrderDao carryoverOrderDao;
    private final SupplierDao supplierDao;
    private final LogBaseService logBaseService;
    private final McRemoteService mcRemoteService;
    private final ConsistencyService consistencyService;

    /**
     * 根据供应商编码查询对账状态为“对账完成”的对账单信息，并剔除不符合条件的对账单。
     *
     * @param supplierCode 供应商编码
     * @return 符合条件的对账单信息列表，如果没有符合条件的对账单则返回空列表
     */
    public List<FinanceRecoOrderPo> getRecoOrdersBySupplierCode(String supplierCode) {
        // 查询对账状态=对账完成，供应商编码= xx 的对账单号列表
        List<FinanceRecoOrderPo> recoOrderPos = recoOrderDao.getRecoOrdersBySupplierCodeAndStatus(supplierCode,
                FinanceRecoOrderStatus.COMPLETE);
        // 如果对账单列表为空，直接返回
        if (CollectionUtils.isEmpty(recoOrderPos)) {
            log.info("创建结算单结束！供应商{}不存在对账完成的结算单列表", supplierCode);
            return Collections.emptyList();
        }

        // 获取对账单号列表
        Set<String> recoOrderNos = recoOrderPos.stream()
                .map(FinanceRecoOrderPo::getFinanceRecoOrderNo)
                .collect(Collectors.toSet());
        List<FinanceSettleOrderItemPo> settleOrderItem = settleOrderItemDao.getSettlementOrdersByRecoOrderNos(
                recoOrderNos);
        // 如果结算单明细为空，说明不存在关联的结算单，直接返回对账明细
        if (CollectionUtils.isEmpty(settleOrderItem)) {
            return recoOrderPos;
        }

        // 查询结算单列表
        Set<String> settleOrderNos = settleOrderItem.stream()
                .map(FinanceSettleOrderItemPo::getFinanceSettleOrderNo)
                .collect(Collectors.toSet());
        List<FinanceSettleOrderPo> settleOrderPos = settleOrderDao.findByOrderNos(settleOrderNos);

        Iterator<FinanceRecoOrderPo> iterator = recoOrderPos.iterator();
        while (iterator.hasNext()) {
            FinanceRecoOrderPo recoOrderPo = iterator.next();
            String recoOrderNo = recoOrderPo.getFinanceRecoOrderNo();

            // 判断是否存在结算明细
            List<FinanceSettleOrderItemPo> matchSettleItems = settleOrderItem.stream()
                    .filter(settleOrderItemPo -> Objects.equals(recoOrderNo,
                            settleOrderItemPo.getBusinessNo()) && Objects.equals(
                            FinanceSettleOrderItemType.RECO_ORDER, settleOrderItemPo.getFinanceSettleOrderItemType()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(matchSettleItems)) {
                List<String> matchSettleOrderNos = matchSettleItems.stream()
                        .map(FinanceSettleOrderItemPo::getFinanceSettleOrderNo)
                        .collect(Collectors.toList());

                // 寻找非已作废的结算单
                List<FinanceSettleOrderPo> matchSettleOrders = settleOrderPos.stream()
                        .filter(settleOrderPo -> matchSettleOrderNos.contains(
                                settleOrderPo.getFinanceSettleOrderNo()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(matchSettleOrders)) {
                    boolean existUnInvalidate = matchSettleOrders.stream()
                            .anyMatch(matchSettleOrder -> !Objects.equals(FinanceSettleOrderStatus.INVALIDATE,
                                    matchSettleOrder.getFinanceSettleOrderStatus()));
                    if (existUnInvalidate) {
                        iterator.remove();
                    }
                }
            }
        }


        if (CollectionUtils.isEmpty(recoOrderPos)) {
            return Collections.emptyList();
        }
        return recoOrderPos;
    }

    /**
     * 获取可创建结算单的供应商编码列表。
     * 1. 查询所有对账状态为“对账完成”的对账单信息。
     * 2. 获取对账单中的供应商编码集合。
     *
     * @return 可创建结算单的供应商编码集合
     */
    public Set<String> getAvailableSupplierCodesForCreateSettlement() {
        // 第一步：查询所有对账状态为“对账完成”的对账单信息
        List<FinanceRecoOrderPo> financeRecoOrderPos = this.getRecoOrdersBySupplierCode(null);

        // 获取所有供应商编码集合
        return financeRecoOrderPos.stream()
                .map(FinanceRecoOrderPo::getSupplierCode)
                .collect(Collectors.toSet());
    }

    public Set<String> getTodaySettleSuppliers() {
        // 获取当前日期的几号
        LocalDateTime localDateTime = TimeUtil.convertZone(LocalDateTime.now(), TimeZoneId.UTC, TimeZoneId.CN);
        String dayOfMonth = String.valueOf(localDateTime.getDayOfMonth());

        return supplierDao.findSupplierCodesBySettleTime(dayOfMonth);
    }

    /**
     * 计算结转明细。
     *
     * @param totalReAmount 结算总金额
     * @param supplierCode  供应商代码
     * @return 结转明细计算结果
     */
    public CarryoverCalculationBo calculateCarryoverDetails(BigDecimal totalReAmount,
                                                            String supplierCode) {
        // 创建结转明细计算结果对象
        CarryoverCalculationBo carryoverCalculationBo = new CarryoverCalculationBo();

        // 查询符合条件的结转订单列表
        List<FinanceSettleCarryoverOrderPo> carryoverOrderPos
                = carryoverOrderDao.getCarryoverOrdersBySupplierAndAmount(supplierCode, BigDecimal.ZERO);

        // 如果没有符合条件的结转订单，则直接返回
        if (CollectionUtils.isEmpty(carryoverOrderPos)) {
            carryoverCalculationBo.setTotalReAmount(totalReAmount);
            carryoverCalculationBo.setCarryoverOrders(Collections.emptyList());
            return carryoverCalculationBo;
        }

        // 初始化结转明细列表和待更新的结转订单列表
        List<CarryoverCalculationBo.CarryoverOrderBo> carryoverDetails = new ArrayList<>();
        List<FinanceSettleCarryoverOrderPo> updateFinanceSettleCarryoverOrderPos = new ArrayList<>();

        // 遍历结转订单列表
        Iterator<FinanceSettleCarryoverOrderPo> iterator = carryoverOrderPos.iterator();
        while (iterator.hasNext()) {
            FinanceSettleCarryoverOrderPo financeSettleCarryoverOrderPo = iterator.next();

            // 如果总金额为零，则直接返回结果
            if (totalReAmount.compareTo(BigDecimal.ZERO) == 0) {
                carryoverCalculationBo.setTotalReAmount(totalReAmount);
                carryoverCalculationBo.setCarryoverOrders(carryoverDetails);
                carryoverCalculationBo.setUpdateFinanceSettleCarryoverOrderPos(updateFinanceSettleCarryoverOrderPos);
                return carryoverCalculationBo;
            }

            // 计算结转金额
            BigDecimal deductionAmount = totalReAmount.min(financeSettleCarryoverOrderPo.getAvailableCarryoverAmount());
            totalReAmount = totalReAmount.subtract(deductionAmount);

            // 更新订单的可结转金额
            BigDecimal updatedAvailableCarryoverAmount = financeSettleCarryoverOrderPo.getAvailableCarryoverAmount()
                    .subtract(deductionAmount);
            if (updatedAvailableCarryoverAmount.compareTo(BigDecimal.ZERO) < 0) {
                log.error("结算单创建异常，扣减结转单{}可结转金额后结果小于0，已添加结转单列表{} 请相关同事注意！",
                        financeSettleCarryoverOrderPo.getFinanceSettleCarryoverOrderNo(),
                        JSON.toJSONString(carryoverDetails));
                throw new BizException("创建失败！扣减结转单{} 可结转金额异常。",
                        financeSettleCarryoverOrderPo.getFinanceSettleCarryoverOrderNo());
            }

            financeSettleCarryoverOrderPo.setAvailableCarryoverAmount(updatedAvailableCarryoverAmount);

            // 构建结转明细对象并添加到列表中
            CarryoverCalculationBo.CarryoverOrderBo carryoverDetail = new CarryoverCalculationBo.CarryoverOrderBo();
            carryoverDetail.setCarryoverNo(financeSettleCarryoverOrderPo.getFinanceSettleCarryoverOrderNo());
            carryoverDetail.setCarryoverAmount(deductionAmount);
            carryoverDetails.add(carryoverDetail);

            // 构建待更新的结转订单对象并添加到列表中
            FinanceSettleCarryoverOrderPo updateSettleCarryoverOrderPo = new FinanceSettleCarryoverOrderPo();
            updateSettleCarryoverOrderPo.setFinanceSettleCarryoverOrderId(
                    financeSettleCarryoverOrderPo.getFinanceSettleCarryoverOrderId());
            updateSettleCarryoverOrderPo.setAvailableCarryoverAmount(
                    financeSettleCarryoverOrderPo.getAvailableCarryoverAmount());
            updateSettleCarryoverOrderPo.setVersion(financeSettleCarryoverOrderPo.getVersion());
            updateFinanceSettleCarryoverOrderPos.add(updateSettleCarryoverOrderPo);

            // 如果订单的可结转金额为零，则移除该订单
            if (financeSettleCarryoverOrderPo.getAvailableCarryoverAmount()
                    .compareTo(BigDecimal.ZERO) == 0) {
                iterator.remove();
            }
        }

        // 设置计算结果并返回
        carryoverCalculationBo.setTotalReAmount(totalReAmount);
        carryoverCalculationBo.setCarryoverOrders(carryoverDetails);
        carryoverCalculationBo.setUpdateFinanceSettleCarryoverOrderPos(updateFinanceSettleCarryoverOrderPos);
        return carryoverCalculationBo;
    }


    @Transactional(rollbackFor = Exception.class)
    public void batchTransfer(List<FinanceSettleOrderPo> notExistWorkFlowList,
                              UserVo toUserInfo) {
        notExistWorkFlowList.forEach(notExistWorkFlow -> {
            notExistWorkFlow.setCtrlUser(toUserInfo.getUserCode());
            notExistWorkFlow.setCtrlUsername(toUserInfo.getUsername());
            settleOrderDao.updateByIdVersion(notExistWorkFlow);

            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("转交给");
            logVersionBo.setValue("【" + toUserInfo.getUsername() + "】");
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logBaseService.simpleLog(LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                    notExistWorkFlow.getFinanceSettleOrderNo(),
                    notExistWorkFlow.getFinanceSettleOrderStatus()
                            .getRemark(), Collections.singletonList(logVersionBo));
        });


    }

    @Transactional(rollbackFor = Exception.class)
    public void batchTransferWithFeishu(List<FinanceSettleOrderPo> existWorkFlowList,
                                        List<FinanceSettleOrderTransferDto.FianceSettleOrderTransferItemDto> transferItemDtoList,
                                        UserVo toUserInfo) {
        List<String> failSettleOrderNos = Lists.newArrayList();
        for (FinanceSettleOrderPo settleOrderPo : existWorkFlowList) {
            String settleOrderNo = settleOrderPo.getFinanceSettleOrderNo();
            String workflowNo
                    = ParamValidUtils.requireNotBlank(settleOrderPo.getWorkflowNo(),
                    "转交失败！结算单关联飞书审批单号信息缺失，请联系相关业务人员");

            FinanceSettleOrderTransferDto.FianceSettleOrderTransferItemDto matchDto = transferItemDtoList.stream()
                    .filter(dto -> Objects.equals(settleOrderNo, dto.getSettleOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (Objects.isNull(matchDto)) {
                log.info("转交跳过，无法通过结算单匹配入参信息，结算单号:{}", settleOrderNo);
                continue;
            }

            String taskId
                    = ParamValidUtils.requireNotBlank(matchDto.getTaskId(),
                    "转交失败！结算单关联飞书审批任务编号信息缺失，请联系相关业务人员");
            String comment = matchDto.getComment();

            WorkflowTransferDto transferParam = FinanceSettleOrderBuilder.buildWorkflowTransferDto(workflowNo, taskId,
                    comment, toUserInfo);
            try {
                mcRemoteService.transferWorkFlow(transferParam);

                LogVersionBo logVersionBo = new LogVersionBo();
                logVersionBo.setKey("转交给");
                logVersionBo.setValue("【" + toUserInfo.getUsername() + "】");
                logVersionBo.setValueType(LogVersionValueType.STRING);
                logBaseService.simpleLog(LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                        settleOrderPo.getFinanceSettleOrderNo(),
                        settleOrderPo.getFinanceSettleOrderStatus()
                                .getRemark(), Collections.singletonList(logVersionBo));

                settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.FEISHU_APPROVAL_PROCESSING);
                settleOrderDao.updateByIdVersion(settleOrderPo);
            } catch (Exception e) {
                log.error("结算单飞书转交发送失败", e);
                failSettleOrderNos.add(settleOrderNo);
            }
        }

        if (CollectionUtils.isNotEmpty(failSettleOrderNos)) {
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(failSettleOrderNos)) {
                throw new ParamIllegalException(
                        "转交条件：当前登录账号为单据处理人，且转交对象非审批节点前置处理人。结算单转交失败单号：{}",
                        failSettleOrderNos.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")));
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void createFinanceSettleOrderJob(String supplierCode) {
        SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);
        consistencyService.execAsyncTask(CreateSettleOrderHandler.class, supplierPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSettleOrder(FinanceSettleOrderPo settleOrderPo) {
        settleOrderDao.updateByIdVersion(settleOrderPo);
    }
}
