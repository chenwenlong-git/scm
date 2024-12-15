package com.hete.supply.scm.server.scm.cost.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.CostSkuItemDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsCostDto;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.scm.api.scm.entity.vo.GoodsOfCostExportVo;
import com.hete.supply.scm.server.scm.cost.entity.bo.GoodsCostBo;
import com.hete.supply.scm.server.scm.cost.entity.bo.SkuAndWarehouseBatchBo;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostLogItemVo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品成本表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-20
 */
@Component
@Validated
public class CostOfGoodsDao extends BaseDao<CostOfGoodsMapper, CostOfGoodsPo> {

    public CommonPageResult.PageInfo<GoodsCostVo> searchGoodsOfCost(Page<Void> page, GoodsCostDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.searchGoodsOfCost(page, dto));

    }

    /**
     * 获取关联数据
     *
     * @param goodsCostVoList
     * @param timeStr
     * @param costTimeType
     * @param polymerizeType
     * @return
     */
    public List<GoodsCostBo> getRelatedCostData(List<GoodsCostVo> goodsCostVoList, String timeStr,
                                                CostTimeType costTimeType,
                                                PolymerizeType polymerizeType) {
        if (CollectionUtils.isEmpty(goodsCostVoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getRelatedCostData(goodsCostVoList, timeStr, costTimeType, polymerizeType);
    }

    public List<CostOfGoodsPo> getMoDataBySkuAndTime(String sku, String costTime) {
        return baseMapper.selectList(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .eq(CostOfGoodsPo::getCostTime, costTime)
                .eq(CostOfGoodsPo::getSku, sku)
                .eq(CostOfGoodsPo::getCostTimeType, CostTimeType.MONTH));

    }

    public List<CostOfGoodsPo> getListBySkuAndTimeOrType(String sku,
                                                         String costTime,
                                                         CostTimeType costTimeType,
                                                         PolymerizeType polymerizeType,
                                                         PolymerizeWarehouse polymerizeWarehouse,
                                                         String warehouseCode) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .eq(CostOfGoodsPo::getSku, sku)
                .eq(StringUtils.isNotBlank(costTime), CostOfGoodsPo::getCostTime, costTime)
                .eq(costTimeType != null, CostOfGoodsPo::getCostTimeType, costTimeType)
                .eq(polymerizeType != null, CostOfGoodsPo::getPolymerizeType, polymerizeType)
                .eq(polymerizeWarehouse != null, CostOfGoodsPo::getPolymerizeWarehouse, polymerizeWarehouse)
                .eq(StringUtils.isNotBlank(warehouseCode), CostOfGoodsPo::getWarehouseCode, warehouseCode)
                .orderByDesc(CostOfGoodsPo::getUpdateTime));

    }

    public CostOfGoodsPo getMoDataBySkuAndWarehouse(String sku, String warehouseCode,
                                                    PolymerizeType polymerizeType,
                                                    PolymerizeWarehouse polymerizeWarehouse, String costTime) {
        if (StringUtils.isBlank(sku) || null == polymerizeType
                || (StringUtils.isBlank(warehouseCode) && null == polymerizeWarehouse)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .eq(CostOfGoodsPo::getSku, sku)
                .eq(CostOfGoodsPo::getPolymerizeType, polymerizeType)
                .eq(StringUtils.isNotBlank(warehouseCode), CostOfGoodsPo::getWarehouseCode, warehouseCode)
                .eq(null != polymerizeWarehouse, CostOfGoodsPo::getPolymerizeWarehouse, polymerizeWarehouse)
                .eq(CostOfGoodsPo::getCostTimeType, CostTimeType.MONTH)
                .eq(CostOfGoodsPo::getCostTime, costTime));


    }

    public CommonPageResult.PageInfo<GoodsCostLogItemVo> searchGoodsOfCostLogs(Page<Void> page,
                                                                               @NotBlank String sku,
                                                                               PolymerizeType polymerizeType,
                                                                               CostTimeType costTimeType,
                                                                               PolymerizeWarehouse polymerizeWarehouse,
                                                                               String warehouseCode) {
        return PageInfoUtil.getPageInfo(baseMapper.searchGoodsOfCostLogs(page, sku, polymerizeType, costTimeType, polymerizeWarehouse, warehouseCode));
    }

    public CommonPageResult.PageInfo<GoodsOfCostExportVo> searchGoodsOfCostExport(Page<Void> page, GoodsCostDto dto, String timeStr, CostTimeType costTimeType) {
        return PageInfoUtil.getPageInfo(baseMapper.searchGoodsOfCostExport(page, dto, timeStr, costTimeType));

    }

    public List<CostOfGoodsPo> getSkuCostBySkuAndWarehouse(String timeStr,
                                                           CostTimeType costTimeType,
                                                           PolymerizeType polymerizeType,
                                                           List<CostSkuItemDto> singleWarehouseDtoList) {
        if (CollectionUtils.isEmpty(singleWarehouseDtoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getSkuCostBySkuAndWarehouse(timeStr, costTimeType, polymerizeType, singleWarehouseDtoList);
    }

    public List<CostOfGoodsPo> getSkuCostBySkuAndMulWarehouse(String timeStr, CostTimeType costTimeType,
                                                              PolymerizeType polymerizeType,
                                                              PolymerizeWarehouse polymerizeWarehouse,
                                                              List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .eq(CostOfGoodsPo::getCostTime, timeStr)
                .eq(CostOfGoodsPo::getCostTimeType, costTimeType)
                .eq(CostOfGoodsPo::getPolymerizeType, polymerizeType)
                .eq(CostOfGoodsPo::getPolymerizeWarehouse, polymerizeWarehouse)
                .in(CostOfGoodsPo::getSku, skuList));
    }


    public CostOfGoodsPo getSkuCostBySkuAndMulWarehouse(String timeStr, CostTimeType costTimeType,
                                                        PolymerizeType polymerizeType,
                                                        PolymerizeWarehouse polymerizeWarehouse,
                                                        String sku) {
        if (StringUtils.isBlank(sku)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .eq(CostOfGoodsPo::getCostTime, timeStr)
                .eq(CostOfGoodsPo::getCostTimeType, costTimeType)
                .eq(CostOfGoodsPo::getPolymerizeType, polymerizeType)
                .eq(CostOfGoodsPo::getPolymerizeWarehouse, polymerizeWarehouse)
                .eq(CostOfGoodsPo::getSku, sku));
    }

    public IPage<CostOfGoodsPo> getYestCostByCostTime(Page<CostOfGoodsPo> page, String costTime) {
        return baseMapper.selectPage(page, Wrappers.<CostOfGoodsPo>lambdaQuery()
                .eq(CostOfGoodsPo::getCostTime, costTime)
                .eq(CostOfGoodsPo::getCostTimeType, CostTimeType.DAY)
                .orderByAsc(CostOfGoodsPo::getCostOfGoodsId));
    }

    public Long getCntByTimeStr(String costTime) {
        return baseMapper.selectCount(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .eq(CostOfGoodsPo::getCostTime, costTime));
    }

    public List<CostOfGoodsPo> getMonthCostNotExist(String lastMonthStr, String currentMonthString) {

        return baseMapper.getMonthCostNotExist(lastMonthStr, currentMonthString);
    }

    public List<CostOfGoodsPo> getMonthCostBySkuAndWarehouse(List<CostOfGoodsPo> costOfGoodsPoList,
                                                             @NotBlank(message = "成本时间不能为空") String costTime) {
        if (CollectionUtils.isEmpty(costOfGoodsPoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getMonthCostBySkuAndWarehouse(costOfGoodsPoList, costTime);

    }

    public List<CostOfGoodsPo> getWeightingPriceBySkuList(List<String> skuList,
                                                          String timeStr,
                                                          CostTimeType costTimeType,
                                                          PolymerizeType polymerizeType,
                                                          PolymerizeWarehouse polymerizeWarehouse,
                                                          BigDecimal weightingPrice) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .in(CostOfGoodsPo::getSku, skuList)
                .eq(CostOfGoodsPo::getCostTime, timeStr)
                .eq(CostOfGoodsPo::getCostTimeType, costTimeType)
                .eq(CostOfGoodsPo::getPolymerizeType, polymerizeType)
                .eq(CostOfGoodsPo::getPolymerizeWarehouse, polymerizeWarehouse)
                .ne(null != weightingPrice, CostOfGoodsPo::getWeightingPrice, weightingPrice));
    }

    public List<CostOfGoodsPo> getListBySkuListAndCostTime(List<String> skuList,
                                                           String timeStr,
                                                           CostTimeType costTimeType) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .in(CostOfGoodsPo::getSku, skuList)
                .eq(CostOfGoodsPo::getCostTime, timeStr)
                .eq(CostOfGoodsPo::getCostTimeType, costTimeType));
    }

    public long getCountByCondition(String costTime, String warehouseCode, PolymerizeType polymerizeType, String sku) {
        if (StringUtils.isBlank(sku)) {
            return 0;
        }

        return baseMapper.selectCount(Wrappers.<CostOfGoodsPo>lambdaQuery()
                .eq(CostOfGoodsPo::getCostTime, costTime)
                .eq(StringUtils.isNotBlank(warehouseCode), CostOfGoodsPo::getWarehouseCode, warehouseCode)
                .eq(null != polymerizeType, CostOfGoodsPo::getPolymerizeType, polymerizeType)
                .eq(CostOfGoodsPo::getSku, sku));
    }

    public List<CostOfGoodsPo> getMoDataByBatchBo(List<SkuAndWarehouseBatchBo> boList) {
        if (CollectionUtils.isEmpty(boList)) {
            return Collections.emptyList();
        }

        return baseMapper.getMoDataByBatchBo(boList);

    }
}
