package com.hete.supply.scm.server.scm.ibfs.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.mc.api.workflow.entity.dto.WorkflowTransferDto;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoPayType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.cost.dao.CostOfGoodsDao;
import com.hete.supply.scm.server.scm.cost.entity.bo.SkuAndWarehouseBatchBo;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceRecoOrderDao;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceRecoOrderItemSkuDao;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.RecoOrderBatchUpdateInfoBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.RecoOrderBindingSettleOrderBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.RecoOrderCreateBo;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.RecoOrderTransferDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.RecoOrderTransferItemDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemSkuPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.ibfs.handler.CollectRecoOrderHandler;
import com.hete.supply.scm.server.scm.ibfs.handler.CreateRecoOrderHandler;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author ChenWenLong
 * @date 2024/5/22 18:18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecoOrderBaseService {

    private final FinanceRecoOrderDao financeRecoOrderDao;
    private final McRemoteService mcRemoteService;
    private final LogBaseService logBaseService;
    private final FinanceRecoOrderItemSkuDao financeRecoOrderItemSkuDao;
    private final ConsistencyService consistencyService;
    private final DefectHandlingDao defectHandlingDao;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final CostOfGoodsDao costOfGoodsDao;


    /**
     * 更新对账单的金额
     *
     * @param financeRecoOrderPo: 更新PO
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 14:16
     */
    public void updateRecoOrderPrice(@NotNull FinanceRecoOrderPo financeRecoOrderPo) {
        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList = financeRecoOrderItemSkuDao.getListByFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());
        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoHandles = financeRecoOrderItemSkuPoList.stream().filter(po -> FinanceRecoPayType.HANDLE.equals(po.getFinanceRecoPayType())).collect(Collectors.toList());
        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoReceivables = financeRecoOrderItemSkuPoList.stream().filter(po -> FinanceRecoPayType.RECEIVABLE.equals(po.getFinanceRecoPayType())).collect(Collectors.toList());
        BigDecimal payPrice = financeRecoOrderItemSkuPoHandles.stream()
                .map(FinanceRecoOrderItemSkuPo::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal receivePrice = financeRecoOrderItemSkuPoReceivables.stream()
                .map(FinanceRecoOrderItemSkuPo::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal settlePrice = payPrice.subtract(receivePrice);
        financeRecoOrderPo.setSettlePrice(settlePrice);
        financeRecoOrderPo.setReceivePrice(receivePrice);
        financeRecoOrderPo.setPayPrice(payPrice);
        financeRecoOrderDao.updateByIdVersion(financeRecoOrderPo);
    }


    /**
     * 对账单绑定结算单
     *
     * @param recoOrderBindingSettleOrderBo:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/28 09:20
     */
    public void recoOrderBindingSettleOrder(RecoOrderBindingSettleOrderBo recoOrderBindingSettleOrderBo) {
        List<RecoOrderBindingSettleOrderBo.RecoOrderBindingSettleOrderItemBo> itemList = recoOrderBindingSettleOrderBo.getRecoOrderBindingSettleOrderItemList();
        List<String> financeRecoOrderNoList = itemList.stream().map(RecoOrderBindingSettleOrderBo.RecoOrderBindingSettleOrderItemBo::getFinanceRecoOrderNo).collect(Collectors.toList());
        List<FinanceRecoOrderPo> financeRecoOrderPoList = financeRecoOrderDao.getListByNoList(financeRecoOrderNoList);
        List<FinanceRecoOrderPo> updatePoList = new ArrayList<>();
        for (FinanceRecoOrderPo financeRecoOrderPo : financeRecoOrderPoList) {
            FinanceRecoOrderPo updatePo = new FinanceRecoOrderPo();
            updatePo.setFinanceRecoOrderId(financeRecoOrderPo.getFinanceRecoOrderId());
            updatePo.setVersion(financeRecoOrderPo.getVersion());
            updatePo.setFinanceSettleOrderNo(recoOrderBindingSettleOrderBo.getFinanceSettleOrderNo());
            updatePoList.add(updatePo);
        }
        financeRecoOrderDao.updateBatchByIdVersion(updatePoList);
    }

    /**
     * 转交业务处理逻辑
     *
     * @param financeRecoOrderPoList:
     * @param dto:
     * @param userVo:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/1 10:39
     */
    @Transactional(rollbackFor = Exception.class)
    public void simpleTransfer(List<FinanceRecoOrderPo> financeRecoOrderPoList,
                               RecoOrderTransferDto dto,
                               UserVo userVo) {

        financeRecoOrderPoList.forEach(po -> {
            po.setCtrlUser(dto.getTransferUser());
            final LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("转交给");
            logVersionBo.setValue("【" + userVo.getUsername() + "】");
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                    po.getFinanceRecoOrderNo(), po.getFinanceRecoOrderStatus().getRemark(),
                    Collections.singletonList(logVersionBo));
        });

        financeRecoOrderDao.updateBatchByIdVersion(financeRecoOrderPoList);
    }

    /**
     * 转交飞书处理逻辑
     *
     * @param financeRecoOrderDtoMap:
     * @param po:
     * @param userVo:
     * @param dto:
     * @param failRecoOrderNoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/1 10:40
     */
    @Transactional(rollbackFor = Exception.class)
    public void feiShuTransfer(Map<String, RecoOrderTransferItemDto> financeRecoOrderDtoMap,
                               FinanceRecoOrderPo po,
                               UserVo userVo,
                               RecoOrderTransferDto dto,
                               List<String> failRecoOrderNoList) {
        RecoOrderTransferItemDto recoOrderTransferItemDto = financeRecoOrderDtoMap.get(po.getFinanceRecoOrderNo());
        // 推飞书转交
        final WorkflowTransferDto workflowTransferDto = new WorkflowTransferDto();
        workflowTransferDto.setWorkflowNo(po.getWorkflowNo());
        workflowTransferDto.setTaskId(recoOrderTransferItemDto.getTaskId());
        workflowTransferDto.setComment(recoOrderTransferItemDto.getComment());
        workflowTransferDto.setTransferUserCode(dto.getTransferUser());
        workflowTransferDto.setUserCode(GlobalContext.getUserKey());
        try {
            mcRemoteService.transferWorkFlow(workflowTransferDto);
            // 日志
            final LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("转交给");
            logVersionBo.setValue("【" + userVo.getUsername() + "】");
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                    po.getFinanceRecoOrderNo(), po.getFinanceRecoOrderStatus().getRemark(),
                    Collections.singletonList(logVersionBo));
        } catch (Exception e) {
            log.error("飞书转交发送失败", e);
            failRecoOrderNoList.add(po.getFinanceRecoOrderNo());
        }
    }

    /**
     * 异步任务处理创建对账单
     *
     * @param recoOrderCreateBo:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/1 09:55
     */
    @Transactional(rollbackFor = Exception.class)
    public void createRecoOrderHandler(RecoOrderCreateBo recoOrderCreateBo) {
        // 执行异步任务处理每一个供应商
        consistencyService.execAsyncTask(CreateRecoOrderHandler.class, recoOrderCreateBo);
    }

    /**
     * 异步任务处理收单对账单
     *
     * @param financeRecoOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/1 09:55
     */
    @Transactional(rollbackFor = Exception.class)
    public void collectRecoOrderHandler(FinanceRecoOrderPo financeRecoOrderPo) {
        // 执行异步任务处理每一个对账单
        consistencyService.execAsyncTask(CollectRecoOrderHandler.class, financeRecoOrderPo);
    }


    /**
     * 删除已收单的对账单应付、应收金额均为0，进行软删除
     *
     * @param financeRecoOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/7/4 10:14
     */
    public void delRecoOrderAmountEqZero(List<FinanceRecoOrderPo> financeRecoOrderPoList) {
        if (CollectionUtils.isEmpty(financeRecoOrderPoList)) {
            return;
        }
        log.info("需要删除已收单的对账单应付、应收金额均为0的入参NO={}",
                JacksonUtil.parse2Str(financeRecoOrderPoList.stream().map(FinanceRecoOrderPo::getFinanceRecoOrderNo).collect(Collectors.toList())));
        // 筛选符合条件的列表
        List<FinanceRecoOrderPo> filteredList = financeRecoOrderPoList.stream()
                .filter(po -> po.getReceivePrice().compareTo(BigDecimal.ZERO) == 0
                        && po.getPayPrice().compareTo(BigDecimal.ZERO) == 0
                        && FinanceRecoOrderStatus.WAIT_SUBMIT.equals(po.getFinanceRecoOrderStatus()))
                .collect(Collectors.toList());

        log.info("需要删除已收单的对账单应付、应收金额均为0的NO={}",
                JacksonUtil.parse2Str(filteredList.stream().map(FinanceRecoOrderPo::getFinanceRecoOrderNo).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(filteredList)) {
            return;
        }

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 将当前时间格式化为指定的格式并解析为 Long 类型
        Long delTimestamp = Long.parseLong(now.format(formatter));

        List<Long> financeRecoOrderIdList = filteredList.stream().map(FinanceRecoOrderPo::getFinanceRecoOrderId).collect(Collectors.toList());
        RecoOrderBatchUpdateInfoBo recoOrderBatchUpdateInfoBo = new RecoOrderBatchUpdateInfoBo();
        recoOrderBatchUpdateInfoBo.setDelTimestamp(delTimestamp);
        recoOrderBatchUpdateInfoBo.setUpdateUser(ScmConstant.SYSTEM_USER);
        recoOrderBatchUpdateInfoBo.setUpdateUsername(ScmConstant.JOB_DEFAULT_USER_NAME);
        financeRecoOrderDao.updateInfoByBatchId(financeRecoOrderIdList, recoOrderBatchUpdateInfoBo);

    }

    /**
     * 通过采购退货单计算对账时结算单价
     *
     * @param returnOrderNoList:
     * @return Map<String, BigDecimal>:返回值key:退货单号+批次码，value:结算单价
     * @author ChenWenLong
     * @date 2024/7/5 16:26
     */
    public Map<String, BigDecimal> getPurchaseReturnSettlePrice(List<String> returnOrderNoList) {
        if (CollectionUtils.isEmpty(returnOrderNoList)) {
            return Collections.emptyMap();
        }

        Map<String, BigDecimal> settlePriceMap = new HashMap<>();

        // 查询退货单详情
        Map<String, PurchaseReturnOrderPo> purchaseReturnOrderPoMap = purchaseReturnOrderDao.getMapByReturnOrderNoList(returnOrderNoList);
        List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnNoList(returnOrderNoList);

        // 获取次品记录信息，可以获得对应退货仓库以及结算价
        List<DefectHandlingPo> defectHandlingPoList = defectHandlingDao.getListByRelatedOrderNoList(returnOrderNoList);
        Map<String, DefectHandlingPo> defectHandlingPoRelatedMap = defectHandlingPoList.stream()
                .collect(Collectors.toMap(DefectHandlingPo::getRelatedOrderNo, Function.identity(),
                        (existing, replacement) -> existing));
        // 批量获取月初加权价
        List<SkuAndWarehouseBatchBo> boList = purchaseReturnOrderItemPoList.stream()
                .map(purchaseReturnOrderItemPo -> {
                    SkuAndWarehouseBatchBo skuAndWarehouseBatchBo = new SkuAndWarehouseBatchBo();
                    // 获取关联的 月初加权单价
                    DefectHandlingPo defectHandlingPo = defectHandlingPoRelatedMap.get(purchaseReturnOrderItemPo.getReturnOrderNo());
                    if (null == defectHandlingPo) {
                        return null;
                    }
                    // 判断收货的状态是否收货而且存在收货时间
                    PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderPoMap.get(purchaseReturnOrderItemPo.getReturnOrderNo());
                    if (purchaseReturnOrderPo == null || null == purchaseReturnOrderPo.getReceiptTime()) {
                        return null;
                    }

                    if (StringUtils.isBlank(defectHandlingPo.getWarehouseCode())) {
                        return null;
                    }

                    skuAndWarehouseBatchBo.setSku(purchaseReturnOrderItemPo.getSku());
                    skuAndWarehouseBatchBo.setWarehouseCode(defectHandlingPo.getWarehouseCode());
                    // 查询商品成本的月初加权单价
                    final String currentMonthString = ScmTimeUtil.getSpecifyMonthString(purchaseReturnOrderPo.getReceiptTime());
                    skuAndWarehouseBatchBo.setPolymerizeType(PolymerizeType.SINGLE_WAREHOUSE);
                    skuAndWarehouseBatchBo.setCostTime(currentMonthString);
                    return skuAndWarehouseBatchBo;
                }).filter(Objects::nonNull).collect(Collectors.toList());

        List<CostOfGoodsPo> costOfGoodsPoList = costOfGoodsDao.getMoDataByBatchBo(boList);


        for (PurchaseReturnOrderItemPo purchaseReturnOrderItemPo : purchaseReturnOrderItemPoList) {
            BigDecimal settlePrice = purchaseReturnOrderItemPo.getSettlePrice();

            // 判断收货的状态是否收货而且存在收货时间
            PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderPoMap.get(purchaseReturnOrderItemPo.getReturnOrderNo());
            if (purchaseReturnOrderPo == null) {
                continue;
            }

            // 获取关联的 月初加权单价
            DefectHandlingPo defectHandlingPo = defectHandlingPoRelatedMap.get(purchaseReturnOrderItemPo.getReturnOrderNo());
            if (this.shouldFetchWeightingPrice(settlePrice, purchaseReturnOrderPo, defectHandlingPo)) {
                settlePrice = this.fetchWeightingPrice(purchaseReturnOrderItemPo,
                        defectHandlingPo,
                        purchaseReturnOrderPo.getReceiptTime(),
                        costOfGoodsPoList);
            }

            settlePriceMap.putIfAbsent(purchaseReturnOrderItemPo.getReturnOrderNo() + purchaseReturnOrderItemPo.getSkuBatchCode(),
                    settlePrice);
        }
        log.info("查询预计结算单价Map={}", settlePriceMap);
        return settlePriceMap;

    }

    /**
     * 判断是否查询月初加权单价
     *
     * @param settlePrice:
     * @param purchaseReturnOrderPo:
     * @param defectHandlingPo:
     * @return boolean
     * @author ChenWenLong
     * @date 2024/7/10 11:36
     */
    private boolean shouldFetchWeightingPrice(BigDecimal settlePrice,
                                              PurchaseReturnOrderPo purchaseReturnOrderPo,
                                              DefectHandlingPo defectHandlingPo) {
        return defectHandlingPo != null
                && StringUtils.isNotBlank(defectHandlingPo.getWarehouseCode())
                && purchaseReturnOrderPo.getReceiptTime() != null
                && (settlePrice == null || BigDecimal.ZERO.compareTo(settlePrice) == 0);
    }

    /**
     * 查询商品成本的月初加权单价
     *
     * @param purchaseReturnOrderItemPo:
     * @param defectHandlingPo:
     * @param receiptTime:
     * @param costOfGoodsPoList:
     * @return BigDecimal
     * @author ChenWenLong
     * @date 2024/7/10 11:36
     */
    private BigDecimal fetchWeightingPrice(PurchaseReturnOrderItemPo purchaseReturnOrderItemPo,
                                           DefectHandlingPo defectHandlingPo,
                                           LocalDateTime receiptTime,
                                           List<CostOfGoodsPo> costOfGoodsPoList) {
        // 查询商品成本的月初加权单价
        final String currentMonthString = ScmTimeUtil.getSpecifyMonthString(receiptTime);
        CostOfGoodsPo costOfGoodsMonthPo = costOfGoodsPoList.stream()
                .filter(costOfGoodsPo -> costOfGoodsPo.getSku().equals(purchaseReturnOrderItemPo.getSku()))
                .filter(costOfGoodsPo -> costOfGoodsPo.getWarehouseCode().equals(defectHandlingPo.getWarehouseCode()))
                .filter(costOfGoodsPo -> PolymerizeType.SINGLE_WAREHOUSE.equals(costOfGoodsPo.getPolymerizeType()))
                .filter(costOfGoodsPo -> currentMonthString.equals(costOfGoodsPo.getCostTime()))
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(costOfGoodsMonthPo)) {
            return Objects.isNull(costOfGoodsMonthPo.getWeightingPrice())
                    || costOfGoodsMonthPo.getWeightingPrice().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : costOfGoodsMonthPo.getWeightingPrice();
        }
        return BigDecimal.ZERO;
    }

}
