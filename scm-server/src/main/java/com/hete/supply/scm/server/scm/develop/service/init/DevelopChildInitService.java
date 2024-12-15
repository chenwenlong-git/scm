
package com.hete.supply.scm.server.scm.develop.service.init;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.server.scm.develop.dao.DevelopChildOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopOrderPriceDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPricingOrderInfoDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopOrderPricePo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderInfoPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.support.api.enums.BooleanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/1/26 16:40
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DevelopChildInitService {

    private final DevelopSampleOrderDao developSampleOrderDao;
    private final DevelopPricingOrderInfoDao developPricingOrderInfoDao;
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopOrderPriceDao developOrderPriceDao;

    /**
     * 初始化开发单大货渠道价格
     *
     * @param :
     * @return void
     * @author ChenWenLong
     * @date 2024/8/26 09:31
     */
    @Transactional(rollbackFor = Exception.class)
    public void initDevelopOrderPricePrice() {
        // 初始化开发单大货渠道价格
        this.initDevelopChildOrder();
        // 初始化核价单表详情信息大货渠道价格
        this.initDevelopPricingOrderInfo();
        // 初始化样品单大货渠道价格,供应商报价
        this.initDevelopSampleOrder();

    }

    /**
     * 初始化开发子单默认大货价格
     *
     * @param :
     * @return void
     * @author ChenWenLong
     * @date 2024/8/26 09:59
     */
    private void initDevelopChildOrder() {
        List<DevelopChildOrderPo> developChildOrderPos = developChildOrderDao.getAll();
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderPos.stream()
                .filter(item -> item.getPurchasePrice() != null && item.getPurchasePrice().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        List<String> developChildOrderNos = developChildOrderPoList.stream().map(DevelopChildOrderPo::getDevelopChildOrderNo).collect(Collectors.toList());
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(developChildOrderNos, List.of(DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE));
        // 写入新数据
        List<DevelopOrderPricePo> developOrderPricePoNewList = new ArrayList<>();
        List<DevelopOrderPricePo> developOrderPricePoUpdateList = new ArrayList<>();
        for (DevelopChildOrderPo developChildOrderPo : developChildOrderPoList) {
            DevelopOrderPricePo developOrderPriceFilterPo = developOrderPricePoList.stream()
                    .filter(developOrderPricePo -> (null == developOrderPricePo.getChannelId() || 0 == developOrderPricePo.getChannelId())
                            && developOrderPricePo.getDevelopOrderNo().equals(developChildOrderPo.getDevelopChildOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (null == developOrderPriceFilterPo) {
                DevelopOrderPricePo developOrderPricePo = new DevelopOrderPricePo();
                developOrderPricePo.setDevelopOrderNo(developChildOrderPo.getDevelopChildOrderNo());
                developOrderPricePo.setDevelopOrderPriceType(DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE);
                developOrderPricePo.setPrice(developChildOrderPo.getPurchasePrice());
                developOrderPricePoNewList.add(developOrderPricePo);
            } else {
                developOrderPriceFilterPo.setPrice(developChildOrderPo.getPurchasePrice());
                developOrderPricePoUpdateList.add(developOrderPriceFilterPo);
            }

        }
        developOrderPriceDao.insertBatch(developOrderPricePoNewList);
        if (CollectionUtils.isNotEmpty(developOrderPricePoUpdateList)) {
            developOrderPriceDao.updateBatchById(developOrderPricePoUpdateList);
        }
    }

    private void initDevelopPricingOrderInfo() {
        List<DevelopPricingOrderInfoPo> developPricingOrderInfos = developPricingOrderInfoDao.getAll();
        List<DevelopPricingOrderInfoPo> developPricingOrderInfoList = developPricingOrderInfos.stream()
                .filter(item -> item.getBulkPrice() != null && item.getBulkPrice().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        List<String> developSampleOrderNos = developPricingOrderInfoList.stream().map(DevelopPricingOrderInfoPo::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());
        List<String> developChildOrderNos = developPricingOrderInfoList.stream().map(DevelopPricingOrderInfoPo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());
        List<String> developPricingOrderNos = developPricingOrderInfoList.stream().map(DevelopPricingOrderInfoPo::getDevelopPricingOrderNo).distinct().collect(Collectors.toList());

        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(developSampleOrderNos, List.of(DevelopOrderPriceType.PRICING_PURCHASE_PRICE));
        List<DevelopOrderPricePo> developOrderPriceNotPoList = developOrderPriceDao.getListByNoListAndTypeList(developPricingOrderNos, List.of(DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE));
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderDao.getMapByNoList(developChildOrderNos);
        // 写入新数据
        List<DevelopOrderPricePo> developOrderPricePoNewList = new ArrayList<>();
        List<DevelopOrderPricePo> developOrderPricePoUpdateList = new ArrayList<>();
        for (DevelopPricingOrderInfoPo developPricingOrderInfoPo : developPricingOrderInfoList) {
            if (StringUtils.isNotBlank(developPricingOrderInfoPo.getDevelopChildOrderNo())
                    && developChildOrderPoMap.containsKey(developPricingOrderInfoPo.getDevelopChildOrderNo())) {

                if (BooleanType.TRUE.equals(developChildOrderPoMap.get(developPricingOrderInfoPo.getDevelopChildOrderNo()).getIsSample())) {
                    DevelopOrderPricePo developOrderPriceFilterPo = developOrderPricePoList.stream()
                            .filter(developOrderPricePo -> (null == developOrderPricePo.getChannelId() || 0 == developOrderPricePo.getChannelId())
                                    && developOrderPricePo.getDevelopOrderNo().equals(developPricingOrderInfoPo.getDevelopSampleOrderNo()))
                            .findFirst()
                            .orElse(null);
                    if (null == developOrderPriceFilterPo) {
                        DevelopOrderPricePo developOrderPricePo = new DevelopOrderPricePo();
                        developOrderPricePo.setDevelopOrderNo(developPricingOrderInfoPo.getDevelopSampleOrderNo());
                        developOrderPricePo.setDevelopOrderPriceType(DevelopOrderPriceType.PRICING_PURCHASE_PRICE);
                        developOrderPricePo.setPrice(developPricingOrderInfoPo.getBulkPrice());
                        developOrderPricePoNewList.add(developOrderPricePo);
                    } else {
                        developOrderPriceFilterPo.setPrice(developPricingOrderInfoPo.getBulkPrice());
                        developOrderPricePoUpdateList.add(developOrderPriceFilterPo);
                    }
                } else {
                    DevelopOrderPricePo developOrderPriceFilterPo = developOrderPriceNotPoList.stream()
                            .filter(developOrderPricePo -> (null == developOrderPricePo.getChannelId() || 0 == developOrderPricePo.getChannelId())
                                    && developOrderPricePo.getDevelopOrderNo().equals(developPricingOrderInfoPo.getDevelopPricingOrderNo()))
                            .findFirst()
                            .orElse(null);
                    if (null == developOrderPriceFilterPo) {
                        DevelopOrderPricePo developOrderPricePo = new DevelopOrderPricePo();
                        developOrderPricePo.setDevelopOrderNo(developPricingOrderInfoPo.getDevelopPricingOrderNo());
                        developOrderPricePo.setDevelopOrderPriceType(DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE);
                        developOrderPricePo.setPrice(developPricingOrderInfoPo.getBulkPrice());
                        developOrderPricePoNewList.add(developOrderPricePo);
                    } else {
                        developOrderPriceFilterPo.setPrice(developPricingOrderInfoPo.getBulkPrice());
                        developOrderPricePoUpdateList.add(developOrderPriceFilterPo);
                    }
                }
            }
        }
        developOrderPriceDao.insertBatch(developOrderPricePoNewList);
        if (CollectionUtils.isNotEmpty(developOrderPricePoUpdateList)) {
            developOrderPriceDao.updateBatchById(developOrderPricePoUpdateList);
        }
    }


    private void initDevelopSampleOrder() {
        List<DevelopSampleOrderPo> developSampleOrderPos = developSampleOrderDao.getAll();
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderPos.stream()
                .filter(item -> item.getSkuBatchPurchasePrice() != null && item.getSkuBatchPurchasePrice().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        List<String> developSampleOrderNos = developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());

        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(developSampleOrderNos, List.of(DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE));

        List<DevelopSampleOrderPo> developSampleOrderPoSuppliers = developSampleOrderPos.stream()
                .filter(item -> item.getPurchasePrice() != null && item.getPurchasePrice().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        List<String> developSampleOrderSupplierNos = developSampleOrderPoSuppliers.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());

        List<DevelopOrderPricePo> developOrderPriceSupplierPoList = developOrderPriceDao.getListByNoListAndTypeList(developSampleOrderSupplierNos, List.of(DevelopOrderPriceType.SUPPLIER_SAMPLE_PURCHASE_PRICE));


        // 写入新数据
        List<DevelopOrderPricePo> developOrderPricePoNewList = new ArrayList<>();
        List<DevelopOrderPricePo> developOrderPricePoUpdateList = new ArrayList<>();

        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {

            DevelopOrderPricePo developOrderPriceFilterPo = developOrderPricePoList.stream()
                    .filter(developOrderPricePo -> (null == developOrderPricePo.getChannelId() || 0 == developOrderPricePo.getChannelId())
                            && developOrderPricePo.getDevelopOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (null == developOrderPriceFilterPo) {
                DevelopOrderPricePo developOrderPricePo = new DevelopOrderPricePo();
                developOrderPricePo.setDevelopOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                developOrderPricePo.setDevelopOrderPriceType(DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE);
                developOrderPricePo.setPrice(developSampleOrderPo.getSkuBatchPurchasePrice());
                developOrderPricePoNewList.add(developOrderPricePo);
            } else {
                developOrderPriceFilterPo.setPrice(developSampleOrderPo.getSkuBatchPurchasePrice());
                developOrderPricePoUpdateList.add(developOrderPriceFilterPo);
            }

        }

        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoSuppliers) {

            DevelopOrderPricePo developOrderPriceSupplierFilterPo = developOrderPriceSupplierPoList.stream()
                    .filter(developOrderPricePo -> (null == developOrderPricePo.getChannelId() || 0 == developOrderPricePo.getChannelId())
                            && developOrderPricePo.getDevelopOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (null == developOrderPriceSupplierFilterPo) {
                DevelopOrderPricePo developOrderPricePo = new DevelopOrderPricePo();
                developOrderPricePo.setDevelopOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                developOrderPricePo.setDevelopOrderPriceType(DevelopOrderPriceType.SUPPLIER_SAMPLE_PURCHASE_PRICE);
                developOrderPricePo.setPrice(developSampleOrderPo.getPurchasePrice());
                developOrderPricePoNewList.add(developOrderPricePo);
            } else {
                developOrderPriceSupplierFilterPo.setPrice(developSampleOrderPo.getPurchasePrice());
                developOrderPricePoUpdateList.add(developOrderPriceSupplierFilterPo);
            }

        }
        developOrderPriceDao.insertBatch(developOrderPricePoNewList);
        if (CollectionUtils.isNotEmpty(developOrderPricePoUpdateList)) {
            developOrderPriceDao.updateBatchById(developOrderPricePoUpdateList);
        }
    }
}

    