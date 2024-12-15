package com.hete.supply.scm.server.scm.cost.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.common.util.ScmWarehouseUtil;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.cost.dao.CostOfGoodsDao;
import com.hete.supply.scm.server.scm.cost.entity.bo.GoodsCostBo;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostVo;
import com.hete.supply.wms.api.interna.entity.vo.BatchCodeInventoryPageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/2/29 16:56
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CostInitBizService {
    private final WmsRemoteService wmsRemoteService;
    private final CostOfGoodsDao costOfGoodsDao;
    /**
     * 最大循环页数
     */
    private static final Integer MAX_LOOP_PAGE = 10000;

    /**
     * 每页循环个数
     */
    private static final Integer PAGE_SIZE = 200;

    @Transactional(rollbackFor = Exception.class)
    public void costYestDataInitJob() {
        final String yesterdayDateString = ScmTimeUtil.getBeforeDateString(1);
        // 若已初始化了，则不再执行
        final Long existCnt = costOfGoodsDao.getCntByTimeStr(yesterdayDateString);
        if (existCnt > 0) {
            log.error("重复执行初始化{}的sku成本数据", yesterdayDateString);
            return;
        }

        for (int i = 1; i < MAX_LOOP_PAGE; i++) {
            // 从wms获取价格
            final List<BatchCodeInventoryPageVo> inventoryVoList = wmsRemoteService.getYestInventoryData(i);
            if (CollectionUtils.isEmpty(inventoryVoList)) {
                return;
            }

            // 循环获取sku对应的价格，把等于0的价格，在数据库中查一遍前一天的价格。
            final List<GoodsCostVo> goodsCostVoList = new ArrayList<>();
            for (BatchCodeInventoryPageVo inventoryVo : inventoryVoList) {
                final List<BatchCodeInventoryPageVo.BatchCodeVo> batchCodeVoList = inventoryVo.getBatchCodeVoList();
                if (CollectionUtils.isEmpty(batchCodeVoList)) {
                    continue;
                }
                final BigDecimal inventoryPrice = batchCodeVoList.stream()
                        .filter(batchCodeVo -> batchCodeVo.getBatchPrice() != null)
                        .map(batchCodeVo -> batchCodeVo.getBatchPrice().multiply(new BigDecimal(batchCodeVo.getTotalAmount())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // 不等于0不记录
                if (inventoryPrice.compareTo(BigDecimal.ZERO) != 0) {
                    continue;
                }
                final GoodsCostVo goodsCostVo = new GoodsCostVo();
                goodsCostVo.setSku(inventoryVo.getSkuCode());
                goodsCostVo.setWarehouseCode(inventoryVo.getWarehouseCode());
                goodsCostVoList.add(goodsCostVo);
            }
            final String beforeYest = ScmTimeUtil.getBeforeDateString(2);
            final List<GoodsCostBo> relatedCostDataList = costOfGoodsDao.getRelatedCostData(goodsCostVoList, beforeYest,
                    CostTimeType.DAY, PolymerizeType.SINGLE_WAREHOUSE);
            // 按sku+warehouseCode维度分组
            final Map<String, GoodsCostBo> skuWarehouseCostMap = relatedCostDataList.stream()
                    .collect(Collectors.toMap(data -> data.getSku() + data.getWarehouseCode(),
                            Function.identity()));

            for (BatchCodeInventoryPageVo inventoryVo : inventoryVoList) {
                final List<BatchCodeInventoryPageVo.BatchCodeVo> batchCodeVoList = inventoryVo.getBatchCodeVoList();
                if (CollectionUtils.isEmpty(batchCodeVoList)) {
                    continue;
                }
                final int inventory = batchCodeVoList.stream()
                        .filter(batchCodeVo -> null != batchCodeVo.getBatchPrice())
                        .filter(batchCodeVo -> BigDecimal.ZERO.compareTo(batchCodeVo.getBatchPrice()) != 0)
                        .mapToInt(BatchCodeInventoryPageVo.BatchCodeVo::getTotalAmount)
                        .sum();
                final BigDecimal inventoryPrice = batchCodeVoList.stream()
                        .filter(batchCodeVo -> batchCodeVo.getBatchPrice() != null)
                        .map(batchCodeVo -> batchCodeVo.getBatchPrice().multiply(new BigDecimal(batchCodeVo.getTotalAmount())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                final long skuExistCnt = costOfGoodsDao.getCountByCondition(yesterdayDateString,
                        inventoryVo.getWarehouseCode(), PolymerizeType.SINGLE_WAREHOUSE, inventoryVo.getSkuCode());
                if (skuExistCnt > 0) {
                    log.warn("sku:{}存在数据", inventoryVo.getSkuCode());
                    continue;
                }
                // 单仓维度
                final CostOfGoodsPo costOfGoodsPo = new CostOfGoodsPo();
                costOfGoodsPo.setSku(inventoryVo.getSkuCode());
                costOfGoodsPo.setWarehouseCode(inventoryVo.getWarehouseCode());
                costOfGoodsPo.setWarehouseName(inventoryVo.getWarehouseName());
                costOfGoodsPo.setWarehouseTypes(inventoryVo.getWarehouseType());
                costOfGoodsPo.setPolymerizeType(PolymerizeType.SINGLE_WAREHOUSE);
                costOfGoodsPo.setCostTimeType(CostTimeType.DAY);
                costOfGoodsPo.setCostTime(yesterdayDateString);
                costOfGoodsPo.setInventory(inventory);
                // 若sku的价格为0或库存为0，则继承前一天的价格
                if (inventoryPrice.compareTo(BigDecimal.ZERO) == 0 || inventory == 0) {
                    final GoodsCostBo goodsCostBo = skuWarehouseCostMap.get(inventoryVo.getSkuCode() + inventoryVo.getWarehouseCode());
                    if (null != goodsCostBo) {
                        costOfGoodsPo.setWeightingPrice(goodsCostBo.getWeightingPrice());
                    }
                } else {
                    final BigDecimal weightingPrice = inventoryPrice.divide(new BigDecimal(inventory), 2,
                            RoundingMode.DOWN);
                    costOfGoodsPo.setInventoryPrice(inventoryPrice);
                    costOfGoodsPo.setWeightingPrice(weightingPrice);
                }
                costOfGoodsDao.insert(costOfGoodsPo);

                // 多仓维度
                final List<PolymerizeWarehouse> polymerizeWarehouseList = ScmWarehouseUtil.getPolymerizeWarehouseByTypes(inventoryVo.getWarehouseType(),
                        inventoryVo.getWarehouseCode(),
                        inventoryVo.getWarehouseName());
                if (CollectionUtils.isNotEmpty(polymerizeWarehouseList)) {
                    // 查找多仓维度的数据是否存在
                    polymerizeWarehouseList.forEach(polymerizeWarehouse -> {
                        CostOfGoodsPo existCostOfGoodsPo = costOfGoodsDao.getSkuCostBySkuAndMulWarehouse(yesterdayDateString, CostTimeType.DAY,
                                PolymerizeType.MULTIPLE_WAREHOUSE, polymerizeWarehouse, inventoryVo.getSkuCode());

                        if (null != existCostOfGoodsPo) {
                            existCostOfGoodsPo.setInventory(existCostOfGoodsPo.getInventory() + inventory);
                            existCostOfGoodsPo.setInventoryPrice(existCostOfGoodsPo.getInventoryPrice().add(inventoryPrice));
                            if (existCostOfGoodsPo.getInventory() == 0) {
                                existCostOfGoodsPo.setWeightingPrice(BigDecimal.ZERO);
                            } else {
                                final BigDecimal newWeightingPrice = existCostOfGoodsPo.getInventoryPrice()
                                        .divide(new BigDecimal(existCostOfGoodsPo.getInventory()), 2, RoundingMode.HALF_UP);
                                existCostOfGoodsPo.setWeightingPrice(newWeightingPrice);
                            }

                            costOfGoodsDao.updateByIdVersion(existCostOfGoodsPo);
                        } else {
                            existCostOfGoodsPo = new CostOfGoodsPo();
                            existCostOfGoodsPo.setSku(inventoryVo.getSkuCode());
                            existCostOfGoodsPo.setPolymerizeType(PolymerizeType.MULTIPLE_WAREHOUSE);
                            existCostOfGoodsPo.setPolymerizeWarehouse(polymerizeWarehouse);
                            existCostOfGoodsPo.setCostTimeType(CostTimeType.DAY);
                            existCostOfGoodsPo.setCostTime(yesterdayDateString);
                            existCostOfGoodsPo.setInventory(inventory);
                            existCostOfGoodsPo.setInventoryPrice(inventoryPrice);
                            if (inventory == 0) {
                                existCostOfGoodsPo.setWeightingPrice(BigDecimal.ZERO);
                            } else {
                                final BigDecimal weightingPrice = existCostOfGoodsPo.getInventoryPrice()
                                        .divide(new BigDecimal(existCostOfGoodsPo.getInventory()), 2, RoundingMode.HALF_UP);
                                existCostOfGoodsPo.setWeightingPrice(weightingPrice);
                            }
                            costOfGoodsDao.insert(existCostOfGoodsPo);
                        }
                    });
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void costMonthDataInitJob() {
        final String currentMonthString = ScmTimeUtil.getCurrentMonthString();

        // 若已初始化了，则不再执行
        final Long existCnt = costOfGoodsDao.getCntByTimeStr(currentMonthString);
        if (existCnt > 0) {
            log.error("重复执行初始化{}的sku成本数据", currentMonthString);
            return;
        }
        // 月初成本价优先取上个月的最后一天价格
        final String lastDayOfPreviousMonth = ScmTimeUtil.getLastDayOfPreviousMonth();

        // 获取上个月的时间字符串
        final LocalDateTime lastMonth = ScmTimeUtil.getAfterMonthDate(-1);
        final String lastMonthStr = ScmTimeUtil.getMonthString(lastMonth);


        for (int i = 1; i < MAX_LOOP_PAGE; i++) {
            final IPage<CostOfGoodsPo> costOfGoodsPoPage = costOfGoodsDao.getYestCostByCostTime(PageDTO.of(i, PAGE_SIZE), lastDayOfPreviousMonth);
            final List<CostOfGoodsPo> records = costOfGoodsPoPage.getRecords();
            if (CollectionUtils.isEmpty(records)) {
                break;
            }

            // 筛选出加权价为0的数据
            final List<CostOfGoodsPo> zeroWeightPricePoList = records.stream()
                    .filter(record -> BigDecimal.ZERO.compareTo(record.getWeightingPrice()) == 0)
                    .collect(Collectors.toList());
            final List<CostOfGoodsPo> lastMonthCostPoList = costOfGoodsDao.getMonthCostBySkuAndWarehouse(zeroWeightPricePoList, lastMonthStr);
            final Map<String, BigDecimal> skuWarehouseCostPoMap = lastMonthCostPoList.stream()
                    .collect(Collectors.toMap(po -> po.getSku() + po.getWarehouseCode() + po.getPolymerizeWarehouse(),
                            CostOfGoodsPo::getWeightingPrice));


            final List<CostOfGoodsPo> costOfGoodsPoList = records.stream().map(record -> {
                final CostOfGoodsPo costOfGoodsPo = new CostOfGoodsPo();
                costOfGoodsPo.setSku(record.getSku());
                costOfGoodsPo.setWarehouseCode(record.getWarehouseCode());
                costOfGoodsPo.setWarehouseName(record.getWarehouseName());
                costOfGoodsPo.setWarehouseTypes(record.getWarehouseTypes());
                costOfGoodsPo.setPolymerizeType(record.getPolymerizeType());
                costOfGoodsPo.setPolymerizeWarehouse(record.getPolymerizeWarehouse());
                costOfGoodsPo.setCostTimeType(CostTimeType.MONTH);
                costOfGoodsPo.setCostTime(currentMonthString);
                costOfGoodsPo.setInventory(record.getInventory());
                costOfGoodsPo.setInventoryPrice(record.getInventoryPrice());
                if (BigDecimal.ZERO.compareTo(record.getWeightingPrice()) == 0) {
                    costOfGoodsPo.setWeightingPrice(skuWarehouseCostPoMap.getOrDefault(
                            record.getSku() + record.getWarehouseCode() + record.getPolymerizeWarehouse(), BigDecimal.ZERO));
                } else {
                    costOfGoodsPo.setWeightingPrice(record.getWeightingPrice());
                }
                return costOfGoodsPo;
            }).collect(Collectors.toList());

            costOfGoodsDao.insertBatch(costOfGoodsPoList);
        }

        // 若sku不存在的，则取上个月的加权成本价
        final List<CostOfGoodsPo> monthCostNotExistPoList = costOfGoodsDao.getMonthCostNotExist(lastMonthStr, currentMonthString);

        // 给这些po的时间赋值上当月后保存
        monthCostNotExistPoList.forEach(po -> po.setCostTime(currentMonthString));
        costOfGoodsDao.insertBatch(monthCostNotExistPoList);
    }
}
