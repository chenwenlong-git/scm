package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.org.apache.commons.lang3.exception.ExceptionUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.cost.entity.bo.SkuAndWarehouseBo;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.cost.service.base.CostBaseService;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrValueBySkuDto;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataAttrValueBySkuVo;
import com.hete.supply.scm.server.scm.ibfs.service.base.BatchCodeCostBaseService;
import com.hete.supply.scm.server.scm.process.builder.MaterialBuilder;
import com.hete.supply.scm.server.scm.process.builder.ProcOrderMaterialBuilder;
import com.hete.supply.scm.server.scm.process.builder.SkuWeightedPriceBuilder;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderMaterialVo;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.util.HeteCollectionsUtil;
import com.hete.supply.wms.api.basic.facade.SkuBatchPriceVo;
import com.hete.support.api.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/10/7.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessOrderMaterialBaseService {

    private final ProcessTemplateMaterialDao processTemplateMaterialDao;
    private final ProcessOrderDao processOrderDao;
    private final ProcessMaterialReceiptDao materialReceiptDao;
    private final ProcessMaterialReceiptItemDao materialReceiptItemDao;
    private final ProcessMaterialBackDao materialBackDao;
    private final ProcessMaterialBackItemDao materialBackItemDao;
    private final WmsRemoteService wmsRemoteService;
    private final CostBaseService costBaseService;
    private final PlmRemoteService plmRemoteService;
    private final Environment environment;
    private final ProduceDataBaseService produceDataBaseService;
    private final BatchCodeCostBaseService batchCodeCostBaseService;

    /**
     * 根据一组SKU更新原料模板信息到原料信息VO中。
     *
     * @param processOrderMaterialVos 加工单Vo列表
     * @param processOrderMaterialPos 原料模板信息列表
     */
    public void updateMaterialSkuType(List<ProcessOrderMaterialVo> processOrderMaterialVos,
                                      List<ProcessOrderMaterialPo> processOrderMaterialPos) {
        List<String> materialSkus = processOrderMaterialPos.stream()
                .map(ProcessOrderMaterialPo::getSku)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(materialSkus)) {
            List<ProcessTemplateMaterialPo> processTemplateMaterialPos = processTemplateMaterialDao.getBySkus(
                    materialSkus);

            processOrderMaterialVos.forEach(processOrderMaterialVo -> {
                final String materialSku = processOrderMaterialVo.getSku();
                ProcessTemplateMaterialPo matchProcessTemplateMaterial = processTemplateMaterialPos.stream()
                        .filter(processTemplateMaterialPo -> Objects.equals(materialSku,
                                processTemplateMaterialPo.getSku()))
                        .findFirst()
                        .orElse(null);

                if (Objects.nonNull(matchProcessTemplateMaterial)) {
                    processOrderMaterialVo.setMaterialSkuType(matchProcessTemplateMaterial.getMaterialSkuType());
                }
            });
        }
    }

    /**
     * 获取sku实时月初加权价格。
     *
     * @param skuWeightedPriceQueryBos 批次码实时月初加权价格查询列表
     * @return 批次码实时月初加权价格结果列表
     */
    public List<SkuWeightedPriceResultBo> getSkuWeightedPricesBySingleWarehouse(List<SkuWeightedPriceQueryBo> skuWeightedPriceQueryBos) {
        return skuWeightedPriceQueryBos.stream()
                .map(skuWeightedPriceQueryBo -> {
                    SkuAndWarehouseBo queryBatchWeightedPrice = new SkuAndWarehouseBo();
                    queryBatchWeightedPrice.setSku(skuWeightedPriceQueryBo.getSku());
                    queryBatchWeightedPrice.setWarehouseCode(skuWeightedPriceQueryBo.getWarehouseCode());
                    queryBatchWeightedPrice.setPolymerizeType(PolymerizeType.SINGLE_WAREHOUSE);
                    CostOfGoodsPo moWeightingPrice = costBaseService.getMoWeightingPrice(queryBatchWeightedPrice);

                    SkuWeightedPriceResultBo skuWeightedPriceResultBo = new SkuWeightedPriceResultBo();
                    skuWeightedPriceResultBo.setSku(skuWeightedPriceQueryBo.getSku());
                    if (Objects.nonNull(moWeightingPrice)) {
                        BigDecimal weightingPrice = Objects.isNull(moWeightingPrice.getWeightingPrice()) ||
                                moWeightingPrice.getWeightingPrice()
                                        .compareTo(BigDecimal.ZERO) == 0 ?
                                BigDecimal.ZERO : moWeightingPrice.getWeightingPrice();
                        skuWeightedPriceResultBo.setMonthStartWeightedPrice(weightingPrice);
                    }
                    return skuWeightedPriceResultBo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取多仓库的月初加权价。
     *
     * @param skuWeightedPriceQueryBos 批次码实时月初加权价格查询列表
     * @param polymerizeWarehouse      是否聚合仓库
     * @return 批次码实时月初加权价格结果列表
     */
    public List<SkuWeightedPriceResultBo> getSkuWeightedPricesByPolymerizeWarehouse(List<SkuWeightedPriceQueryBo> skuWeightedPriceQueryBos,
                                                                                    PolymerizeWarehouse polymerizeWarehouse) {
        return skuWeightedPriceQueryBos.stream()
                .map(skuWeightedPriceQueryBo -> {
                    SkuAndWarehouseBo queryBatchWeightedPrice = new SkuAndWarehouseBo();
                    queryBatchWeightedPrice.setSku(skuWeightedPriceQueryBo.getSku());
                    queryBatchWeightedPrice.setPolymerizeType(PolymerizeType.MULTIPLE_WAREHOUSE);
                    queryBatchWeightedPrice.setPolymerizeWarehouse(polymerizeWarehouse);
                    CostOfGoodsPo moWeightingPrice = costBaseService.getMoWeightingPrice(queryBatchWeightedPrice);

                    SkuWeightedPriceResultBo skuWeightedPriceResultBo = new SkuWeightedPriceResultBo();
                    skuWeightedPriceResultBo.setSku(skuWeightedPriceQueryBo.getSku());
                    if (Objects.nonNull(moWeightingPrice)) {
                        BigDecimal weightingPrice = Objects.isNull(
                                moWeightingPrice.getWeightingPrice()) || moWeightingPrice.getWeightingPrice()
                                .compareTo(BigDecimal.ZERO) == 0 ?
                                BigDecimal.ZERO :
                                moWeightingPrice.getWeightingPrice();
                        skuWeightedPriceResultBo.setMonthStartWeightedPrice(weightingPrice);
                    }
                    return skuWeightedPriceResultBo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取原料SKU价格。
     *
     * @param skuPriceQueryList SKU价格查询列表
     * @return SKU价格结果列表
     */
    public List<SkuPriceResultBo> getMaterialSkuPrices(List<SkuPriceQueryBo> skuPriceQueryList) {
        List<SkuPriceResultBo> skuPriceResultBos
                = SkuWeightedPriceBuilder.buildSkuPriceResults(skuPriceQueryList);

        // 获取批次单价、月初加权价
        List<String> queryPriceBatchCode = skuPriceQueryList.stream()
                .map(SkuPriceQueryBo::getBatchCode)
                .collect(Collectors.toList());
        CompletableFuture<List<MaterialBatchCostBo>> batchCodePriceFuture = CompletableFuture
                .supplyAsync(() -> getWmsMaterialBatchCodePrices(queryPriceBatchCode));

        List<SkuWeightedPriceQueryBo> skuWeightedPriceQueryBos
                = SkuWeightedPriceBuilder.buildWeightedPriceQueries(skuPriceQueryList);
        CompletableFuture<List<SkuWeightedPriceResultBo>> monthPriceFuture = CompletableFuture
                .supplyAsync(() -> getSkuWeightedPricesBySingleWarehouse(skuWeightedPriceQueryBos));

        try {
            CompletableFuture<Void> combinedFuture
                    = CompletableFuture.allOf(batchCodePriceFuture, monthPriceFuture);
            combinedFuture.get();
            List<MaterialBatchCostBo> wmsMaterialBatchCodePrices = batchCodePriceFuture.get();
            log.info("计算原料批次单价结果：{}", JSON.toJSONString(wmsMaterialBatchCodePrices));

            List<SkuWeightedPriceResultBo> batchWeightedPrices = monthPriceFuture.get();
            log.info("计算原料月初加权价结果：{}", JSON.toJSONString(batchWeightedPrices));

            for (SkuPriceResultBo skuPriceResultBo : skuPriceResultBos) {
                String batchCode = skuPriceResultBo.getBatchCode();
                String sku = skuPriceResultBo.getSku();

                MaterialBatchCostBo matchBatchPrice = wmsMaterialBatchCodePrices.stream()
                        .filter(batchCodePrice -> Objects.equals(batchCode, batchCodePrice.getBatchCode()))
                        .findFirst()
                        .orElse(null);
                BigDecimal batchPrice = Objects.isNull(
                        matchBatchPrice) ? BigDecimal.ZERO : matchBatchPrice.getCostPrice();

                SkuWeightedPriceResultBo matchMonthStartPrice = batchWeightedPrices.stream()
                        .filter(monthStartPrice -> Objects.equals(sku, monthStartPrice.getSku()))
                        .findFirst()
                        .orElse(null);

                BigDecimal monthStartPrice = Objects.isNull(
                        matchMonthStartPrice) ? BigDecimal.ZERO : matchMonthStartPrice.getMonthStartWeightedPrice();

                BigDecimal price = batchPrice.compareTo(BigDecimal.ZERO) > 0 ? batchPrice : monthStartPrice;
                skuPriceResultBo.setPrice(price);
            }
            log.info("计算原料批次单价&月初加权价结果：{}", JSON.toJSONString(skuPriceResultBos));

        } catch (InterruptedException | ExecutionException e) {
            throw new BizException("获取原料批次/月初加权 单价异常 {}", ExceptionUtils.getStackTrace(e));
        }

        return skuPriceResultBos;
    }

    /**
     * 计算加工原料总成本的方法。
     *
     * @param processOrderNo 加工单号
     * @return 加工原料总成本，以 BigDecimal 类型返回
     */
    public BigDecimal calculateMaterialTotalCost(String processOrderNo) {
        // 初始化原料总成本为零
        BigDecimal materialTotalCost = BigDecimal.ZERO;

        // 查询加工单信息
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            return materialTotalCost;
        }

        // 查询加工单关联的原料收货记录 & 查询加工单关联的原料收货记录明细 & 查询加工单关联的原料归还记录 & 查询加工单关联的原料归还记录明细
        MaterialAssociationBo materialAssociation = getProcessOrderMaterialAssociation(processOrderNo);
        List<ProcessMaterialReceiptPo> materialReceiptList = materialAssociation.getMaterialReceiptList();
        List<ProcessMaterialReceiptItemPo> materialReceiptItemList = materialAssociation.getMaterialReceiptItemList();
        List<ProcessMaterialBackItemPo> materialBackItemList = materialAssociation.getMaterialBackItemList();

        if (CollectionUtils.isEmpty(materialReceiptList)) {
            return materialTotalCost;
        }

        // 获取原料价格
        List<SkuPriceQueryBo> skuPriceQueryBos = MaterialBuilder.buildSkuPriceQueryBos(materialReceiptList,
                materialReceiptItemList);
        final List<SkuPriceResultBo> materialPrices = getMaterialSkuPrices(skuPriceQueryBos);

        // 将原料收货明细根据批次码分组求和 & 将原料归还明细根据批次码分组求和
        Map<String, Integer> sumReceiptNumBySkuBatchCode = materialReceiptItemList.stream()
                .collect(Collectors.groupingBy(ProcessMaterialReceiptItemPo::getSkuBatchCode,
                        Collectors.summingInt(ProcessMaterialReceiptItemPo::getReceiptNum)));
        Map<String, Integer> sumBackNumBySkuBatchCode = materialBackItemList.stream()
                .collect(Collectors.groupingBy(ProcessMaterialBackItemPo::getSkuBatchCode,
                        Collectors.summingInt(ProcessMaterialBackItemPo::getDeliveryNum)));
        // 遍历原料收货记录明细，计算原料总成本
        for (Map.Entry<String, Integer> receiptEntry : sumReceiptNumBySkuBatchCode.entrySet()) {
            String materialBatchCode = receiptEntry.getKey();

            // 计算实际使用数量（收货数量减去退货数量）
            Integer receiptNum = receiptEntry.getValue();
            Integer backNum = sumBackNumBySkuBatchCode.getOrDefault(materialBatchCode, 0);
            BigDecimal actUseNum = BigDecimal.valueOf(receiptNum - backNum);

            // 查询SKU批次码对应的成本价格 & 存在任意一个批次码价格为0则返回0,便于异常处理 & 计算批次码成本价
            BigDecimal matchMaterialPrice = materialPrices.stream()
                    .filter(materialPrice -> Objects.equals(materialBatchCode, materialPrice.getBatchCode()))
                    .findFirst()
                    .map(SkuPriceResultBo::getPrice)
                    .orElse(null);
            if (Objects.isNull(matchMaterialPrice)) {
                throw new BizException("计算原料批次码成本失败，批次码成本价格不存在！批次码:{}", materialBatchCode);
            }
            if (matchMaterialPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new BizException("计算原料批次码成本失败，批次码成本价格小于0！批次码:{}", materialBatchCode);
            }
            if (matchMaterialPrice.compareTo(BigDecimal.ZERO) == 0) {
                log.warn("计算原料批次码成本过程发现批次码成本为0，请相关同事校验是否合法！批次码:{}", materialBatchCode);

                //通过成本为0的批次码匹配sku编码，发送飞书提醒
                materialReceiptItemList.stream().filter(item -> Objects.equals(item.getSkuBatchCode(), materialBatchCode))
                        .findFirst().ifPresent(item -> batchCodeCostBaseService.sendAppTips(item.getSku()));
            }
            materialTotalCost = materialTotalCost.add(actUseNum.multiply(matchMaterialPrice).setScale(2, RoundingMode.HALF_UP));
        }

        // 返回计算得到的原料总成本
        return materialTotalCost;
    }

    /**
     * 批量查询批次码的方法。
     *
     * @param skuBatchCodes 批次码列表
     * @return 批次码成本信息列表
     */
    public List<MaterialBatchCostBo> getWmsMaterialBatchCodePrices(List<String> skuBatchCodes) {
        if (CollectionUtils.isEmpty(skuBatchCodes)) {
            return Collections.emptyList();
        }
        List<SkuBatchPriceVo> skuBatchPriceVos = wmsRemoteService.getSkuBatchPriceList(skuBatchCodes);
        return skuBatchCodes.stream()
                .map(batchCode -> {
                    MaterialBatchCostBo bo = new MaterialBatchCostBo();
                    bo.setBatchCode(batchCode);

                    BigDecimal matchCostPrice = skuBatchPriceVos.stream()
                            .filter(skuBatchPriceVo -> Objects.equals(batchCode, skuBatchPriceVo.getBatchCode()))
                            .findFirst()
                            .map(SkuBatchPriceVo::getPrice)
                            .orElse(BigDecimal.ZERO);
                    bo.setCostPrice(matchCostPrice);
                    return bo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取指定加工单号的原料关联信息。
     *
     * @param processOrderNo 加工单号
     * @return 返回包含原料关联信息的 MaterialAssociationBo 对象。如果找不到相关信息，则返回一个空的 MaterialAssociationBo 对象。
     */
    public MaterialAssociationBo getProcessOrderMaterialAssociation(String processOrderNo) {
        MaterialAssociationBo materialAssociationBo = new MaterialAssociationBo();

        List<ProcessMaterialReceiptPo> processMaterialReceiptPos = materialReceiptDao.getByProcessOrderNo(
                processOrderNo);
        materialAssociationBo.setMaterialReceiptList(processMaterialReceiptPos);

        List<Long> processMaterialReceiptIds = processMaterialReceiptPos.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                = materialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIds);
        materialAssociationBo.setMaterialReceiptItemList(processMaterialReceiptItemPos);

        List<ProcessMaterialBackPo> processMaterialBackPos = materialBackDao.getByProcessOrderNo(processOrderNo);
        materialAssociationBo.setMaterialBackList(processMaterialBackPos);

        List<Long> processMaterialBackIds = processMaterialBackPos.stream()
                .map(ProcessMaterialBackPo::getProcessMaterialBackId)
                .collect(Collectors.toList());
        List<ProcessMaterialBackItemPo> processMaterialBackItemPos = materialBackItemDao.listByProcessMaterialBackIds(
                processMaterialBackIds);
        materialAssociationBo.setMaterialBackItemList(processMaterialBackItemPos);

        return materialAssociationBo;
    }

    /**
     * 获取指定返修单号的原料关联信息。
     *
     * @param repairOrderNo 返修单号
     * @return 返回包含原料关联信息的 MaterialAssociationBo 对象。如果找不到相关信息，则返回一个空的 MaterialAssociationBo 对象。
     */
    public MaterialAssociationBo getRepairOrderMaterialAssociation(String repairOrderNo) {
        MaterialAssociationBo materialAssociationBo = new MaterialAssociationBo();

        List<ProcessMaterialReceiptPo> processMaterialReceiptPos = materialReceiptDao.getListByRepairOrderNo(
                repairOrderNo);
        materialAssociationBo.setMaterialReceiptList(processMaterialReceiptPos);

        List<Long> processMaterialReceiptIds = processMaterialReceiptPos.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                = materialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIds);
        materialAssociationBo.setMaterialReceiptItemList(processMaterialReceiptItemPos);

        List<ProcessMaterialBackPo> processMaterialBackPos = materialBackDao.listByRepairOrderNo(repairOrderNo);
        materialAssociationBo.setMaterialBackList(processMaterialBackPos);

        List<Long> processMaterialBackIds = processMaterialBackPos.stream()
                .map(ProcessMaterialBackPo::getProcessMaterialBackId)
                .collect(Collectors.toList());
        List<ProcessMaterialBackItemPo> processMaterialBackItemPos = materialBackItemDao.listByProcessMaterialBackIds(
                processMaterialBackIds);
        materialAssociationBo.setMaterialBackItemList(processMaterialBackItemPos);

        return materialAssociationBo;
    }

    /**
     * 获取SKU生产信息(二级类目ID，蕾丝面积，长度)
     *
     * @param sku
     * @return SKU生产信息
     */
    private MaterialProductionInfoBo getProductionInfo(String sku) {
        if (StrUtil.isBlank(sku)) {
            return null;
        }

        MaterialProductionInfoBo bo = new MaterialProductionInfoBo();
        bo.setSku(sku);

        // 获取sku二级分类id
        List<PlmGoodsDetailVo> goodsDetails = plmRemoteService.getGoodsDetail(Collections.singletonList(sku));
        if (CollectionUtils.isEmpty(goodsDetails)) {
            return bo;
        }
        PlmGoodsDetailVo matchSkuInfo = goodsDetails.stream()
                .findFirst()
                .orElse(null);
        if (Objects.isNull(matchSkuInfo)) {
            return bo;
        }
        List<PlmCategoryVo> categoryList = matchSkuInfo.getCategoryList();
        if (CollectionUtils.isEmpty(categoryList)) {
            return bo;
        }
        Long secondCategoryId = categoryList.stream()
                .filter(category -> Objects.equals(2, category.getLevel()))
                .map(PlmCategoryVo::getCategoryId)
                .findFirst()
                .orElse(null);
        bo.setSecondCategoryId(secondCategoryId);

        // 获取sku蕾丝面积与长度
        Long laceAreaAttributeNameId = produceDataBaseService.getLaceAreaAttributeNameId();
        Long lengthAttributeNameId = produceDataBaseService.getLengthAttributeNameId();
        List<Long> attributeNameIds = Arrays.asList(laceAreaAttributeNameId, lengthAttributeNameId);

        ProduceDataAttrValueBySkuDto queryDto
                = ProcOrderMaterialBuilder.buildProduceDataAttrValueBySkuDto(sku, attributeNameIds);
        ProduceDataAttrValueBySkuVo produceDataAttrValueBySkuVo
                = produceDataBaseService.produceDataAttrValueBySku(queryDto);
        if (Objects.isNull(produceDataAttrValueBySkuVo)) {
            return bo;
        }
        List<ProduceDataAttrValueBySkuVo.ProduceDataAttrValueBySkuItemVo> produceDataAttrValueBySkuItemList
                = produceDataAttrValueBySkuVo.getProduceDataAttrValueBySkuItemList();
        if (CollectionUtils.isEmpty(produceDataAttrValueBySkuItemList)) {
            return bo;
        }
        List<String> laceAreas = produceDataAttrValueBySkuItemList.stream()
                .filter(produceDataAttrValueBySkuItem -> Objects.equals(laceAreaAttributeNameId,
                        produceDataAttrValueBySkuItem.getAttributeNameId()))
                .map(ProduceDataAttrValueBySkuVo.ProduceDataAttrValueBySkuItemVo::getAttrValue)
                .collect(Collectors.toList());
        bo.setLaceAreas(laceAreas);

        List<String> lengths = produceDataAttrValueBySkuItemList.stream()
                .filter(produceDataAttrValueBySkuItem -> Objects.equals(lengthAttributeNameId,
                        produceDataAttrValueBySkuItem.getAttributeNameId()))
                .map(ProduceDataAttrValueBySkuVo.ProduceDataAttrValueBySkuItemVo::getAttrValue)
                .collect(Collectors.toList());
        bo.setLengths(lengths);
        return bo;
    }


    /**
     * 根据蕾丝面积和长度获取SKU列表
     *
     * @param laceAreasAttributes 蕾丝面积列表
     * @param lengthAttributes    长度列表
     * @return 返回包含SKU的列表
     */
    public List<String> getSkuListByLaceAreaAndLength(List<String> laceAreasAttributes,
                                                      List<String> lengthAttributes) {
        Long laceAreaAttributeNameId = produceDataBaseService.getLaceAreaAttributeNameId();
        Long lengthAttributeNameId = produceDataBaseService.getLengthAttributeNameId();

        // 获取生产资料同蕾丝面积属性的SKU列表
        Set<String> laceSkus
                = produceDataBaseService.listSkuByContainAttrValues(laceAreaAttributeNameId, laceAreasAttributes);
        Set<String> lengthSkus
                = produceDataBaseService.listSkuByContainAttrValues(lengthAttributeNameId, lengthAttributes);
        if (CollectionUtils.isEmpty(laceSkus) || CollectionUtils.isEmpty(lengthSkus)) {
            return Collections.emptyList();
        }

        Map<String, List<String>> laceAttributeMap
                = produceDataBaseService.getAttributesForSkus(laceSkus, laceAreaAttributeNameId);
        Map<String, List<String>> lengthAttributeMap
                = produceDataBaseService.getAttributesForSkus(lengthSkus, lengthAttributeNameId);

        log.info("计算加权平均价：参考SKU的蕾丝面积信息:{} ",
                JSON.toJSONString(laceAttributeMap));
        log.info("计算加权平均价：参考SKU的裆长尺寸信息:{} ",
                JSON.toJSONString(lengthAttributeMap));

        List<String> sameLaceSkus = HeteCollectionsUtil.filterIntersectingKeys(laceAreasAttributes, laceAttributeMap);
        List<String> sameLengthSkus = HeteCollectionsUtil.filterIntersectingKeys(lengthAttributes, lengthAttributeMap);
        if (CollectionUtils.isEmpty(sameLaceSkus) || CollectionUtils.isEmpty(sameLengthSkus)) {
            return Collections.emptyList();
        }

        return sameLaceSkus.stream()
                .filter(sameLengthSkus::contains)
                .collect(Collectors.toList());
    }

    public List<MonthStartWeightAvgPriceResultBo> getMonthStartWeightAvgPrices(List<QueryMonthStartWeightPriceBo> queryMonthStartWeightPriceBos) {
        if (CollectionUtils.isEmpty(queryMonthStartWeightPriceBos)) {
            return Collections.emptyList();
        }

        List<MonthStartWeightAvgPriceResultBo> resultBos
                = ProcOrderMaterialBuilder.buildMonthStartWeightAvgPriceResultBos(queryMonthStartWeightPriceBos);

        // 获取指定类目下的分类id
        Long wigAttributeNameId = produceDataBaseService.getWigAttributeNameId();
        List<Long> configCategoryIds = plmRemoteService.getChildByCategoryId(wigAttributeNameId);

        for (MonthStartWeightAvgPriceResultBo resultBo : resultBos) {
            String materialSku = resultBo.getSku();
            MaterialProductionInfoBo productionInfo = this.getProductionInfo(materialSku);
            if (Objects.isNull(productionInfo)) {
                continue;
            }

            Long curCategoryId = productionInfo.getSecondCategoryId();
            List<String> laceAreas = productionInfo.getLaceAreas();
            List<String> lengths = productionInfo.getLengths();
            if (Objects.isNull(curCategoryId) ||
                    CollectionUtils.isEmpty(laceAreas) ||
                    CollectionUtils.isEmpty(lengths)) {
                continue;
            }
            log.info("计算加权平均价：目标SKU:{} 类目ID:{} 蕾丝面积:{} 裆长长度:{}",
                    materialSku, curCategoryId,
                    JSON.toJSONString(laceAreas), JSON.toJSONString(lengths));

            if (CollectionUtils.isNotEmpty(configCategoryIds) && !configCategoryIds.contains(curCategoryId)) {
                log.info("计算加权平均价结束：目标SKU：{} 二级分类ID：{} 不属于指定类目ID列表:{}",
                        materialSku, curCategoryId, configCategoryIds.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")));
                continue;
            }

            // 获取相同蕾丝面积长度的SKU列表
            List<String> sameLaceAreaAndLengthSkuCodes = getSkuListByLaceAreaAndLength(laceAreas, lengths);
            if (CollectionUtils.isEmpty(sameLaceAreaAndLengthSkuCodes)) {
                continue;
            }
            sameLaceAreaAndLengthSkuCodes.remove(materialSku);
            if (CollectionUtils.isEmpty(sameLaceAreaAndLengthSkuCodes)) {
                continue;
            }
            log.info("计算加权平均价：筛选相同同蕾丝面积&裆长长度SKU：{}", JSON.toJSONString(sameLaceAreaAndLengthSkuCodes));

            // 查询月初加权价格 & 剔除月初加权不存在的sku
            List<SkuWeightedPriceQueryBo> skuWeightedPriceQueryBos
                    = SkuWeightedPriceBuilder.buildWeightedPriceQueriesBySkuCodes(sameLaceAreaAndLengthSkuCodes);
            List<SkuWeightedPriceResultBo> skuWeightedPrices
                    = getSkuWeightedPricesByPolymerizeWarehouse(skuWeightedPriceQueryBos, PolymerizeWarehouse.NON_WH);
            if (CollectionUtils.isEmpty(skuWeightedPrices)) {
                continue;
            }
            skuWeightedPrices.removeIf(skuWeightedPriceResultBo -> Objects.isNull(
                    skuWeightedPriceResultBo.getMonthStartWeightedPrice()) || skuWeightedPriceResultBo.getMonthStartWeightedPrice()
                    .compareTo(BigDecimal.ZERO) == 0);
            if (CollectionUtils.isEmpty(skuWeightedPrices)) {
                continue;
            }
            log.info("计算加权平均价：筛选参考SKU月初加权价结果：{}", JSON.toJSONString(skuWeightedPrices));

            // 获取关联类目Id列表 & 剔除非关联分类的sku
            List<Long> relateCategoryIds = plmRemoteService.getChildByCategoryId(curCategoryId);
            List<String> querySkuList = skuWeightedPrices.stream()
                    .map(SkuWeightedPriceResultBo::getSku)
                    .collect(Collectors.toList());
            Map<String, List<Long>> skuCategoryIdsMap = plmRemoteService.queryCategoryIds(querySkuList);
            log.info("计算加权平均价：目标SKU类目ID：{} 参考SKU类目:{}",
                    JSON.toJSONString(relateCategoryIds), JSON.toJSONString(skuCategoryIdsMap));

            Iterator<SkuWeightedPriceResultBo> iterator = skuWeightedPrices.iterator();
            while (iterator.hasNext()) {
                SkuWeightedPriceResultBo skuWeightedPriceResultBo = iterator.next();
                String categorySku = skuWeightedPriceResultBo.getSku();

                List<Long> categoryIds = skuCategoryIdsMap.get(categorySku);
                if (CollectionUtils.isEmpty(categoryIds)) {
                    iterator.remove();
                } else {
                    boolean hasIntersection = categoryIds.stream()
                            .anyMatch(relateCategoryIds::contains);
                    if (!hasIntersection) {
                        iterator.remove();
                    }
                }
            }

            if (CollectionUtils.isEmpty(skuWeightedPrices)) {
                continue;
            }

            BigDecimal totalMonthStartWeightedPrice = skuWeightedPrices.stream()
                    .map(SkuWeightedPriceResultBo::getMonthStartWeightedPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal averageMonthStartWeightedPrice = totalMonthStartWeightedPrice
                    .divide(BigDecimal.valueOf(skuWeightedPrices.size()), 2, RoundingMode.DOWN);
            resultBo.setMonthStartWeightedAveragePrice(averageMonthStartWeightedPrice);
        }

        log.info("计算加权平均价结束：{}", JSON.toJSONString(resultBos));
        return resultBos;
    }
}
