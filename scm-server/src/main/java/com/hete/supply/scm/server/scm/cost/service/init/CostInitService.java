package com.hete.supply.scm.server.scm.cost.service.init;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.cost.dao.CostOfGoodsDao;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.dao.ProduceDataAttrDao;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/2/29 16:56
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CostInitService {
    private final ProduceDataBaseService produceDataBaseService;
    private final CostOfGoodsDao costOfGoodsDao;
    private final ProduceDataAttrDao produceDataAttrDao;
    private final WmsRemoteService wmsRemoteService;

    /**
     * 页码
     */
    private final static Integer LOOP_SIZE = 50;

    @Transactional(rollbackFor = Exception.class)
    public void costMonthWeightingPriceInitJob() {
        // 获取sku蕾丝面积与长度
        Long laceAreaAttributeNameId = produceDataBaseService.getLaceAreaAttributeNameId();
        Long lengthAttributeNameId = produceDataBaseService.getLengthAttributeNameId();
        List<Long> attributeNameIds = Arrays.asList(laceAreaAttributeNameId, lengthAttributeNameId);
        CommonPageResult.PageInfo<String> pageResult = produceDataAttrDao.selectProduceDataAttrPage(PageDTO.of(1, LOOP_SIZE), attributeNameIds);
        final List<String> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        for (int i = 1; i <= pageResult.getTotalCount() / LOOP_SIZE + 1; i++) {
            List<CostOfGoodsPo> updateCostOfGoodsPoList = new ArrayList<>();
            List<CostOfGoodsPo> insertCostOfGoodsPoList = new ArrayList<>();
            final CommonPageResult.PageInfo<String> skuPageInfo = produceDataAttrDao.selectProduceDataAttrPage(PageDTO.of(i, LOOP_SIZE), attributeNameIds);
            final List<String> skuList = skuPageInfo.getRecords();
            // 获取当前月份维度成本数据
            final String currentMonthStr = ScmTimeUtil.getCurrentMonthString();
            // 查询有月初加权单价的sku
            List<CostOfGoodsPo> costOfGoodsPoList = costOfGoodsDao.getWeightingPriceBySkuList(skuList,
                    currentMonthStr,
                    CostTimeType.MONTH,
                    PolymerizeType.MULTIPLE_WAREHOUSE,
                    PolymerizeWarehouse.NON_WH,
                    BigDecimal.ZERO);
            List<String> existWeightingPriceSkuList = costOfGoodsPoList.stream()
                    .map(CostOfGoodsPo::getSku)
                    .distinct()
                    .collect(Collectors.toList());

            // 使用流操作过滤出在 skuList 中不存在的 SKU 组成一个初始化的sku列表
            List<String> nonExistentSkus = skuList.stream()
                    .filter(sku -> !existWeightingPriceSkuList.contains(sku))
                    .collect(Collectors.toList());

            // 获取蕾丝面积和裆长尺寸生产属性对应sku
            Map<String, List<String>> attrMatchSkuMap = produceDataBaseService.getAttrMatchSkuListMap(nonExistentSkus);

            // 查询SKU和月份的初始化的PO
            Map<String, List<CostOfGoodsPo>> costOfGoodsPoMap = costOfGoodsDao.getListBySkuListAndCostTime(nonExistentSkus,
                            currentMonthStr,
                            CostTimeType.MONTH)
                    .stream()
                    .collect(Collectors.groupingBy(CostOfGoodsPo::getSku));

            final String currentMonthString = ScmTimeUtil.getCurrentMonthString();

            for (String existentSku : nonExistentSkus) {
                List<CostOfGoodsPo> costOfGoodsPoInits = Optional.ofNullable(costOfGoodsPoMap.get(existentSku))
                        .orElse(new ArrayList<>());

                for (CostOfGoodsPo costOfGoodsPo : costOfGoodsPoInits) {
                    String sku = costOfGoodsPo.getSku();
                    if (StringUtils.isBlank(sku)) {
                        log.info("初始化sku:空值，po={}", JacksonUtil.parse2Str(costOfGoodsPo));
                        continue;
                    }
                    List<String> allSkuList = attrMatchSkuMap.get(sku);
                    if (CollectionUtils.isEmpty(allSkuList)) {
                        log.info("初始化sku:{}通过生产属性值匹配不到对应sku", sku);
                        continue;
                    }

                    // 查询蕾丝面积和裆长尺寸的sku对应加权单价
                    List<CostOfGoodsPo> costOfGoodsPoAttrList = costOfGoodsDao.getWeightingPriceBySkuList(allSkuList,
                            currentMonthStr,
                            CostTimeType.MONTH,
                            PolymerizeType.MULTIPLE_WAREHOUSE,
                            PolymerizeWarehouse.NON_WH,
                            BigDecimal.ZERO);


                    List<CostOfGoodsPo> costOfGoodsPoAverage = costOfGoodsPoAttrList.stream()
                            .filter(costOfGoodsPoAttr -> !sku.equals(costOfGoodsPoAttr.getSku()))
                            .filter(costOfGoodsPoAttr -> costOfGoodsPoAttr.getWeightingPrice() != null && BigDecimal.ZERO.compareTo(costOfGoodsPoAttr.getWeightingPrice()) < 0)
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(costOfGoodsPoAverage)) {
                        log.info("初始化sku:{}通过查询参考sku的月初加权价为空，参考sku的po={}", sku, JacksonUtil.parse2Str(costOfGoodsPoAttrList));
                        continue;
                    }
                    BigDecimal weightingPrice = costOfGoodsPoAverage.stream()
                            .map(CostOfGoodsPo::getWeightingPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .divide(new BigDecimal(costOfGoodsPoAverage.size()), 2, RoundingMode.DOWN);

                    if (BigDecimal.ZERO.compareTo(weightingPrice) == 0) {
                        log.info("初始化sku:{}通过查询参考sku的月初加权价为0，参考sku的po={}", sku, JacksonUtil.parse2Str(costOfGoodsPoAverage));
                        continue;
                    }

                    CostOfGoodsPo updateCostOfGoodsPo = new CostOfGoodsPo();
                    updateCostOfGoodsPo.setCostOfGoodsId(costOfGoodsPo.getCostOfGoodsId());
                    updateCostOfGoodsPo.setWeightingPrice(weightingPrice);
                    updateCostOfGoodsPoList.add(updateCostOfGoodsPo);
                    log.info("初始化sku:{}更新成功月初加权价{}，po={}", sku, weightingPrice, JacksonUtil.parse2Str(costOfGoodsPo));
                }
                if (CollectionUtils.isEmpty(costOfGoodsPoInits)) {
                    this.insertCostOfGoodsPo(existentSku, attrMatchSkuMap, currentMonthStr, currentMonthString, insertCostOfGoodsPoList);
                }
            }

            if (CollectionUtils.isNotEmpty(insertCostOfGoodsPoList)) {
                costOfGoodsDao.insertBatch(insertCostOfGoodsPoList);
            }
            costOfGoodsDao.updateBatchById(updateCostOfGoodsPoList);


        }


    }

    private void insertCostOfGoodsPo(String existentSku, Map<String, List<String>> attrMatchSkuMap,
                                     String currentMonthStr, String currentMonthString, List<CostOfGoodsPo> insertCostOfGoodsPoList) {
        List<String> allSkuList = attrMatchSkuMap.get(existentSku);
        if (CollectionUtils.isEmpty(allSkuList)) {
            log.info("初始化sku:{}通过生产属性值匹配不到对应sku", existentSku);
            return;
        }

        // 查询蕾丝面积和裆长尺寸的sku对应加权单价
        List<CostOfGoodsPo> costOfGoodsPoAttrList = costOfGoodsDao.getWeightingPriceBySkuList(allSkuList,
                currentMonthStr,
                CostTimeType.MONTH,
                PolymerizeType.MULTIPLE_WAREHOUSE,
                PolymerizeWarehouse.NON_WH,
                BigDecimal.ZERO);

        List<CostOfGoodsPo> costOfGoodsPoAverage = costOfGoodsPoAttrList.stream()
                .filter(costOfGoodsPoAttr -> !existentSku.equals(costOfGoodsPoAttr.getSku()))
                .filter(costOfGoodsPoAttr -> costOfGoodsPoAttr.getWeightingPrice() != null && BigDecimal.ZERO.compareTo(costOfGoodsPoAttr.getWeightingPrice()) < 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(costOfGoodsPoAverage)) {
            log.info("初始化sku:{}通过查询参考sku的月初加权价为空，参考sku的po={}", existentSku, JacksonUtil.parse2Str(costOfGoodsPoAttrList));
            return;
        }
        BigDecimal weightingPrice = costOfGoodsPoAverage.stream()
                .map(CostOfGoodsPo::getWeightingPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(costOfGoodsPoAverage.size()), 2, RoundingMode.DOWN);

        if (BigDecimal.ZERO.compareTo(weightingPrice) == 0) {
            log.info("初始化sku:{}通过查询参考sku的月初加权价为0，参考sku的po={}", existentSku, JacksonUtil.parse2Str(costOfGoodsPoAverage));
            return;
        }

        // 若sku不存在，则初始化
        final List<WarehouseVo> warehouseVoList = wmsRemoteService.getAllWarehouse();
        // 单仓维度
        final List<CostOfGoodsPo> singleWarehouseCostPoList = warehouseVoList.stream()
                .map(warehouseVo -> {
                    final CostOfGoodsPo costOfGoodsPo = new CostOfGoodsPo();
                    costOfGoodsPo.setSku(existentSku);
                    costOfGoodsPo.setWarehouseCode(warehouseVo.getWarehouseCode());
                    costOfGoodsPo.setWarehouseName(warehouseVo.getWarehouseName());
                    costOfGoodsPo.setWarehouseTypes(warehouseVo.getWarehouseType());
                    costOfGoodsPo.setPolymerizeType(PolymerizeType.SINGLE_WAREHOUSE);
                    costOfGoodsPo.setCostTimeType(CostTimeType.MONTH);
                    costOfGoodsPo.setCostTime(currentMonthString);
                    costOfGoodsPo.setWeightingPrice(weightingPrice);
                    return costOfGoodsPo;
                })
                .collect(Collectors.toList());
        insertCostOfGoodsPoList.addAll(singleWarehouseCostPoList);
        // 多仓维度
        for (PolymerizeWarehouse polymerizeWarehouse : PolymerizeWarehouse.values()) {
            final CostOfGoodsPo costOfGoodsPo = new CostOfGoodsPo();
            costOfGoodsPo.setSku(existentSku);
            costOfGoodsPo.setPolymerizeType(PolymerizeType.MULTIPLE_WAREHOUSE);
            costOfGoodsPo.setPolymerizeWarehouse(polymerizeWarehouse);
            costOfGoodsPo.setCostTimeType(CostTimeType.MONTH);
            costOfGoodsPo.setCostTime(currentMonthString);
            costOfGoodsPo.setWeightingPrice(weightingPrice);
            insertCostOfGoodsPoList.add(costOfGoodsPo);
        }

    }

}
