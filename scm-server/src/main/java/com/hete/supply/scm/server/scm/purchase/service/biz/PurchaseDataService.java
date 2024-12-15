package com.hete.supply.scm.server.scm.purchase.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.mc.api.feishu.entity.vo.FeiShuAccessTokenVo;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.feishu.client.FeiShuExcelClient;
import com.hete.supply.scm.server.scm.feishu.config.FeiShuConfig;
import com.hete.supply.scm.server.scm.feishu.entity.dto.AppendDto;
import com.hete.supply.scm.server.scm.feishu.entity.dto.PrependDto;
import com.hete.supply.scm.server.scm.feishu.entity.vo.FeiShuResult;
import com.hete.supply.scm.server.scm.feishu.entity.vo.PrependVo;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseParentOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseParentOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import com.hete.supply.scm.server.scm.purchase.enums.OrderType;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.sda.api.scm.purchase.entity.vo.ScmPurchaseCntVo;
import com.hete.supply.sda.api.scm.purchase.entity.vo.ScmPurchaseDeliverDataVo;
import com.hete.supply.sda.api.scm.sku.entity.vo.ScmSkuOrderStockoutReasonVo;
import com.hete.supply.sda.api.scm.sku.entity.vo.ScmSkuStockoutMonthReasonVo;
import com.hete.supply.sda.api.scm.sku.entity.vo.ScmSkuStockoutReasonVo;
import com.hete.supply.sda.api.scm.sku.entity.vo.ScmSkuStockoutVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/3/28 09:45
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Validated
public class PurchaseDataService {
    private final SdaRemoteService sdaRemoteService;
    private final McRemoteService mcRemoteService;
    private final FeiShuExcelClient feiShuExcelClient;
    private final FeiShuConfig feiShuConfig;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final PurchaseParentOrderItemDao purchaseParentOrderItemDao;
    private final LogBaseService logBaseService;


    public void pushSkuStockOutMonthReason() {
        final List<ScmSkuStockoutMonthReasonVo> skuStockOutVoList = sdaRemoteService.getSkuStockoutMonthReason(LocalDate.now());
        final FeiShuAccessTokenVo feiShuToken = mcRemoteService.getFeiShuToken();
        final AppendDto appendDto = new AppendDto();
        final AppendDto.ValueRange valueRange = new AppendDto.ValueRange();
        valueRange.setRange(feiShuConfig.getSkuStockOutMonthReasonSheet() + "!A2:E");

        List<Object[]> result = skuStockOutVoList.stream()
                .map(vo -> new Object[]{vo.getCreateDate(), vo.getSku(), vo.getReason(), vo.getDutyDept(), vo.getStockoutDay()})
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        valueRange.setValues(result);
        appendDto.setValueRange(valueRange);

        feiShuExcelClient.append(feiShuToken.getAccessToken(), feiShuConfig.getSkuStockOutTable(), appendDto);
    }


    public void pushSkuStockOutOrderReason() {
        final List<ScmSkuOrderStockoutReasonVo> skuStockOutVoList = sdaRemoteService.getSkuStockoutOrderReason(LocalDate.now());
        final FeiShuAccessTokenVo feiShuToken = mcRemoteService.getFeiShuToken();
        final AppendDto appendDto = new AppendDto();
        final AppendDto.ValueRange valueRange = new AppendDto.ValueRange();
        valueRange.setRange(feiShuConfig.getSkuStockOutOrderReasonSheet() + "!A2:E");

        List<Object[]> result = skuStockOutVoList.stream()
                .map(vo -> new Object[]{vo.getCreateDate(), vo.getOrderNo(), vo.getSku(), vo.getReason(), vo.getDutyDept()})
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        valueRange.setValues(result);
        appendDto.setValueRange(valueRange);

        feiShuExcelClient.append(feiShuToken.getAccessToken(), feiShuConfig.getSkuStockOutTable(), appendDto);
    }

    public void pushSkuStockOutReason() {
        final List<ScmSkuStockoutReasonVo> skuStockOutVoList = sdaRemoteService.getSkuStockoutReason(LocalDate.now());

        final FeiShuAccessTokenVo feiShuToken = mcRemoteService.getFeiShuToken();
        final AppendDto appendDto = new AppendDto();
        final AppendDto.ValueRange valueRange = new AppendDto.ValueRange();
        valueRange.setRange(feiShuConfig.getSkuStockOutReasonSheet() + "!A2:F");

        List<Object[]> result = skuStockOutVoList.stream()
                .map(vo -> new Object[]{vo.getCreateDate(), vo.getSku(), vo.getReason(), vo.getStockoutAmount(),
                        vo.getStockoutDay(), vo.getDutyDept()})
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        valueRange.setValues(result);
        appendDto.setValueRange(valueRange);

        feiShuExcelClient.append(feiShuToken.getAccessToken(), feiShuConfig.getSkuStockOutTable(), appendDto);
    }

