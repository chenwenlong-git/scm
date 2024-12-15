package com.hete.supply.scm.server.scm.develop.converter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.*;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.entity.vo.PricingDevelopSampleOrderSearchVo;
import com.hete.support.api.enums.BooleanType;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/08/08 14:35
 */
@Slf4j
public class DevelopPricingOrderConverter {

    public static DevelopPricingOrderDetailVo developPricingOrderPoToVo(DevelopPricingOrderPo developPricingOrderPo,
                                                                        Map<String, DevelopPricingOrderInfoPo> infoPoMap,
                                                                        Map<String, List<DevelopPricingOrderMechanismPo>> mechanismPoMap,
                                                                        Map<String, DevelopSampleOrderPo> developSampleOrderPoMap,
                                                                        Map<String, SupplierType> supplierTypeMap,
                                                                        Map<String, String> skuEncodeMap,
                                                                        DevelopChildOrderPo developChildOrderPo,
                                                                        List<DevelopChildOrderAttrPo> developChildOrderAttrPoList,
                                                                        Map<String, List<DevelopReviewSampleOrderInfoPo>> developReviewSampleOrderInfoPoMap,
                                                                        List<DevelopOrderPricePo> developOrderPricePoList) {
        DevelopPricingOrderDetailVo detailVo = new DevelopPricingOrderDetailVo();
        detailVo.setDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
        detailVo.setDevelopPricingOrderStatus(developPricingOrderPo.getDevelopPricingOrderStatus());
        detailVo.setPlatform(developPricingOrderPo.getPlatform());
        detailVo.setSupplierCode(developPricingOrderPo.getSupplierCode());
        detailVo.setIsSample(developChildOrderPo.getIsSample());

        if (supplierTypeMap.containsKey(developPricingOrderPo.getSupplierCode())) {
            detailVo.setSupplierType(supplierTypeMap.get(developPricingOrderPo.getSupplierCode()));
        }

        List<DevelopPricingOrderInfoVo> infoVoList = new ArrayList<>();

        developSampleOrderPoMap.forEach((String developSampleOrderNo, DevelopSampleOrderPo sampleOrderPo) -> {
            DevelopPricingOrderInfoVo infoVo = new DevelopPricingOrderInfoVo();
            infoVo.setDevelopSampleOrderNo(developSampleOrderNo);
            infoVo.setSupplierCode(sampleOrderPo.getSupplierCode());
            if (supplierTypeMap.containsKey(sampleOrderPo.getSupplierCode())) {
                infoVo.setSupplierType(supplierTypeMap.get(sampleOrderPo.getSupplierCode()));
            }
            infoVo.setSku(sampleOrderPo.getSku());
            infoVo.setSkuEncode(skuEncodeMap.get(sampleOrderPo.getSku()));
            infoVo.setSkuBatchCode(sampleOrderPo.getSkuBatchCode());
            infoVo.setDevelopSampleMethod(sampleOrderPo.getDevelopSampleMethod());

            List<DevelopReviewSampleInfoVo> developReviewSampleInfoList = Optional.ofNullable(developReviewSampleOrderInfoPoMap.get(developSampleOrderNo))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(infoPo -> {
                        final DevelopReviewSampleInfoVo developReviewSampleInfoVo = new DevelopReviewSampleInfoVo();
                        developReviewSampleInfoVo.setDevelopSampleOrderNo(infoPo.getDevelopSampleOrderNo());
                        developReviewSampleInfoVo.setAttributeNameId(infoPo.getAttributeNameId());
                        developReviewSampleInfoVo.setSampleInfoKey(infoPo.getSampleInfoKey());
                        developReviewSampleInfoVo.setSampleInfoValue(infoPo.getSampleInfoValue());
                        developReviewSampleInfoVo.setEvaluationOpinion(infoPo.getEvaluationOpinion());
                        return developReviewSampleInfoVo;
                    }).collect(Collectors.toList());

            infoVo.setDevelopReviewSampleInfoList(developReviewSampleInfoList);

            DevelopPricingOrderInfoPo infoPo = infoPoMap.get(developSampleOrderNo);
            if (infoPo != null) {
                infoVo.setSamplePrice(infoPo.getSamplePrice());
                infoVo.setRemarks(infoPo.getRemarks());
                infoVo.setOrdinary(infoPo.getOrdinary());
                infoVo.setFrontSize(infoPo.getFrontSize());
                infoVo.setHandWeavingSize(infoPo.getHandWeavingSize());
                infoVo.setPrice(infoPo.getPrice());
                infoVo.setGramWeight(infoPo.getGramWeight());
                infoVo.setHandWeavingPrice(infoPo.getHandWeavingPrice());
                infoVo.setHandHookPrice(infoPo.getHandHookPrice());
                infoVo.setProductionPrice(infoPo.getProductionPrice());
                infoVo.setMeshCap(infoPo.getMeshCap());
                infoVo.setCurvaturePrice(infoPo.getCurvaturePrice());
                infoVo.setStainPrice(infoPo.getStainPrice());
                infoVo.setCost(infoPo.getCost());
                infoVo.setFactoryProfit(infoPo.getFactoryProfit());
                infoVo.setQuotedPrice(infoPo.getQuotedPrice());
                infoVo.setWeightedPrice(infoPo.getWeightedPrice());
                infoVo.setSecondPrice(infoPo.getSecondPrice());
                infoVo.setManagePrice(infoPo.getManagePrice());
                infoVo.setCostTotalPrice(infoPo.getCostTotalPrice());

                List<DevelopPricingOrderMechanismVo> mechanismVoList = new ArrayList<>();
                List<DevelopPricingOrderMechanismPo> mechanismPoList = mechanismPoMap.get(developSampleOrderNo);
                if (CollectionUtils.isNotEmpty(mechanismPoList)) {
                    for (DevelopPricingOrderMechanismPo mechanismPo : mechanismPoList) {
                        DevelopPricingOrderMechanismVo mechanismVo = new DevelopPricingOrderMechanismVo();
                        mechanismVo.setHairSize(mechanismPo.getHairSize());
                        mechanismVo.setGramWeight(mechanismPo.getGramWeight());
                        mechanismVo.setPrice(mechanismPo.getPrice());
                        mechanismVo.setHairPrice(mechanismPo.getHairPrice());
                        mechanismVoList.add(mechanismVo);
                    }
                }
                infoVo.setDevelopPricingOrderMechanismList(mechanismVoList);

                // 需要打样获取关联渠道大货价格
                List<DevelopOrderPricePo> developOrderPricePoFilterList = developOrderPricePoList.stream()
                        .filter(developOrderPricePo -> DevelopOrderPriceType.PRICING_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                        .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developSampleOrderNo))
                        .collect(Collectors.toList());
                List<DevelopOrderPriceVo> developOrderPriceList = DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoFilterList);
                infoVo.setDevelopOrderPriceList(developOrderPriceList);
            }
            infoVoList.add(infoVo);
        });

        List<DevelopChildOrderAttrVo> developChildOrderAttrList = Optional.ofNullable(developChildOrderAttrPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(attrPo -> {
                    final DevelopChildOrderAttrVo developChildOrderAttrVo = new DevelopChildOrderAttrVo();
                    developChildOrderAttrVo.setAttributeNameId(attrPo.getAttributeNameId());
                    developChildOrderAttrVo.setAttrName(attrPo.getAttrName());
                    developChildOrderAttrVo.setAttrValue(attrPo.getAttrValue());
                    return developChildOrderAttrVo;
                }).collect(Collectors.toList());
        detailVo.setDevelopChildOrderAttrList(developChildOrderAttrList);

        detailVo.setDevelopPricingOrderInfoList(infoVoList);
        return detailVo;
    }

    public static DevelopPricingOrderDetailVo developPricingOrderPoToIsSampleVo(DevelopPricingOrderPo developPricingOrderPo,
                                                                                Map<String, List<DevelopPricingOrderInfoPo>> infoPricingMap,
                                                                                Map<String, List<DevelopPricingOrderMechanismPo>> mechanismPricingMap,
                                                                                Map<String, SupplierType> supplierTypeMap,
                                                                                DevelopChildOrderPo developChildOrderPo,
                                                                                List<DevelopChildOrderAttrPo> developChildOrderAttrPoList,
                                                                                List<DevelopOrderPricePo> developOrderPricePoList) {
        DevelopPricingOrderDetailVo detailVo = new DevelopPricingOrderDetailVo();
        detailVo.setDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
        detailVo.setDevelopPricingOrderStatus(developPricingOrderPo.getDevelopPricingOrderStatus());
        detailVo.setPlatform(developPricingOrderPo.getPlatform());
        detailVo.setSupplierCode(developPricingOrderPo.getSupplierCode());
        detailVo.setIsSample(developChildOrderPo.getIsSample());

        if (supplierTypeMap.containsKey(developPricingOrderPo.getSupplierCode())) {
            detailVo.setSupplierType(supplierTypeMap.get(developPricingOrderPo.getSupplierCode()));
        }

        List<DevelopPricingOrderInfoVo> infoVoList = new ArrayList<>();

        infoPricingMap.forEach((String developPricingOrderNo, List<DevelopPricingOrderInfoPo> infoPricingPoList) -> {
            //如果多条时只取一条
            DevelopPricingOrderInfoPo infoPo = infoPricingPoList.get(0);
            DevelopPricingOrderInfoVo infoVo = new DevelopPricingOrderInfoVo();
            if (supplierTypeMap.containsKey(developPricingOrderPo.getSupplierCode())) {
                infoVo.setSupplierType(supplierTypeMap.get(developPricingOrderPo.getSupplierCode()));
            }

            infoVo.setSamplePrice(infoPo.getSamplePrice());
            infoVo.setRemarks(infoPo.getRemarks());
            infoVo.setOrdinary(infoPo.getOrdinary());
            infoVo.setFrontSize(infoPo.getFrontSize());
            infoVo.setHandWeavingSize(infoPo.getHandWeavingSize());
            infoVo.setPrice(infoPo.getPrice());
            infoVo.setGramWeight(infoPo.getGramWeight());
            infoVo.setHandWeavingPrice(infoPo.getHandWeavingPrice());
            infoVo.setHandHookPrice(infoPo.getHandHookPrice());
            infoVo.setProductionPrice(infoPo.getProductionPrice());
            infoVo.setMeshCap(infoPo.getMeshCap());
            infoVo.setCurvaturePrice(infoPo.getCurvaturePrice());
            infoVo.setStainPrice(infoPo.getStainPrice());
            infoVo.setCost(infoPo.getCost());
            infoVo.setFactoryProfit(infoPo.getFactoryProfit());
            infoVo.setQuotedPrice(infoPo.getQuotedPrice());
            infoVo.setWeightedPrice(infoPo.getWeightedPrice());
            infoVo.setSecondPrice(infoPo.getSecondPrice());
            infoVo.setManagePrice(infoPo.getManagePrice());
            infoVo.setCostTotalPrice(infoPo.getCostTotalPrice());

            List<DevelopPricingOrderMechanismVo> mechanismVoList = new ArrayList<>();
            List<DevelopPricingOrderMechanismPo> mechanismPoList = mechanismPricingMap.get(developPricingOrderNo);
            if (CollectionUtils.isNotEmpty(mechanismPoList)) {
                for (DevelopPricingOrderMechanismPo mechanismPo : mechanismPoList) {
                    DevelopPricingOrderMechanismVo mechanismVo = new DevelopPricingOrderMechanismVo();
                    mechanismVo.setHairSize(mechanismPo.getHairSize());
                    mechanismVo.setGramWeight(mechanismPo.getGramWeight());
                    mechanismVo.setPrice(mechanismPo.getPrice());
                    mechanismVo.setHairPrice(mechanismPo.getHairPrice());
                    mechanismVoList.add(mechanismVo);
                }
            }
            infoVo.setDevelopPricingOrderMechanismList(mechanismVoList);

            // 无需打样获取关联渠道大货价格
            List<DevelopOrderPricePo> developOrderPricePoFilterList = developOrderPricePoList.stream()
                    .filter(developOrderPricePo -> DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                    .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developPricingOrderNo))
                    .collect(Collectors.toList());
            List<DevelopOrderPriceVo> developOrderPriceList = DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoFilterList);
            infoVo.setDevelopOrderPriceList(developOrderPriceList);

            infoVoList.add(infoVo);
        });
        detailVo.setDevelopPricingOrderInfoList(infoVoList);
        if (CollectionUtils.isEmpty(infoVoList) && BooleanType.FALSE.equals(developChildOrderPo.getIsSample())) {
            detailVo.setDevelopPricingOrderInfoList(List.of(new DevelopPricingOrderInfoVo()));
        }

        List<DevelopChildOrderAttrVo> developChildOrderAttrList = Optional.ofNullable(developChildOrderAttrPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(attrPo -> {
                    final DevelopChildOrderAttrVo developChildOrderAttrVo = new DevelopChildOrderAttrVo();
                    developChildOrderAttrVo.setAttributeNameId(attrPo.getAttributeNameId());
                    developChildOrderAttrVo.setAttrName(attrPo.getAttrName());
                    developChildOrderAttrVo.setAttrValue(attrPo.getAttrValue());
                    return developChildOrderAttrVo;
                }).collect(Collectors.toList());
        detailVo.setDevelopChildOrderAttrList(developChildOrderAttrList);

        return detailVo;
    }

    public static DevelopPricingOrderPo reviewOrderToPricingOrderPo(String developPricingOrderNo,
                                                                    DevelopChildOrderPo developChildOrderPo,
                                                                    DevelopReviewOrderPo developReviewOrderPo) {
        //生成核价单
        DevelopPricingOrderPo developPricingOrderPo = new DevelopPricingOrderPo();
        developPricingOrderPo.setDevelopPricingOrderNo(developPricingOrderNo);
        developPricingOrderPo.setDevelopParentOrderNo(developReviewOrderPo.getDevelopParentOrderNo());
        developPricingOrderPo.setDevelopChildOrderNo(developReviewOrderPo.getDevelopChildOrderNo());
        developPricingOrderPo.setDevelopPamphletOrderNo(developReviewOrderPo.getDevelopPamphletOrderNo());
        developPricingOrderPo.setDevelopReviewOrderNo(developReviewOrderPo.getDevelopReviewOrderNo());
        developPricingOrderPo.setSupplierCode(developReviewOrderPo.getSupplierCode());
        developPricingOrderPo.setDevelopPricingOrderStatus(DevelopPricingOrderStatus.WAIT_SUBMIT_PRICE);
        developPricingOrderPo.setPlatform(developReviewOrderPo.getPlatform());
        developPricingOrderPo.setSupplierCode(developReviewOrderPo.getSupplierCode());
        developPricingOrderPo.setSupplierName(developReviewOrderPo.getSupplierName());
        developPricingOrderPo.setParentCreateTime(developChildOrderPo.getParentCreateTime());
        developPricingOrderPo.setParentCreateUser(developChildOrderPo.getParentCreateUser());
        developPricingOrderPo.setParentCreateUsername(developChildOrderPo.getParentCreateUsername());
        return developPricingOrderPo;

    }

    public static List<PricingDevelopSampleOrderSearchVo> poListToSearchVoList(List<DevelopPricingOrderInfoPo> developPricingOrderInfoPoList,
                                                                               List<DevelopOrderPricePo> developOrderPricePoList) {
        return Optional.ofNullable(developPricingOrderInfoPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final PricingDevelopSampleOrderSearchVo pricingDevelopSampleOrderSearchVo = new PricingDevelopSampleOrderSearchVo();
                    pricingDevelopSampleOrderSearchVo.setDevelopSampleOrderNo(po.getDevelopSampleOrderNo());
                    pricingDevelopSampleOrderSearchVo.setSamplePrice(po.getSamplePrice());

                    // 获取开发子单渠道大货价格
                    List<DevelopOrderPricePo> developOrderPricePoFilterList;
                    // 核价单无需打样的渠道大货价格
                    if (StringUtils.isBlank(po.getDevelopSampleOrderNo())) {
                        developOrderPricePoFilterList = developOrderPricePoList.stream()
                                .filter(developOrderPricePo -> DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                                .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(po.getDevelopPricingOrderNo()))
                                .collect(Collectors.toList());
                    } else {
                        // 核价单需要打样的渠道大货价格
                        developOrderPricePoFilterList = developOrderPricePoList.stream()
                                .filter(developOrderPricePo -> DevelopOrderPriceType.PRICING_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                                .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(po.getDevelopSampleOrderNo()))
                                .collect(Collectors.toList());
                    }
                    List<DevelopOrderPriceVo> developOrderPriceList = DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoFilterList);
                    pricingDevelopSampleOrderSearchVo.setDevelopOrderPriceList(developOrderPriceList);
                    return pricingDevelopSampleOrderSearchVo;
                }).collect(Collectors.toList());
    }

}
