package com.hete.supply.scm.server.scm.cost.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.CostSkuItemDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsCostDto;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.GoodsOfCostExportVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.CostImportDto;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.cost.converter.CostConverter;
import com.hete.supply.scm.server.scm.cost.dao.CostOfGoodsDao;
import com.hete.supply.scm.server.scm.cost.entity.dto.*;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.cost.entity.vo.*;
import com.hete.supply.scm.server.scm.cost.service.base.CostBaseService;
import com.hete.supply.scm.server.scm.dao.ProduceDataDao;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPricingOrderInfoByPriceTimeBo;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopPricingOrderBaseService;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import com.hete.supply.scm.server.scm.nacosconfig.SkuCategoryProp;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseItemByReceiveTimeBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/2/22 09:23
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CostBizService {
    private final CostOfGoodsDao costOfGoodsDao;
    private final PlmRemoteService plmRemoteService;
    private final CostBaseService costBaseService;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final ProduceDataDao produceDataDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopPricingOrderBaseService developPricingOrderBaseService;
    private final PurchaseBaseService purchaseBaseService;
    private final SkuCategoryProp skuCategoryProp;

    public CommonPageResult.PageInfo<GoodsCostVo> searchGoodsOfCost(GoodsCostDto dto) {
        return costBaseService.searchGoodsOfCostPageInfo(dto);
    }

    /**
     * 详情
     *
     * @param dto:
     * @return GoodsCostDetailVo
     * @author ChenWenLong
     * @date 2024/2/28 14:51
     */
    public GoodsCostDetailVo goodsOfCostDetails(CostOfGoodsIdDto dto) {
        CostOfGoodsPo costOfGoodsPo = costOfGoodsDao.getById(dto.getCostOfGoodsId());
        if (null == costOfGoodsPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        String sku = costOfGoodsPo.getSku();

        // 转换数据
        GoodsCostDetailVo goodsCostDetailVo = CostConverter.convertCostOfGoodsPoToVo(costOfGoodsPo);

        //获取昨日或月初的数据
        if (CostTimeType.DAY.equals(costOfGoodsPo.getCostTimeType())) {
            // 月度时间
            final String currentMonthString = ScmTimeUtil.getCurrentMonthString();
            List<CostOfGoodsPo> costOfGoodsPoMoList = costOfGoodsDao.getListBySkuAndTimeOrType(sku, currentMonthString,
                    CostTimeType.MONTH, costOfGoodsPo.getPolymerizeType(), costOfGoodsPo.getPolymerizeWarehouse(), costOfGoodsPo.getWarehouseCode());
            if (CollectionUtils.isNotEmpty(costOfGoodsPoMoList)) {
                CostOfGoodsPo costOfGoodsPoMo = costOfGoodsPoMoList.get(0);
                // 获取昨日商品成本信息
                goodsCostDetailVo.setMoInventory(costOfGoodsPoMo.getInventory());
                goodsCostDetailVo.setMoInventoryPrice(costOfGoodsPoMo.getInventoryPrice());
                goodsCostDetailVo.setMoWeightingPrice(costOfGoodsPoMo.getWeightingPrice());
                goodsCostDetailVo.setMoUpdateTime(costOfGoodsPoMo.getUpdateTime());
            }
        }

        if (CostTimeType.MONTH.equals(costOfGoodsPo.getCostTimeType())) {
            // 昨日日期
            final String yesterdayDateString = ScmTimeUtil.getBeforeDateString(1);
            List<CostOfGoodsPo> costOfGoodsPoDayList = costOfGoodsDao.getListBySkuAndTimeOrType(sku, yesterdayDateString,
                    CostTimeType.DAY, costOfGoodsPo.getPolymerizeType(), costOfGoodsPo.getPolymerizeWarehouse(), costOfGoodsPo.getWarehouseCode());
            if (CollectionUtils.isNotEmpty(costOfGoodsPoDayList)) {
                CostOfGoodsPo costOfGoodsPoDay = costOfGoodsPoDayList.get(0);
                // 获取昨日商品成本信息
                goodsCostDetailVo.setYestInventory(costOfGoodsPoDay.getInventory());
                goodsCostDetailVo.setYestInventoryPrice(costOfGoodsPoDay.getInventoryPrice());
                goodsCostDetailVo.setYestWeightingPrice(costOfGoodsPoDay.getWeightingPrice());
                goodsCostDetailVo.setYestUpdateTime(costOfGoodsPoDay.getUpdateTime());
            }
        }


        // 获取产品名称
        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(List.of(sku));

        // 获取核价单信息
        List<DevelopPricingOrderInfoByPriceTimeBo> developPricingOrderInfoPriceTimeBoList = developPricingOrderBaseService.getPriceTimeBoListBySkuList(List.of(sku));
        if (CollectionUtils.isNotEmpty(developPricingOrderInfoPriceTimeBoList)) {
            DevelopPricingOrderInfoByPriceTimeBo developPricingOrderInfoByPriceTimeBo = developPricingOrderInfoPriceTimeBoList.get(0);
            goodsCostDetailVo.setDevelopPricingPurchasePrice(developPricingOrderInfoByPriceTimeBo.getBulkPrice());
            goodsCostDetailVo.setNuclearPriceTime(developPricingOrderInfoByPriceTimeBo.getNuclearPriceTime());
        }

        // 获取采购信息
        List<PurchaseItemByReceiveTimeBo> purchaseItemByReceiveTimeBoList = purchaseChildOrderItemDao.getListByPurchaseItemAndReceiveTime(List.of(sku), PurchaseOrderStatus.getCostOfGoodsNotStatus());
        if (CollectionUtils.isNotEmpty(purchaseItemByReceiveTimeBoList)) {
            PurchaseItemByReceiveTimeBo purchaseItemByReceiveTimeBo = purchaseItemByReceiveTimeBoList.get(0);
            goodsCostDetailVo.setPurchaseChildPurchasePrice(purchaseItemByReceiveTimeBo.getPurchasePrice());
            goodsCostDetailVo.setReceiveOrderTime(purchaseItemByReceiveTimeBo.getReceiveOrderTime());
        }

        // 获取生产信息商品采购价格
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (null != produceDataPo && null != produceDataPo.getGoodsPurchasePriceTime()) {
            goodsCostDetailVo.setGoodsPurchasePrice(produceDataPo.getGoodsPurchasePrice());
            goodsCostDetailVo.setGoodsPurchasePriceTime(produceDataPo.getGoodsPurchasePriceTime());
        }

        goodsCostDetailVo.setSku(sku);
        goodsCostDetailVo.setSkuEncode(skuEncodeMap.get(sku));
        return goodsCostDetailVo;
    }

    /**
     * 变更记录
     *
     * @param dto:
     * @return PageInfo<GoodsCostLogItemVo>
     * @author ChenWenLong
     * @date 2024/2/28 14:51
     */
    public CommonPageResult.PageInfo<GoodsCostLogItemVo> goodsOfCostDayLogs(CostOfGoodsLogsPageDto dto) {
        CostOfGoodsPo costOfGoodsPo = costOfGoodsDao.getById(dto.getCostOfGoodsId());
        if (null == costOfGoodsPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return costOfGoodsDao.searchGoodsOfCostLogs(PageDTO.of(dto.getPageNo(), dto.getPageSize()),
                costOfGoodsPo.getSku(), costOfGoodsPo.getPolymerizeType(), dto.getCostTimeType(), costOfGoodsPo.getPolymerizeWarehouse(), costOfGoodsPo.getWarehouseCode());
    }

    /**
     * 列表导出(单仓)
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/2/28 14:51
     */
    @Transactional(rollbackFor = Exception.class)
    public void goodsOfCostOneExport(GoodsCostDto dto) {
        Integer exportTotals = this.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && exportTotals > 0, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.IBFS_GOODS_OF_COST_ONE_EXPORT.getCode(), dto));
    }

    /**
     * 列表导出(多仓)
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/2/28 14:51
     */
    @Transactional(rollbackFor = Exception.class)
    public void goodsOfCostManyExport(GoodsCostDto dto) {
        Integer exportTotals = this.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && exportTotals > 0, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.IBFS_GOODS_OF_COST_MANY_EXPORT.getCode(), dto));
    }

    /**
     * 商品成本列表导出
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < GoodsOfCostExportVo>>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    public CommonResult<ExportationListResultBo<GoodsOfCostExportVo>> getExportList(GoodsCostDto dto) {
        ExportationListResultBo<GoodsOfCostExportVo> resultBo = new ExportationListResultBo<>();
        List<GoodsOfCostExportVo> goodsOfCostExportVoList = this.getExportListInfo(dto);
        resultBo.setRowDataList(goodsOfCostExportVoList);
        return CommonResult.success(resultBo);
    }

    /**
     * 商品成本统计
     *
     * @param dto:
     * @return Long
     * @author ChenWenLong
     * @date 2024/2/28 16:19
     */
    public Integer getExportTotals(GoodsCostDto dto) {
        return this.getExportListInfo(dto).size();
    }

    /**
     * 商品成本列表导出分页
     *
     * @param dto:
     * @return List<GoodsOfCostExportVo>
     * @author ChenWenLong
     * @date 2024/2/28 16:20
     */
    public List<GoodsOfCostExportVo> getExportListInfo(GoodsCostDto dto) {
        // 禁止分页时count
        dto.setSearchCount(Boolean.FALSE);
        // 导出
        dto.setIsExport(Boolean.TRUE);
        final CommonPageResult.PageInfo<GoodsCostVo> pageInfo = costBaseService.searchGoodsOfCostPageInfo(dto);
        final List<GoodsCostVo> records = pageInfo.getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        final List<String> skuList = records.stream()
                .map(GoodsCostVo::getSku)
                .distinct()
                .collect(Collectors.toList());

        // 获取核价单信息
        List<DevelopPricingOrderInfoByPriceTimeBo> developPricingOrderInfoPriceTimeBoList = developPricingOrderBaseService.getPriceTimeBoListBySkuList(skuList);

        // 获取采购信息
        List<PurchaseItemByReceiveTimeBo> purchaseItemByReceiveTimeBoList = purchaseChildOrderItemDao.getListByPurchaseItemAndReceiveTime(skuList, PurchaseOrderStatus.getCostOfGoodsNotStatus());

        return CostConverter.convertSearchPagePoListToExportVo(records,
                developPricingOrderInfoPriceTimeBoList,
                purchaseItemByReceiveTimeBoList);

    }

    public CostCalRawPriceVo calculateRawPrice(CostCalRawPriceDto dto) {
        final String currentMonthString = ScmTimeUtil.getCurrentMonthString();
        final List<CostCalRawPriceItemDto> costCalRawPriceItemList = dto.getCostCalRawPriceItemList();
        // 获取采购子单
        final List<String> purchaseChildOrderNoList = costCalRawPriceItemList.stream()
                .map(CostCalRawPriceItemDto::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseBaseService.getPoListByPurchaseChildNoList(purchaseChildOrderNoList);
        if (purchaseChildOrderNoList.size() != purchaseChildOrderPoList.size()) {
            throw new BizException("查找不到对应的采购子单，请联系系统管理员！");
        }


        costCalRawPriceItemList.forEach(itemDto -> {
            final List<RawItemDto> rawItemList = itemDto.getRawItemList();
            final List<CostOfGoodsPo> costOfGoodsPoList = new ArrayList<>();
            // 没有传仓库的，默认查广州仓
            final List<String> noWarehouseRawSkuItemList = rawItemList.stream()
                    .filter(rawItemDto -> StringUtils.isBlank(rawItemDto.getRawWarehouseCode()))
                    .map(RawItemDto::getRawSku)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(noWarehouseRawSkuItemList)) {
                costOfGoodsPoList.addAll(costOfGoodsDao.getSkuCostBySkuAndMulWarehouse(currentMonthString, CostTimeType.MONTH,
                        PolymerizeType.MULTIPLE_WAREHOUSE, PolymerizeWarehouse.GUANGZHOU, noWarehouseRawSkuItemList));
            }

            // 存在仓库的
            final List<RawItemDto> warehouseRawItemDtoList = rawItemList.stream()
                    .filter(rawItemDto -> StringUtils.isNotBlank(rawItemDto.getRawWarehouseCode()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(warehouseRawItemDtoList)) {
                final List<CostSkuItemDto> skuItemDtoList = warehouseRawItemDtoList.stream()
                        .map(rawItemDto -> {
                            final CostSkuItemDto costSkuItemDto = new CostSkuItemDto();
                            costSkuItemDto.setSku(rawItemDto.getRawSku());
                            costSkuItemDto.setWarehouseCode(rawItemDto.getRawWarehouseCode());
                            return costSkuItemDto;
                        }).collect(Collectors.toList());
                costOfGoodsPoList.addAll(costOfGoodsDao.getSkuCostBySkuAndWarehouse(currentMonthString, CostTimeType.MONTH,
                        PolymerizeType.SINGLE_WAREHOUSE, skuItemDtoList));
            }

            final Map<String, BigDecimal> skuCostPriceMap = costOfGoodsPoList.stream()
                    .collect(Collectors.toMap(CostOfGoodsPo::getSku, CostOfGoodsPo::getWeightingPrice));

            rawItemList.forEach(rawItemDto -> rawItemDto.setCostPrice(skuCostPriceMap.getOrDefault(rawItemDto.getRawSku(), BigDecimal.ZERO)));
        });


        final Map<String, PurchaseChildOrderPo> purchaseChildOrderPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        final List<CostCalRawPriceItemVo> costCalRawPriceItemVoList = costCalRawPriceItemList.stream().map(itemDto -> {
            final CostCalRawPriceItemVo costCalRawPriceItemVo = new CostCalRawPriceItemVo();
            costCalRawPriceItemVo.setPurchaseChildOrderNo(itemDto.getPurchaseChildOrderNo());
            final List<RawItemDto> rawItemList = itemDto.getRawItemList();
            final BigDecimal costPriceSum = rawItemList.stream().map(rawItemDto -> rawItemDto.getCostPrice()
                            .multiply(new BigDecimal(rawItemDto.getRawCnt())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderPoMap.get(itemDto.getPurchaseChildOrderNo());
            final BigDecimal substractPrice = costPriceSum.divide(new BigDecimal(purchaseChildOrderPo.getPurchaseTotal()), 2, RoundingMode.DOWN);
            costCalRawPriceItemVo.setSubstractPrice(substractPrice);

            return costCalRawPriceItemVo;
        }).collect(Collectors.toList());
        final CostCalRawPriceVo costCalRawPriceVo = new CostCalRawPriceVo();
        costCalRawPriceVo.setCostCalRawPriceItemList(costCalRawPriceItemVoList);

        return costCalRawPriceVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveChangeMoDataSync(CostImportDto costImportDto) {
        costBaseService.saveChangeMoData(costImportDto);
    }

}