    public void pushSkuStockOutData() {
        final List<ScmSkuStockoutVo> skuStockOutVoList = sdaRemoteService.getSkuStockout(LocalDate.now());
        final FeiShuAccessTokenVo feiShuToken = mcRemoteService.getFeiShuToken();
        final AppendDto appendDto = new AppendDto();
        final AppendDto.ValueRange valueRange = new AppendDto.ValueRange();
        valueRange.setRange(feiShuConfig.getSkuStockOutSheet() + "!A2:F");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Object[]> result = skuStockOutVoList.stream()
                .map(vo -> new Object[]{vo.getCreateDate(), vo.getSku(), vo.getOrderNo(),
                        OrderType.valueOf(vo.getOrderType()).getRemark(),
                        vo.getOrderCreateTime().format(formatter), vo.getDeliverDate().format(formatter)})
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        valueRange.setValues(result);
        appendDto.setValueRange(valueRange);

        feiShuExcelClient.append(feiShuToken.getAccessToken(), feiShuConfig.getSkuStockOutTable(), appendDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void pushPurchaseCntData() {
        final ScmPurchaseCntVo purchaseCntData = sdaRemoteService.getPurchaseCntData(LocalDate.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        final FeiShuAccessTokenVo feiShuToken = mcRemoteService.getFeiShuToken();
        Object[] arr = {purchaseCntData.getTtMoPurchaseCnt(), purchaseCntData.getTtWeekPurchaseCnt(),
                purchaseCntData.getTtMoBackPurchaseCnt(), purchaseCntData.getTtWeekBackPurchaseCnt(),
                purchaseCntData.getTtMoActBackCnt(), purchaseCntData.getTtWeekActBackCnt(),
                purchaseCntData.getNttMoPurchaseCnt(), purchaseCntData.getNttWeekPurchaseCnt(),
                purchaseCntData.getNttMoBackPurchaseCnt(), purchaseCntData.getNttWeekBackPurchaseCnt(),
                purchaseCntData.getNttMoActBackCnt(), purchaseCntData.getNttWeekActBackCnt(),
                purchaseCntData.getCreateDate().format(formatter)};

        final PrependDto prependDto = new PrependDto();
        final PrependDto.ValueRange valueRange = new PrependDto.ValueRange();
        valueRange.setRange(feiShuConfig.getPurchaseSheet() + "!A2:M2");
        valueRange.setValues(Collections.singletonList(arr));
        prependDto.setValueRange(valueRange);
        FeiShuResult<PrependVo> prependVo = feiShuExcelClient.prepend(feiShuToken.getAccessToken(), feiShuConfig.getPurchaseTable(), prependDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void purchaseChildFinishJob() {
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getCanFinishPurchase();
        if (CollectionUtils.isEmpty(purchaseChildOrderPoList)) {
            return;
        }

        purchaseChildOrderPoList.forEach(po -> {
            po.setPurchaseOrderStatus(PurchaseOrderStatus.FINISH);
            po.setCapacity(BigDecimal.ZERO);
            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getPurchaseChildOrderNo(), PurchaseOrderStatus.FINISH.getRemark(), Collections.emptyList());
            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getPurchaseChildOrderNo(), po, po.getCapacity());
        });
        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);


    }

    @Transactional(rollbackFor = Exception.class)
    public void purchaseParentCompleteJob() {
        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getCanCompletePurchase();
        if (CollectionUtils.isEmpty(purchaseParentOrderPoList)) {
            return;
        }
        purchaseParentOrderPoList.forEach(po -> {
            po.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.COMPLETED);
            po.setUndeliveredCnt(0);
            po.setCanSplitCnt(0);

            logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getPurchaseParentOrderNo(), PurchaseParentOrderStatus.COMPLETED.getRemark(),
                    Collections.emptyList());
        });
        purchaseParentOrderDao.updateBatchByIdVersion(purchaseParentOrderPoList);
        final List<String> purchaseParentNoList = purchaseParentOrderPoList.stream()
                .map(PurchaseParentOrderPo::getPurchaseParentOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNoList(purchaseParentNoList);

        purchaseParentOrderItemPoList.forEach(purchaseParentOrderItemPo -> purchaseParentOrderItemPo.setCanSplitCnt(0));
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);

    }

    public void purchaseDeliverDataJob(String date) {
        LocalDate localDate = LocalDate.now();
        if (StringUtils.isNotBlank(date)) {
            localDate = LocalDate.parse(date);
        }
        final List<ScmPurchaseDeliverDataVo> purchaseDeliverVoList = sdaRemoteService.getPurchaseDeliverData(localDate);
        final FeiShuAccessTokenVo feiShuToken = mcRemoteService.getFeiShuToken();
        final AppendDto appendDto = new AppendDto();
        final AppendDto.ValueRange valueRange = new AppendDto.ValueRange();
        valueRange.setRange(feiShuConfig.getPurchaseDeliverDataSheet() + "!A2:Q");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        final List<String> platCodeList = purchaseDeliverVoList.stream()
                .map(ScmPurchaseDeliverDataVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);


        List<Object[]> result = purchaseDeliverVoList.stream()
                .map(vo -> new Object[]{vo.getPurchaseChildOrderNo(), vo.getPurchaseTotal(),
                        vo.getPurchaseParentOrderNo(), vo.getPurchaseDeliverOrderNo(), vo.getPurchaseReceiptOrderNo(),
                        vo.getWarehouseCode(), vo.getWarehouseName(), vo.getSupplierCode(), vo.getSku(),
                        vo.getSkuEncode(), PurchaseOrderStatus.valueOf(vo.getPurchaseOrderStatus()).getRemark(),
                        platCodeNameMap.get(vo.getPlatform()), vo.getWarehousingTime().format(formatter),
                        vo.getExpectedOnShelvesDate().format(formatter), vo.getDeliverCnt(), vo.getReceiptCnt(),
                        vo.getQualityGoodsCnt()})
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        valueRange.setValues(result);
        appendDto.setValueRange(valueRange);

        feiShuExcelClient.append(feiShuToken.getAccessToken(), feiShuConfig.getPurchaseDeliverDataTable(), appendDto);
    }
}
