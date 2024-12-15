package com.hete.supply.scm.server.scm.cost.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.CostSkuDto;
import com.hete.supply.scm.api.scm.entity.dto.CostSkuItemDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsCostDto;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.scm.api.scm.entity.vo.CostSkuItemVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.CostImportDto;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceDao;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.cost.dao.CostOfGoodsDao;
import com.hete.supply.scm.server.scm.cost.entity.bo.SkuAndWarehouseBo;
import com.hete.supply.scm.server.scm.cost.entity.dto.SkuPriceUpdateDto;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostVo;
import com.hete.supply.scm.server.scm.cost.handler.SkuPriceUpdateHandler;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.dao.ProduceDataDao;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import com.hete.supply.scm.server.scm.nacosconfig.SkuCategoryProp;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author weiwenxin
 * @date 2024/2/27 15:48
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CostBaseService {
    private final CostOfGoodsDao costOfGoodsDao;
    private final PlmRemoteService plmRemoteService;
    private final PlmSkuDao plmSkuDao;
    private final WmsRemoteService wmsRemoteService;
    private final SkuCategoryProp skuCategoryProp;
    private final ProduceDataDao produceDataDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final GoodsPriceDao goodsPriceDao;

    /**
     * 根据sku与仓库获取月初加权价
     * 单仓维度时，传PolymerizeType.SINGLE_WAREHOUSE与warehouseCode
     * 多仓维度时，传PolymerizeType.MULTIPLE_WAREHOUSE与PolymerizeWarehouse
     */
    public CostOfGoodsPo getMoWeightingPrice(@Valid @NotNull SkuAndWarehouseBo skuAndWarehouseBo) {
        final String currentMonthString = ScmTimeUtil.getCurrentMonthString();
        return costOfGoodsDao.getMoDataBySkuAndWarehouse(skuAndWarehouseBo.getSku(), skuAndWarehouseBo.getWarehouseCode(),
                skuAndWarehouseBo.getPolymerizeType(), skuAndWarehouseBo.getPolymerizeWarehouse(), currentMonthString);
    }

    /**
     * 提供列表和导出公用查询
     *
     * @param dto:
     * @return PageInfo<GoodsCostVo>
     * @author ChenWenLong
     * @date 2024/3/5 13:43
     */
    public CommonPageResult.PageInfo<GoodsCostVo> searchGoodsOfCostPageInfo(GoodsCostDto dto) {
        // 当时间字段入参为空时，按照传参的枚举值，初始化当日或者当月的时间
        if (StringUtils.isBlank(dto.getCostTime())) {
            if (CostTimeType.DAY.equals(dto.getCostTimeType())) {
                dto.setCostTime(ScmTimeUtil.getBeforeDateString(1));
            } else if (CostTimeType.MONTH.equals(dto.getCostTimeType())) {
                dto.setCostTime(ScmTimeUtil.getCurrentMonthString());
            }
        }
        // 产品名称
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return new CommonPageResult.PageInfo<>();
            }
            final String sku = skuListByEncode.get(0);
            if (StringUtils.isBlank(dto.getSku())) {
                dto.setSku(sku);
            } else {
                if (!sku.equals(dto.getSku())) {
                    return new CommonPageResult.PageInfo<>();
                }
            }
        }

        Page<Void> page = PageDTO.of(dto.getPageNo(), dto.getPageSize());
        // 分页是否进行count查询
        if (null != dto.getSearchCount() && Boolean.FALSE.equals(dto.getSearchCount())) {
            page.setSearchCount(false);
        }
        final CommonPageResult.PageInfo<GoodsCostVo> pageInfo = costOfGoodsDao.searchGoodsOfCost(page, dto);
        final List<GoodsCostVo> records = pageInfo.getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        final List<String> skuList = records.stream()
                .map(GoodsCostVo::getSku)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        records.forEach(record -> {
            if (null != record.getPolymerizeWarehouse()) {
                record.setWarehouseName(record.getPolymerizeWarehouse().getRemark());
            }
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            // 日维度的时间往后推一天
            if (CostTimeType.DAY.equals(record.getCostTimeType())) {
                final String costTime = ScmTimeUtil.getAfterDateString(record.getCostTime(), 1);
                record.setCostTime(costTime);
            }
        });

        return pageInfo;
    }

    /**
     * 校验月度入参与昨日入参不能同时存在
     *
     * @param dto
     */
    public void validDto(GoodsCostDto dto) {
        final boolean isValid = (dto.getMoInventoryMax() == null || dto.getMoInventoryMin() == null
                || dto.getMoInventoryPriceMax() == null || dto.getMoInventoryPriceMin() == null
                || dto.getMoWeightingPriceMax() == null || dto.getMoWeightingPriceMin() == null)
                && (dto.getYestInventoryMax() == null || dto.getYestInventoryMin() == null
                || dto.getYestInventoryPriceMax() == null || dto.getYestInventoryPriceMin() == null
                || dto.getYestWeightingPriceMax() == null || dto.getYestWeightingPriceMin() == null);
        if (!isValid) {
            throw new ParamIllegalException("昨日的搜索条件与月初搜索条件不能同时搜索，请修改查询条件后再搜索");
        }
    }

    public void saveChangeMoData(CostImportDto dto) {
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        if (plmSkuPo == null) {
            throw new ParamIllegalException("sku不存在");
        }
        // 获取当月时间字符串
        final String currentMonthString = ScmTimeUtil.getCurrentMonthString();
        final List<CostOfGoodsPo> costOfGoodsPoList = costOfGoodsDao.getMoDataBySkuAndTime(dto.getSku(),
                currentMonthString);
        // 若sku不存在，则初始化
        if (CollectionUtils.isEmpty(costOfGoodsPoList)) {
            final List<WarehouseVo> warehouseVoList = wmsRemoteService.getAllWarehouse();
            // 单仓维度
            final List<CostOfGoodsPo> singleWarehouseCostPoList = warehouseVoList.stream()
                    .map(warehouseVo -> {
                        final CostOfGoodsPo costOfGoodsPo = new CostOfGoodsPo();
                        costOfGoodsPo.setSku(dto.getSku());
                        costOfGoodsPo.setWarehouseCode(warehouseVo.getWarehouseCode());
                        costOfGoodsPo.setWarehouseName(warehouseVo.getWarehouseName());
                        costOfGoodsPo.setWarehouseTypes(warehouseVo.getWarehouseType());
                        costOfGoodsPo.setPolymerizeType(PolymerizeType.SINGLE_WAREHOUSE);
                        costOfGoodsPo.setCostTimeType(CostTimeType.MONTH);
                        costOfGoodsPo.setCostTime(currentMonthString);
                        costOfGoodsPo.setWeightingPrice(dto.getMoWeightingPriceMin());
                        return costOfGoodsPo;
                    }).collect(Collectors.toList());
            costOfGoodsPoList.addAll(singleWarehouseCostPoList);
            // 多仓维度
            for (PolymerizeWarehouse polymerizeWarehouse : PolymerizeWarehouse.values()) {
                final CostOfGoodsPo costOfGoodsPo = new CostOfGoodsPo();
                costOfGoodsPo.setSku(dto.getSku());
                costOfGoodsPo.setPolymerizeType(PolymerizeType.MULTIPLE_WAREHOUSE);
                costOfGoodsPo.setPolymerizeWarehouse(polymerizeWarehouse);
                costOfGoodsPo.setCostTimeType(CostTimeType.MONTH);
                costOfGoodsPo.setCostTime(currentMonthString);
                costOfGoodsPo.setWeightingPrice(dto.getMoWeightingPriceMin());
                costOfGoodsPoList.add(costOfGoodsPo);
            }
        }

        costOfGoodsPoList.forEach(po -> po.setWeightingPrice(dto.getMoWeightingPriceMin()));
        costOfGoodsDao.insertOrUpdateBatch(costOfGoodsPoList);

        // 推送给wms
        final SkuPriceUpdateDto.SkuPriceDto skuPriceDto = new SkuPriceUpdateDto.SkuPriceDto();
        skuPriceDto.setSkuCode(dto.getSku());
        skuPriceDto.setPrice(dto.getMoWeightingPriceMin());
        final SkuPriceUpdateDto skuPriceUpdateDto = new SkuPriceUpdateDto();
        skuPriceUpdateDto.setSkuCodePriceList(Collections.singletonList(skuPriceDto));
        consistencySendMqService.execSendMq(SkuPriceUpdateHandler.class, skuPriceUpdateDto);
    }

    /**
     * 根据sku/sku+仓库获取sku昨日成本价
     *
     * @param dto
     * @return
     */
    public List<CostSkuItemVo> getCostBySku(CostSkuDto dto) {
        final List<CostSkuItemDto> costSkuItemList = dto.getCostSkuItemList();

        // 筛选 （同时存在sku & 仓库编码） / （只存在sku）
        final List<CostSkuItemDto> existSkuAndWareCodeDtoList = costSkuItemList.stream()
                .filter(itemDto -> StringUtils.isNotBlank(itemDto.getWarehouseCode()))
                .collect(Collectors.toList());
        final List<String> onlyExistSkuList = costSkuItemList.stream()
                .filter(itemDto -> StringUtils.isBlank(itemDto.getWarehouseCode()))
                .map(CostSkuItemDto::getSku)
                .collect(Collectors.toList());

        // 获取当前月份维度成本数据
        final String currentMonthStr = ScmTimeUtil.getCurrentMonthString();
        final Map<String, CostOfGoodsPo> curMonthlyCostDataMap
                = getWarehouseAndSkuCost(currentMonthStr, CostTimeType.MONTH, existSkuAndWareCodeDtoList, onlyExistSkuList);

        // 获取昨日维度成本数据
        final String beforeOneDateStr = ScmTimeUtil.getBeforeDateString(1);
        final Map<String, CostOfGoodsPo> beforeOneDateCostDataMap
                = getWarehouseAndSkuCost(beforeOneDateStr, CostTimeType.DAY, existSkuAndWareCodeDtoList, onlyExistSkuList);

        // 获取所有sku生产信息维护的采购成本价
        final List<String> queryPurchasePriceSkus = costSkuItemList.stream()
                .map(CostSkuItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        final List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(queryPurchasePriceSkus);
        final Map<String, BigDecimal> skuGoodPurchasePriceMap = produceDataPoList.stream()
                .collect(Collectors.toMap(ProduceDataPo::getSku, ProduceDataPo::getGoodsPurchasePrice));
        final List<String> skuList = costSkuItemList.stream()
                .map(CostSkuItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, List<Long>> skuCategoryIdListMap = plmRemoteService.queryCategoryIds(skuList);
        // 获取sku对应的渠道价格
        final List<GoodsPricePo> goodsPricePoList = goodsPriceDao.getListBySkuList(skuList);
        final Map<String, BigDecimal> skuChannelPriceMap = goodsPricePoList.stream()
                .collect(Collectors.toMap(GoodsPricePo::getSku, GoodsPricePo::getChannelPrice,
                        (item1, item2) -> item1.compareTo(item2) > 0 ? item1 : item2));

        return dto.getCostSkuItemList().stream()
                .map(costSkuItemDto -> {
                    final CostSkuItemVo costSkuItemVo = new CostSkuItemVo();
                    costSkuItemVo.setSku(costSkuItemDto.getSku());
                    costSkuItemVo.setWarehouseCode(costSkuItemDto.getWarehouseCode());

                    if (null == costSkuItemDto.getWarehouseCode()) {
                        costSkuItemDto.setWarehouseCode("");
                    }

                    final CostOfGoodsPo currentMonthCostPo = curMonthlyCostDataMap.get(costSkuItemDto.getWarehouseCode() + costSkuItemDto.getSku());
                    final CostOfGoodsPo beforeOneDateCostPo = beforeOneDateCostDataMap.get(costSkuItemDto.getWarehouseCode() + costSkuItemDto.getSku());
                    final BigDecimal skuGoodPurchasePrice = skuGoodPurchasePriceMap.getOrDefault(costSkuItemDto.getSku(), BigDecimal.ZERO);
                    final BigDecimal skuChannelPrice = skuChannelPriceMap.getOrDefault(costSkuItemDto.getSku(), BigDecimal.ZERO);
                    if (null != currentMonthCostPo && BigDecimal.ZERO.compareTo(currentMonthCostPo.getWeightingPrice()) != 0) {
                        costSkuItemVo.setInventoryPrice(currentMonthCostPo.getWeightingPrice());
                        costSkuItemVo.setUpdateTime(currentMonthCostPo.getUpdateTime());
                    } else if (null != beforeOneDateCostPo && BigDecimal.ZERO.compareTo(beforeOneDateCostPo.getWeightingPrice()) != 0) {
                        costSkuItemVo.setInventoryPrice(beforeOneDateCostPo.getWeightingPrice());
                        costSkuItemVo.setUpdateTime(beforeOneDateCostPo.getUpdateTime());
                    } else if (BigDecimal.ZERO.compareTo(skuGoodPurchasePrice) != 0) {
                        costSkuItemVo.setInventoryPrice(skuGoodPurchasePrice);
                    } else if (BigDecimal.ZERO.compareTo(skuChannelPrice) != 0) {
                        costSkuItemVo.setInventoryPrice(skuChannelPrice);
                    } else {
                        final List<Long> categoryIdList = skuCategoryIdListMap.get(costSkuItemDto.getSku());
                        final Long wigId = skuCategoryProp.getWigId();
                        final Long syntheticHairId = skuCategoryProp.getSyntheticHairId();
                        final Long wigRawId = skuCategoryProp.getWigRawId();
                        // sku对应的品类不为空，且属于假发、化纤发、假发原料类别的，输出报错日志
                        if (CollectionUtils.isNotEmpty(categoryIdList) && (categoryIdList.contains(wigId)
                                || categoryIdList.contains(syntheticHairId)
                                || categoryIdList.contains(wigRawId))) {
                            log.warn("sku:{}属于假发、化纤发、假发原料类别之一，且价格为0，请维护该sku价格", costSkuItemDto.getSku());
                        }
                        costSkuItemVo.setInventoryPrice(BigDecimal.ZERO);
                    }

                    return costSkuItemVo;
                }).collect(Collectors.toList());
    }

    /**
     * 根据提供的参数获取仓库和SKU的成本信息。
     *
     * @param dateStr                日期字符串。
     * @param costTimeType           成本时间类型。
     * @param singleWarehouseDtoList 带有非空仓库代码的 CostSkuItemDto 对象列表。
     * @param skuList                带有空白仓库代码的 SKU 列表。
     * @return 包含仓库和SKU成本信息的映射。
     */
    public Map<String, CostOfGoodsPo> getWarehouseAndSkuCost(String dateStr,
                                                             CostTimeType costTimeType,
                                                             List<CostSkuItemDto> singleWarehouseDtoList,
                                                             List<String> skuList) {
        // 获取具有特定仓库的SKU的成本信息
        final List<CostOfGoodsPo> costOfGoodsPoList = costOfGoodsDao.getSkuCostBySkuAndWarehouse(
                dateStr,
                costTimeType,
                PolymerizeType.SINGLE_WAREHOUSE,
                singleWarehouseDtoList);

        // 获取没有特定仓库的SKU的成本信息
        final List<CostOfGoodsPo> mulWarehouseCostPoList = costOfGoodsDao.getSkuCostBySkuAndMulWarehouse(
                dateStr,
                costTimeType,
                PolymerizeType.MULTIPLE_WAREHOUSE,
                PolymerizeWarehouse.NON_WH,
                skuList);

        // 合并两次查询的结果
        final List<CostOfGoodsPo> resultList
                = Stream.concat(costOfGoodsPoList.stream(), mulWarehouseCostPoList.stream())
                .collect(Collectors.toList());

        // 创建一个以仓库代码和SKU为键，对应的CostOfGoodsPo对象为值的映射
        return resultList.stream()
                .collect(Collectors.toMap(po -> po.getWarehouseCode() + po.getSku(), Function.identity()));
    }

}
