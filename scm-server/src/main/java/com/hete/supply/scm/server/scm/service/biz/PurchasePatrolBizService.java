package com.hete.supply.scm.server.scm.service.biz;

import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.vo.PurchaseCheckSupplierCodeVo;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchQueryRelateOrderDto;
import com.hete.supply.wms.api.basic.entity.vo.SkuBatchRelateOrderVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/12/7 09:28
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PurchasePatrolBizService {
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final WmsRemoteService wmsRemoteService;


    /**
     * 按接单时间最近x天校验
     */
    private final static Integer CHECK_RECEIVE_ORDER_TIME_DAYS = 2;


    public void purchaseCheckSupplierCodeTask() {
        // scm数据
        final List<PurchaseCheckSupplierCodeVo> purchaseCheckSupplierCodeList = purchaseChildOrderDao.getListByLastFewDays(CHECK_RECEIVE_ORDER_TIME_DAYS);
        final List<String> skuBatchCodeList = purchaseCheckSupplierCodeList.stream()
                .map(PurchaseCheckSupplierCodeVo::getSkuBatchCode)
                .distinct()
                .collect(Collectors.toList());

        final SkuBatchQueryRelateOrderDto skuBatchQueryRelateOrderDto = new SkuBatchQueryRelateOrderDto();
        skuBatchQueryRelateOrderDto.setBatchCodeList(skuBatchCodeList);
        // wms数据
        final List<SkuBatchRelateOrderVo> skuBatchRelateOrderVoList = wmsRemoteService.getRelateOrderNoList(skuBatchQueryRelateOrderDto);
        final Map<String, SkuBatchRelateOrderVo> skuBatchCodeRelateVoMap = skuBatchRelateOrderVoList.stream()
                .collect(Collectors.toMap(SkuBatchRelateOrderVo::getBatchCode, Function.identity()));


        StringBuilder errorMessage = new StringBuilder();
        purchaseCheckSupplierCodeList.forEach(purchaseCheckSupplierCodeVo -> {
            final SkuBatchRelateOrderVo skuBatchRelateOrderVo = skuBatchCodeRelateVoMap.get(purchaseCheckSupplierCodeVo.getSkuBatchCode());
            if (null == skuBatchRelateOrderVo) {
                errorMessage.append("scm的sku批次码：").append(purchaseCheckSupplierCodeVo.getSkuBatchCode()).append("找不到对应的数据！\n");
                return;
            }
            if (!skuBatchRelateOrderVo.getRelateOrderNo().equals(purchaseCheckSupplierCodeVo.getPurchaseChildOrderNo())) {
                errorMessage.append("sku批次码:").append(purchaseCheckSupplierCodeVo.getSkuBatchCode())
                        .append("对应的采购单号不同\n");
            }
            if (!skuBatchRelateOrderVo.getSupplierCode().equals(purchaseCheckSupplierCodeVo.getSupplierCode())) {
                errorMessage.append("sku批次码:").append(purchaseCheckSupplierCodeVo.getSkuBatchCode())
                        .append("对应的供应商不同\n");
            }
        });
        if (StringUtils.isNotBlank(errorMessage.toString())) {
            errorMessage.insert(0, "采购单批次码巡检异常结果：\n");
            log.error(errorMessage.toString());
        }
    }
}
