package com.hete.supply.scm.server.scm.develop.converter;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.plm.api.developorder.entity.vo.PlmDevelopChildOrderVo;
import com.hete.supply.plm.api.developorder.entity.vo.PlmDevelopOrderAttrVo;
import com.hete.supply.plm.api.developorder.entity.vo.PlmDevelopOrderDetailVo;
import com.hete.supply.plm.api.developorder.entity.vo.PlmDevelopOrderMaterialVo;
import com.hete.supply.plm.api.developorder.enums.ProofType;
import com.hete.supply.plm.api.developorder.enums.UrgentType;
import com.hete.supply.scm.api.scm.entity.enums.DevelopChildOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderType;
import com.hete.supply.scm.api.scm.entity.vo.DevelopReviewOrderExportVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopReviewAndSampleBo;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopChildOrderAttrDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopReviewCreateDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopReviewUnusualDto;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.*;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.develop.enums.DevelopReviewRelated;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.develop.converter.SupplierDevelopConverter;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.service.IdGenerateService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/8/9 16:50
 */
public class DevelopChildConverter {
    public static DevelopChildDetailVo convertDevelopChildPoToVo(DevelopChildOrderPo developChildOrderPo,
                                                                 DevelopChildOrderChangePo developChildOrderChangePo,
                                                                 List<DevelopChildOrderAttrPo> developChildOrderAttrPoList,
                                                                 DevelopPamphletOrderPo developPamphletOrderPo,
                                                                 List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList,
                                                                 Map<Long, List<String>> pamphletImageStyleMap,
                                                                 Map<Long, List<String>> pamphletImageColorMap,
                                                                 List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList,
                                                                 Map<String, PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoMap,
                                                                 Map<Long, List<String>> fileCodeMap,
                                                                 List<DevelopOrderPricePo> developOrderPricePoList,
                                                                 Map<String, String> skuEncodeMap) {
        final DevelopChildDetailVo developChildDetailVo = new DevelopChildDetailVo();
        developChildDetailVo.setDevelopChildOrderId(developChildOrderPo.getDevelopChildOrderId());
        developChildDetailVo.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        developChildDetailVo.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
        developChildDetailVo.setVersion(developChildOrderPo.getVersion());
        developChildDetailVo.setSku(developChildOrderPo.getSku());
        developChildDetailVo.setSkuEncode(skuEncodeMap.get(developChildOrderPo.getSku()));
        developChildDetailVo.setSpu(developChildOrderPo.getSpu());
        developChildDetailVo.setDevelopCreateType(developChildOrderPo.getDevelopCreateType());
        developChildDetailVo.setIsUrgent(developChildOrderPo.getIsUrgent());
        developChildDetailVo.setSupplierCode(developChildOrderPo.getSupplierCode());
        developChildDetailVo.setSupplierName(developChildOrderPo.getSupplierName());
        developChildDetailVo.setDevUser(developChildOrderChangePo.getDevUser());
        developChildDetailVo.setDevUsername(developChildOrderChangePo.getDevUsername());
        developChildDetailVo.setFollowUser(developChildOrderChangePo.getFollowUser());
        developChildDetailVo.setFollowUsername(developChildOrderChangePo.getFollowUsername());
        developChildDetailVo.setCreateTime(developChildOrderPo.getCreateTime());
        developChildDetailVo.setDevelopChildOrderStatus(developChildOrderPo.getDevelopChildOrderStatus());
        developChildDetailVo.setCancelReason(developChildOrderPo.getCancelReason());
        developChildDetailVo.setFileCodeList(fileCodeMap.get(developChildOrderPo.getDevelopChildOrderId()));

        //上架信息
        final DevelopOnShelvesMsgVo developOnShelvesMsgVo = new DevelopOnShelvesMsgVo();
        developOnShelvesMsgVo.setIsOnShelves(developChildOrderPo.getIsOnShelves());
        developOnShelvesMsgVo.setExpectedOnShelvesDate(developChildOrderPo.getExpectedOnShelvesDate());
        developOnShelvesMsgVo.setOnShelvesCompletionDate(developChildOrderChangePo.getOnShelvesCompletionDate());
        developOnShelvesMsgVo.setNewestCompletionDate(developChildOrderChangePo.getNewestCompletionDate());
        developChildDetailVo.setDevelopOnShelvesMsgVo(developOnShelvesMsgVo);

        //基础信息
        final DevelopChildBaseMsgVo developChildBaseMsgVo = new DevelopChildBaseMsgVo();
        developChildBaseMsgVo.setDevelopChildOrderId(developChildOrderPo.getDevelopChildOrderId());
        developChildBaseMsgVo.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        developChildBaseMsgVo.setCategory(developChildOrderPo.getCategory());
        developChildBaseMsgVo.setCategoryId(developChildOrderPo.getCategoryId());
        developChildBaseMsgVo.setSkuDevType(developChildOrderPo.getSkuDevType());
        developChildBaseMsgVo.setDevelopCreateType(developChildOrderPo.getDevelopCreateType());
        developChildBaseMsgVo.setHasException(developChildOrderPo.getHasException());
        developChildBaseMsgVo.setPlatform(developChildOrderPo.getPlatform());
        developChildBaseMsgVo.setPlatformId(developChildOrderPo.getPlatformId());
        developChildBaseMsgVo.setSkuEncode(developChildOrderPo.getSkuEncode());
        developChildBaseMsgVo.setSamplePrice(developChildOrderPo.getSamplePrice());
        List<DevelopChildOrderAttrVo> developChildOrderAttrList = convertDevChildAttrPoToVo(developChildOrderAttrPoList);
        developChildBaseMsgVo.setDevelopChildOrderAttrList(developChildOrderAttrList);
        developChildBaseMsgVo.setIsNeedRaw(developChildOrderPo.getIsNeedRaw());
        developChildBaseMsgVo.setIsSample(developChildOrderPo.getIsSample());
        developChildBaseMsgVo.setDevelopChildOrderStatus(developChildOrderPo.getDevelopChildOrderStatus());
        developChildBaseMsgVo.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
        developChildBaseMsgVo.setSku(developChildOrderPo.getSku());
        developChildBaseMsgVo.setSpu(developChildOrderPo.getSpu());
        developChildBaseMsgVo.setIsUrgent(developChildOrderPo.getIsUrgent());
        developChildBaseMsgVo.setPamphletTimes(developChildOrderPo.getPamphletTimes());

        //版单信息
        final DevelopPamphletMsgVo developPamphletMsgVo = new DevelopPamphletMsgVo();
        if (developPamphletOrderPo != null) {
            developPamphletMsgVo.setDevelopPamphletOrderId(developPamphletOrderPo.getDevelopPamphletOrderId());
            developPamphletMsgVo.setVersion(developPamphletOrderPo.getVersion());
            developPamphletMsgVo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
            developPamphletMsgVo.setDevelopPamphletOrderStatus(developPamphletOrderPo.getDevelopPamphletOrderStatus());
            developPamphletMsgVo.setDevelopSampleNum(developPamphletOrderPo.getDevelopSampleNum());
            developPamphletMsgVo.setProposedPrice(developPamphletOrderPo.getProposedPrice());
            developPamphletMsgVo.setExpectedOnShelvesDate(developPamphletOrderPo.getExpectedOnShelvesDate());
            developPamphletMsgVo.setDemandDesc(developPamphletOrderPo.getDemandDesc());
            developPamphletMsgVo.setRefuseReason(developPamphletOrderPo.getRefuseReason());
            developPamphletMsgVo.setSupplierSamplePrice(developPamphletOrderPo.getSamplePrice());
            List<DevelopPamphletOrderRawPo> pawPoList = developPamphletOrderRawPoList.stream()
                    .filter(w -> w.getDevelopPamphletOrderNo().equals(developPamphletOrderPo.getDevelopPamphletOrderNo()))
                    .collect(Collectors.toList());
            //获取原料
            if (CollectionUtils.isNotEmpty(pawPoList)) {
                List<DevelopPamphletRawDetailVo> developPamphletRawDetailVoList = DevelopPamphletOrderRawConverter.INSTANCE.convert(pawPoList);
                for (DevelopPamphletRawDetailVo developPamphletRawDetailVo : developPamphletRawDetailVoList) {
                    purchaseRawReceiptOrderItemPoList.stream()
                            .filter(w -> w.getSkuBatchCode().equals(developPamphletRawDetailVo.getSkuBatchCode()))
                            .findAny()
                            .ifPresent(purchaseRawReceiptOrderItemPo -> developPamphletRawDetailVo.setPurchaseRawDeliverOrderNo(purchaseRawReceiptOrderPoMap.get(purchaseRawReceiptOrderItemPo.getPurchaseRawReceiptOrderNo()).getPurchaseRawDeliverOrderNo()));
                }
                developPamphletMsgVo.setDevelopPamphletRawDetailVoList(developPamphletRawDetailVoList);
            }
            if (pamphletImageStyleMap.containsKey(developPamphletOrderPo.getDevelopPamphletOrderId())) {
                developPamphletMsgVo.setStyleReferenceFileCodeList(pamphletImageStyleMap.get(developPamphletOrderPo.getDevelopPamphletOrderId()));
            }
            if (pamphletImageColorMap.containsKey(developPamphletOrderPo.getDevelopPamphletOrderId())) {
                developPamphletMsgVo.setColorReferenceFileCodeList(pamphletImageColorMap.get(developPamphletOrderPo.getDevelopPamphletOrderId()));
            }
        }

        // 获取开发子单渠道大货价格
        developOrderPricePoList.stream()
                .filter(developOrderPricePo -> DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developChildOrderPo.getDevelopChildOrderNo()))
                .findFirst()
                .ifPresent(developOrderPricePoFilter ->
                        developChildBaseMsgVo.setDevelopOrderPrice(developOrderPricePoToVo(developOrderPricePoFilter)));

        developChildDetailVo.setDevelopChildBaseMsgVo(developChildBaseMsgVo);
        developChildDetailVo.setDevelopPamphletMsgVo(developPamphletMsgVo);


        return developChildDetailVo;
    }

    public static List<DevelopChildOrderAttrVo> convertDevChildAttrPoToVo(List<DevelopChildOrderAttrPo> developChildOrderAttrPoList) {
        return Optional.ofNullable(developChildOrderAttrPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(attrPo -> {
                    final DevelopChildOrderAttrVo developChildOrderAttrVo = new DevelopChildOrderAttrVo();
                    developChildOrderAttrVo.setAttributeNameId(attrPo.getAttributeNameId());
                    developChildOrderAttrVo.setAttrName(attrPo.getAttrName());
                    developChildOrderAttrVo.setAttrValue(attrPo.getAttrValue());
                    return developChildOrderAttrVo;
                }).collect(Collectors.toList());
    }

    public static DevelopChildDetailVo convertDevelopChildSamplePoToVo(DevelopChildDetailVo developChildDetailVo,
                                                                       List<DevelopSampleOrderPo> developSampleOrderPoList,
                                                                       Map<Long, List<String>> sampleImageSpecificationsMap,
                                                                       Map<Long, List<String>> sampleImageQuotationMap,
                                                                       Map<Long, List<String>> sampleImageMap,
                                                                       Map<String, List<DevelopSampleOrderRawPo>> developSampleOrderRawPoMap,
                                                                       Map<String, String> skuEncodeMap,
                                                                       Map<String, List<DevelopSampleOrderProcessPo>> developSampleOrderProcessPoMap,
                                                                       Map<String, List<DevelopSampleOrderProcessDescPo>> developSampleOrderProcessDescPoMap,
                                                                       Map<String, DevelopPricingOrderInfoPo> developPricingOrderInfoPoMap,
                                                                       Map<String, List<DevelopReviewSampleOrderPo>> developReviewSampleOrderPoMap,
                                                                       List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList,
                                                                       Map<Long, List<String>> developReviewSampleEffectMap,
                                                                       Map<Long, List<String>> developReviewSampleDetailMap,
                                                                       DevelopPricingOrderPo developPricingOrderPo,
                                                                       Map<String, List<DevelopSampleOrderPo>> developSampleOrderPoPricingMap,
                                                                       DevelopPamphletOrderPo developPamphletOrderPo,
                                                                       DevelopPrenatalFirstMsgVo developPrenatalFirstMsgVo,
                                                                       List<DevelopOrderPricePo> developOrderPricePoList) {
        List<DevelopSampleOrderDetailVo> developSampleOrderDetailVoList = new ArrayList<>();

        //无需打样逻辑处理
        if (BooleanType.FALSE.equals(developChildDetailVo.getDevelopChildBaseMsgVo().getIsSample())) {
            //产生默认数据
            developSampleOrderPoList = List.of(new DevelopSampleOrderPo());
        }

        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            DevelopSampleOrderDetailVo developSampleOrderDetailVo = new DevelopSampleOrderDetailVo();
            Long itemId = developSampleOrderPo.getDevelopSampleOrderId();
            String itemNo;
            String developPricingOrderNo = developSampleOrderPo.getDevelopPricingOrderNo();
            if (BooleanType.FALSE.equals(developChildDetailVo.getDevelopChildBaseMsgVo().getIsSample())) {
                itemId = developPamphletOrderPo.getDevelopPamphletOrderId();
                itemNo = developPamphletOrderPo.getDevelopPamphletOrderNo();
                //无需打样时核价单号+版单号查询，需要打样时根据不同样品关联核价单号+样品单号进行查询
                developPricingOrderNo = developPricingOrderPo.getDevelopPricingOrderNo();
            } else {
                itemNo = developSampleOrderPo.getDevelopSampleOrderNo();
            }
            developSampleOrderDetailVo.setVersion(developSampleOrderPo.getVersion());
            developSampleOrderDetailVo.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
            developSampleOrderDetailVo.setSku(developSampleOrderPo.getSku());
            developSampleOrderDetailVo.setSpu(developSampleOrderPo.getSpu());
            developSampleOrderDetailVo.setSkuBatchCode(developSampleOrderPo.getSkuBatchCode());
            developSampleOrderDetailVo.setSkuEncode(skuEncodeMap.get(developSampleOrderPo.getSku()));
            developSampleOrderDetailVo.setDevelopSampleStatus(developSampleOrderPo.getDevelopSampleStatus());
            developSampleOrderDetailVo.setDevelopSampleMethod(developSampleOrderPo.getDevelopSampleMethod());
            developSampleOrderDetailVo.setSupplierCode(developSampleOrderPo.getSupplierCode());
            developSampleOrderDetailVo.setSupplierName(developSampleOrderPo.getSupplierName());
            if (sampleImageSpecificationsMap.containsKey(itemId)) {
                developSampleOrderDetailVo.setSpecificationsFileCodeList(sampleImageSpecificationsMap.get(itemId));
            }
            if (sampleImageQuotationMap.containsKey(itemId)) {
                developSampleOrderDetailVo.setQuotationFileCodeList(sampleImageQuotationMap.get(itemId));
            }
            if (sampleImageMap.containsKey(itemId)) {
                developSampleOrderDetailVo.setFileCodeList(sampleImageMap.get(itemId));
            }
            if (BooleanType.FALSE.equals(developChildDetailVo.getDevelopChildBaseMsgVo().getIsSample())) {
                developSampleOrderDetailVo.setSupplierSamplePrice(developPamphletOrderPo.getSamplePrice());
            } else {
                developSampleOrderDetailVo.setSupplierSamplePrice(developSampleOrderPo.getSamplePrice());
            }

            developSampleOrderDetailVo.setProcessRemarks(developSampleOrderPo.getProcessRemarks());

            //原料
            List<DevelopSampleOrderRawPo> developSampleOrderRawPoList = developSampleOrderRawPoMap.get(itemNo);
            if (CollectionUtils.isNotEmpty(developSampleOrderRawPoList)) {
                List<DevelopSampleOrderRawVo> developPamphletRawDetailVoList = new ArrayList<>();
                for (DevelopSampleOrderRawPo developSampleOrderRawPo : developSampleOrderRawPoList) {
                    DevelopSampleOrderRawVo developSampleOrderRawVo = new DevelopSampleOrderRawVo();
                    developSampleOrderRawVo.setMaterialType(developSampleOrderRawPo.getMaterialType());
                    developSampleOrderRawVo.setSku(developSampleOrderRawPo.getSku());
                    developSampleOrderRawVo.setSkuEncode(skuEncodeMap.get(developSampleOrderRawPo.getSku()));
                    developSampleOrderRawVo.setSkuCnt(developSampleOrderRawPo.getSkuCnt());
                    developPamphletRawDetailVoList.add(developSampleOrderRawVo);
                }
                developSampleOrderDetailVo.setDevelopSampleOrderRawList(developPamphletRawDetailVoList);
            }
            //工序
            List<DevelopSampleOrderProcessPo> developSampleOrderProcessPoList = developSampleOrderProcessPoMap.get(itemNo);
            if (CollectionUtils.isNotEmpty(developSampleOrderProcessPoList)) {
                List<DevelopSampleOrderProcessVo> developSampleOrderProcessVoList = new ArrayList<>();
                for (DevelopSampleOrderProcessPo developSampleOrderProcessPo : developSampleOrderProcessPoList) {
                    DevelopSampleOrderProcessVo developSampleOrderProcessVo = new DevelopSampleOrderProcessVo();
                    developSampleOrderProcessVo.setProcessCode(developSampleOrderProcessPo.getProcessCode());
                    developSampleOrderProcessVo.setProcessName(developSampleOrderProcessPo.getProcessName());
                    developSampleOrderProcessVo.setProcessSecondCode(developSampleOrderProcessPo.getProcessSecondCode());
                    developSampleOrderProcessVo.setProcessSecondName(developSampleOrderProcessPo.getProcessSecondName());
                    developSampleOrderProcessVo.setProcessFirst(developSampleOrderProcessPo.getProcessFirst());
                    developSampleOrderProcessVo.setProcessLabel(developSampleOrderProcessPo.getProcessLabel());
                    developSampleOrderProcessVoList.add(developSampleOrderProcessVo);
                }
                developSampleOrderDetailVo.setDevelopSampleOrderProcessList(developSampleOrderProcessVoList);
            }

            //工序描述
            List<DevelopSampleOrderProcessDescPo> developSampleOrderProcessDescPoList = developSampleOrderProcessDescPoMap.get(itemNo);
            if (CollectionUtils.isNotEmpty(developSampleOrderProcessDescPoList)) {
                List<DevelopSampleOrderProcessDescVo> developSampleOrderProcessDescVoList = new ArrayList<>();
                for (DevelopSampleOrderProcessDescPo developSampleOrderProcessDescPo : developSampleOrderProcessDescPoList) {
                    DevelopSampleOrderProcessDescVo developSampleOrderProcessDescVo = new DevelopSampleOrderProcessDescVo();
                    developSampleOrderProcessDescVo.setName(developSampleOrderProcessDescPo.getName());
                    developSampleOrderProcessDescVo.setDescValue(developSampleOrderProcessDescPo.getDescValue());
                    developSampleOrderProcessDescVoList.add(developSampleOrderProcessDescVo);
                }
                developSampleOrderDetailVo.setDevelopSampleOrderProcessDescList(developSampleOrderProcessDescVoList);
            }

            //审版信息
            List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderPoMap.get(itemNo);
            DevelopChildReviewDetailVo developChildReviewDetailVo = new DevelopChildReviewDetailVo();
            if (CollectionUtils.isNotEmpty(developReviewSampleOrderPoList)) {
                DevelopReviewSampleOrderPo developReviewSampleOrderPo = developReviewSampleOrderPoList.get(0);
                List<DevelopReviewSampleOrderInfoPo> infoPoList = developReviewSampleOrderInfoPoList.stream()
                        .filter(w -> w.getDevelopReviewOrderNo().equals(developReviewSampleOrderPo.getDevelopReviewOrderNo())
                                && w.getDevelopSampleOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(infoPoList)) {
                    developChildReviewDetailVo.setDevelopReviewSampleInfoList(DevelopReviewSampleOrderInfoConverter.INSTANCE.convert(infoPoList));
                }
                developChildReviewDetailVo.setDevelopSampleDemand(developReviewSampleOrderPo.getDevelopSampleDemand());
                developChildReviewDetailVo.setDevelopSampleQuality(developReviewSampleOrderPo.getDevelopSampleQuality());
                developChildReviewDetailVo.setDevelopSampleMethod(developReviewSampleOrderPo.getDevelopSampleMethod());
                developChildReviewDetailVo.setGramWeight(developReviewSampleOrderPo.getGramWeight());
                developChildReviewDetailVo.setDevelopReviewOrderNo(developReviewSampleOrderPo.getDevelopReviewOrderNo());

                if (developReviewSampleEffectMap.containsKey(developReviewSampleOrderPo.getDevelopReviewSampleOrderId())) {
                    developChildReviewDetailVo.setEffectFileCodeList(developReviewSampleEffectMap.get(developReviewSampleOrderPo.getDevelopReviewSampleOrderId()));
                }
                if (developReviewSampleDetailMap.containsKey(developReviewSampleOrderPo.getDevelopReviewSampleOrderId())) {
                    developChildReviewDetailVo.setDetailFileCodeList(developReviewSampleDetailMap.get(developReviewSampleOrderPo.getDevelopReviewSampleOrderId()));
                }
            }
            developSampleOrderDetailVo.setDevelopChildReviewDetail(developChildReviewDetailVo);

            //当前样品单核价信息
            DevelopPricingOrderInfoPo developPricingOrderInfoPo = developPricingOrderInfoPoMap.get(developPricingOrderNo + "-" + itemNo);
            DevelopChildReviewInfoVo developChildReviewInfoVo = new DevelopChildReviewInfoVo();
            if (developPricingOrderInfoPo != null) {
                developChildReviewInfoVo.setDevelopPricingOrderNo(developPricingOrderInfoPo.getDevelopPricingOrderNo());
                developChildReviewInfoVo.setSamplePrice(developPricingOrderInfoPo.getSamplePrice());
                developChildReviewInfoVo.setRemarks(developPricingOrderInfoPo.getRemarks());
                // 获取核价单的渠道大货价格
                List<DevelopOrderPricePo> developOrderPricePoFilterPricingList;
                if (BooleanType.FALSE.equals(developChildDetailVo.getDevelopChildBaseMsgVo().getIsSample())) {
                    // 无需打样核价单的大货价格
                    developOrderPricePoFilterPricingList = developOrderPricePoList.stream()
                            .filter(developOrderPricePo -> DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                            .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developPricingOrderInfoPo.getDevelopPricingOrderNo()))
                            .collect(Collectors.toList());
                } else {
                    // 需打样核价单的大货价格
                    developOrderPricePoFilterPricingList = developOrderPricePoList.stream()
                            .filter(developOrderPricePo -> DevelopOrderPriceType.PRICING_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                            .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developPricingOrderInfoPo.getDevelopSampleOrderNo()))
                            .collect(Collectors.toList());
                }
                List<DevelopOrderPriceVo> developOrderPriceList = developOrderPricePoToVoList(developOrderPricePoFilterPricingList);
                developChildReviewInfoVo.setDevelopOrderPriceList(developOrderPriceList);
            }
            developSampleOrderDetailVo.setDevelopChildReviewInfo(developChildReviewInfoVo);
            developSampleOrderDetailVoList.add(developSampleOrderDetailVo);

            // 获取样品单供应商报价大货价格
            List<DevelopOrderPricePo> developOrderPricePoFilterList = developOrderPricePoList.stream()
                    .filter(developOrderPricePo -> DevelopOrderPriceType.SUPPLIER_SAMPLE_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                    .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(itemNo))
                    .collect(Collectors.toList());
            List<DevelopOrderPriceVo> developOrderPriceList = developOrderPricePoToVoList(developOrderPricePoFilterList);
            developSampleOrderDetailVo.setDevelopOrderPriceList(developOrderPriceList);
        }
        developChildDetailVo.setDevelopSampleOrderDetailList(developSampleOrderDetailVoList);

        DevelopPricingMsgVo developPricingMsgVo = new DevelopPricingMsgVo();
        if (developPricingOrderPo != null) {
            developPricingMsgVo.setDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
            developPricingMsgVo.setDevelopPricingOrderStatus(developPricingOrderPo.getDevelopPricingOrderStatus());
            List<DevelopSampleOrderPo> developSampleOrderPoPricingList = developSampleOrderPoPricingMap.get(developPricingOrderPo.getDevelopPricingOrderNo());
            developPricingMsgVo.setDevelopPricingMsgSampleList(DevelopSampleOrderConverter.INSTANCE.developVoList(developSampleOrderPoPricingList));
        }

        developChildDetailVo.setDevelopPricingMsgVo(developPricingMsgVo);
        developChildDetailVo.setDevelopPrenatalFirstMsgVo(developPrenatalFirstMsgVo);

        return developChildDetailVo;
    }

    public static List<DevelopChildOrderAttrPo> convertDevelopAttrDtoToPo(DevelopChildOrderPo developChildOrderPo,
                                                                          List<DevelopChildOrderAttrDto> developChildOrderAttrList) {
        if (CollectionUtils.isEmpty(developChildOrderAttrList)) {
            return Collections.emptyList();
        }
        return developChildOrderAttrList.stream()
                .map(dto -> {
                    final DevelopChildOrderAttrPo developChildOrderAttrPo = new DevelopChildOrderAttrPo();
                    developChildOrderAttrPo.setAttributeNameId(dto.getAttributeNameId());
                    developChildOrderAttrPo.setAttrName(dto.getAttrName());
                    developChildOrderAttrPo.setAttrValue(dto.getAttrValue());
                    developChildOrderAttrPo.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
                    developChildOrderAttrPo.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());

                    return developChildOrderAttrPo;
                }).collect(Collectors.toList());

    }

    public static List<DevelopChildOrderPo> convertPlmDevelopChildVoToPo(PlmDevelopOrderDetailVo vo, Map<String, String> supplierCodeMap) {
        if (CollectionUtils.isEmpty(vo.getPlmDevelopChildOrderVoList())) {
            return Collections.emptyList();
        }
        return vo.getPlmDevelopChildOrderVoList().stream()
                .map(item -> {
                    final DevelopChildOrderPo po = new DevelopChildOrderPo();
                    po.setDevelopChildOrderNo(item.getDevelopChildOrderNo());
                    po.setDevelopParentOrderNo(vo.getDevelopParentOrderNo());
                    po.setCategory(vo.getCategory());
                    po.setCategoryId(vo.getCategoryId());
                    po.setSpu(vo.getSpuCode());
                    po.setSku(item.getSkuCode());
                    po.setDevelopCreateType(vo.getDevelopCreateType());
                    po.setPamphletTimes(ScmConstant.DEVELOP_PAMPHLET_TIMES);

                    //是否加急
                    if (UrgentType.TRUE.equals(vo.getUrgentType())) {
                        po.setIsUrgent(BooleanType.TRUE);
                    } else {
                        po.setIsUrgent(BooleanType.FALSE);
                    }

                    //是否打样
                    if (ProofType.TRUE.equals(item.getProofType())) {
                        po.setIsSample(BooleanType.TRUE);
                        po.setDevelopChildOrderStatus(DevelopChildOrderStatus.PAMPHLET);
                    } else {
                        po.setIsSample(BooleanType.FALSE);
                        po.setDevelopChildOrderStatus(DevelopChildOrderStatus.PRICING);
                    }

                    po.setSupplierCode(item.getSupplierCode());
                    po.setSupplierName(item.getSupplierName());
                    po.setExpectedOnShelvesDate(vo.getExpectedOnShelvesDate());
                    po.setSkuDevType(vo.getSkuDevType());
                    po.setPlatform(vo.getPlatCode());
                    po.setPlatformId(vo.getGoodsPlatSpuId());
                    po.setExpectedArrivalDate(vo.getExpectedArrivalDate());
                    po.setExpectedPurchaseCost(vo.getExpectedPurchaseCost());
                    po.setDemandDesc(vo.getDemandDesc());
                    po.setParentCreateTime(vo.getOrderTime());
                    po.setParentCreateUser(vo.getOrderUserCode());
                    po.setParentCreateUsername(vo.getOrderUserName());
                    po.setIsOnShelves(BooleanType.FALSE);
                    po.setSupplierCode(item.getSupplierCode());
                    po.setSupplierName(supplierCodeMap.get(item.getSupplierCode()));
                    po.setProofCnt(item.getProofCnt());
                    po.setHasFirstOrder(BooleanType.FALSE);
                    if (CollectionUtils.isNotEmpty(item.getPlmDevelopOrderMaterialVoList())) {
                        po.setIsNeedRaw(BooleanType.TRUE);
                    } else {
                        po.setIsNeedRaw(BooleanType.FALSE);
                    }
                    return po;
                }).collect(Collectors.toList());
    }

    public static List<DevelopChildOrderChangePo> convertPoToChangePo(List<DevelopChildOrderPo> list) {
        return Optional.ofNullable(list)
                .orElse(Collections.emptyList())
                .stream()
                .map(item -> {
                    final DevelopChildOrderChangePo developChildOrderChangePo = new DevelopChildOrderChangePo();
                    developChildOrderChangePo.setDevelopChildOrderNo(item.getDevelopChildOrderNo());
                    developChildOrderChangePo.setDevelopParentOrderNo(item.getDevelopParentOrderNo());
                    developChildOrderChangePo.setDevUser(item.getParentCreateUser());
                    developChildOrderChangePo.setDevUsername(item.getParentCreateUsername());
                    return developChildOrderChangePo;
                }).collect(Collectors.toList());
    }

    public static List<DevelopChildOrderRawPo> convertPoToRawPo(PlmDevelopOrderDetailVo vo) {
        if (CollectionUtils.isEmpty(vo.getPlmDevelopChildOrderVoList())) {
            return Collections.emptyList();
        }
        List<DevelopChildOrderRawPo> list = new ArrayList<>();
        for (PlmDevelopChildOrderVo childOrderVo : vo.getPlmDevelopChildOrderVoList()) {
            for (PlmDevelopOrderMaterialVo materialVo : childOrderVo.getPlmDevelopOrderMaterialVoList()) {
                DevelopChildOrderRawPo developChildOrderRawPo = new DevelopChildOrderRawPo();
                developChildOrderRawPo.setDevelopChildOrderNo(childOrderVo.getDevelopChildOrderNo());
                developChildOrderRawPo.setDevelopParentOrderNo(vo.getDevelopParentOrderNo());
                developChildOrderRawPo.setMaterialType(materialVo.getMaterialType());
                developChildOrderRawPo.setSku(materialVo.getSkuCode());
                developChildOrderRawPo.setSkuCnt(materialVo.getSkuCnt());
                list.add(developChildOrderRawPo);
            }
        }
        return list;
    }

    public static List<DevelopChildOrderAttrPo> convertPoToAttrPo(PlmDevelopOrderDetailVo vo) {
        if (CollectionUtils.isEmpty(vo.getPlmDevelopChildOrderVoList())) {
            return Collections.emptyList();
        }
        List<DevelopChildOrderAttrPo> list = new ArrayList<>();
        for (PlmDevelopChildOrderVo childOrderVo : vo.getPlmDevelopChildOrderVoList()) {
            for (PlmDevelopOrderAttrVo attrVo : childOrderVo.getPlmDevelopOrderAttrVoList()) {
                DevelopChildOrderAttrPo developChildOrderAttrPo = new DevelopChildOrderAttrPo();
                developChildOrderAttrPo.setDevelopChildOrderNo(childOrderVo.getDevelopChildOrderNo());
                developChildOrderAttrPo.setDevelopParentOrderNo(vo.getDevelopParentOrderNo());
                developChildOrderAttrPo.setAttributeNameId(attrVo.getAttributeNameId());
                developChildOrderAttrPo.setAttrName(attrVo.getAttrName());
                developChildOrderAttrPo.setAttrValue(attrVo.getAttrValue());
                list.add(developChildOrderAttrPo);
            }
        }
        return list;
    }

    public static List<DevelopReviewSampleOrderPo> convertSamplePoToReviewSamplePo(List<DevelopSampleOrderPo> developSampleOrderPoList,
                                                                                   DevelopReviewOrderPo developReviewOrderPo) {
        return Optional.ofNullable(developSampleOrderPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final DevelopReviewSampleOrderPo developReviewSampleOrderPo = new DevelopReviewSampleOrderPo();
                    developReviewSampleOrderPo.setDevelopChildOrderNo(developReviewOrderPo.getDevelopChildOrderNo());
                    developReviewSampleOrderPo.setDevelopParentOrderNo(developReviewOrderPo.getDevelopParentOrderNo());
                    developReviewSampleOrderPo.setDevelopReviewOrderNo(developReviewOrderPo.getDevelopReviewOrderNo());
                    developReviewSampleOrderPo.setDevelopSampleOrderNo(po.getDevelopSampleOrderNo());

                    return developReviewSampleOrderPo;
                }).collect(Collectors.toList());
    }

    public static DevelopReviewAndSampleBo reviewCreateDtoToPo(DevelopReviewCreateDto dto,
                                                               String developReviewOrderNo,
                                                               SupplierPo supplierPo,
                                                               Map<String, DevelopSampleOrderPo> developSampleOrderPoMap) {
        final DevelopReviewOrderPo developReviewOrderPo = new DevelopReviewOrderPo();
        developReviewOrderPo.setPrenatalSampleOrderNo(dto.getPrenatalSampleOrderNo());
        developReviewOrderPo.setPlatform(dto.getPlatform());
        developReviewOrderPo.setSpu(dto.getSpu());
        developReviewOrderPo.setCategoryId(dto.getCategoryId());
        developReviewOrderPo.setCategory(dto.getCategory());
        developReviewOrderPo.setDevelopReviewOrderNo(developReviewOrderNo);
        developReviewOrderPo.setDevelopReviewOrderStatus(DevelopReviewOrderStatus.TO_BE_SUBMITTED_REVIEW);
        developReviewOrderPo.setDevelopReviewOrderType(DevelopReviewOrderType.PRENATAL_REVIEW);
        developReviewOrderPo.setDevelopReviewRelated(dto.getDevelopReviewRelated());
        if (null != supplierPo) {
            developReviewOrderPo.setSupplierCode(supplierPo.getSupplierCode());
            developReviewOrderPo.setSupplierName(supplierPo.getSupplierName());
        }

        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = dto.getDevelopReviewCreateItemList()
                .stream()
                .map(itemDto -> {
                    final DevelopReviewSampleOrderPo developReviewSampleOrderPo = new DevelopReviewSampleOrderPo();
                    developReviewSampleOrderPo.setDevelopReviewOrderNo(developReviewOrderNo);
                    developReviewSampleOrderPo.setDevelopSampleOrderNo(itemDto.getDevelopSampleOrderNo());
                    developReviewSampleOrderPo.setGramWeight(itemDto.getGramWeight());
                    developReviewSampleOrderPo.setDevelopSampleDemand(itemDto.getDevelopSampleDemand());
                    developReviewSampleOrderPo.setDevelopSampleQuality(itemDto.getDevelopSampleQuality());
                    developReviewSampleOrderPo.setDevelopSampleMethod(itemDto.getDevelopSampleMethod());
                    developReviewSampleOrderPo.setDevelopSampleNewness(itemDto.getDevelopSampleNewness());
                    developReviewSampleOrderPo.setDevelopSampleStage(itemDto.getDevelopSampleStage());
                    developReviewSampleOrderPo.setDevelopReviewSampleSource(itemDto.getDevelopReviewSampleSource());
                    developReviewSampleOrderPo.setDevelopSampleDevOpinion(itemDto.getDevelopSampleDevOpinion());
                    developReviewSampleOrderPo.setDevelopSampleQltyOpinion(itemDto.getDevelopSampleQltyOpinion());
                    developReviewSampleOrderPo.setAbnormalHair(itemDto.getAbnormalHair());
                    developReviewSampleOrderPo.setFloatingHair(itemDto.getFloatingHair());
                    developReviewSampleOrderPo.setMeshCapFit(itemDto.getMeshCapFit());
                    developReviewSampleOrderPo.setHairFeel(itemDto.getHairFeel());
                    DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoMap.get(itemDto.getDevelopSampleOrderNo());
                    if (null != developSampleOrderPo) {
                        developReviewSampleOrderPo.setDevelopParentOrderNo(developSampleOrderPo.getDevelopParentOrderNo());
                        developReviewSampleOrderPo.setDevelopChildOrderNo(developSampleOrderPo.getDevelopChildOrderNo());
                    }
                    return developReviewSampleOrderPo;
                }).collect(Collectors.toList());

        final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = dto.getDevelopReviewSampleInfoList().stream()
                .map(itemInfoDto -> {
                    final DevelopReviewSampleOrderInfoPo developReviewSampleOrderInfoPo = new DevelopReviewSampleOrderInfoPo();
                    developReviewSampleOrderInfoPo.setDevelopSampleOrderNo(itemInfoDto.getDevelopSampleOrderNo());
                    developReviewSampleOrderInfoPo.setAttributeNameId(itemInfoDto.getAttributeNameId());
                    developReviewSampleOrderInfoPo.setSampleInfoKey(itemInfoDto.getSampleInfoKey());
                    developReviewSampleOrderInfoPo.setSampleInfoValue(itemInfoDto.getSampleInfoValue());
                    developReviewSampleOrderInfoPo.setEvaluationOpinion(itemInfoDto.getEvaluationOpinion());
                    return developReviewSampleOrderInfoPo;
                }).collect(Collectors.toList());


        return DevelopReviewAndSampleBo.builder()
                .developReviewOrderPo(developReviewOrderPo)
                .developReviewSampleOrderPoList(developReviewSampleOrderPoList)
                .developReviewSampleOrderInfoPoList(developReviewSampleOrderInfoPoList)
                .build();
    }

    public static void reviewUnusualDtoToPo(DevelopReviewUnusualDto dto, DevelopReviewOrderUnusualPo developReviewOrderUnusualPo) {

        developReviewOrderUnusualPo.setPhenomena(dto.getPhenomena());
        developReviewOrderUnusualPo.setQualityAnalysis(dto.getQualityAnalysis());
        developReviewOrderUnusualPo.setQualityAnalysisUser(dto.getQualityAnalysisUser());
        developReviewOrderUnusualPo.setQualityAnalysisUsername(dto.getQualityAnalysisUsername());
        developReviewOrderUnusualPo.setQualityAnalysisDate(dto.getQualityAnalysisDate());
        developReviewOrderUnusualPo.setDemandAnalysis(dto.getDemandAnalysis());
        developReviewOrderUnusualPo.setDemandAnalysisUser(dto.getDemandAnalysisUser());
        developReviewOrderUnusualPo.setDemandAnalysisUsername(dto.getDemandAnalysisUsername());
        developReviewOrderUnusualPo.setDemandAnalysisDate(dto.getDemandAnalysisDate());
        developReviewOrderUnusualPo.setImprove(dto.getImprove());
        developReviewOrderUnusualPo.setImproveUser(dto.getImproveUser());
        developReviewOrderUnusualPo.setImproveUsername(dto.getImproveUsername());
        developReviewOrderUnusualPo.setImproveDate(dto.getImproveDate());
        developReviewOrderUnusualPo.setValidation(dto.getValidation());
        developReviewOrderUnusualPo.setValidationUser(dto.getValidationUser());
        developReviewOrderUnusualPo.setValidationUsername(dto.getValidationUsername());
        developReviewOrderUnusualPo.setValidationDate(dto.getValidationDate());
    }

    public static DevelopReviewUnusualVo reviewUnusualPoToVo(DevelopReviewOrderPo developReviewOrderPo,
                                                             DevelopReviewOrderUnusualPo developReviewOrderUnusualPo,
                                                             DevelopChildOrderPo developChildOrderPo,
                                                             DevelopChildOrderChangePo developChildOrderChangePo,
                                                             List<DevelopChildOrderAttrPo> developChildOrderAttrPoList,
                                                             List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList,
                                                             DevelopReviewSampleOrderPo developReviewSampleOrderPo) {
        final DevelopReviewUnusualVo developReviewUnusualVo = new DevelopReviewUnusualVo();
        developReviewUnusualVo.setSupplierCode(developReviewOrderPo.getSupplierCode());
        developReviewUnusualVo.setCategory(developReviewOrderPo.getCategory());
        developReviewUnusualVo.setPamphletTimes(developReviewOrderPo.getPamphletTimes());
        developReviewUnusualVo.setReviewUser(developReviewOrderPo.getReviewUser());
        developReviewUnusualVo.setReviewUsername(developReviewOrderPo.getReviewUsername());
        developReviewUnusualVo.setDevelopSampleNum(developReviewOrderPo.getDevelopSampleNum());
        if (null != developChildOrderChangePo) {
            developReviewUnusualVo.setDevUser(developChildOrderChangePo.getDevUser());
            developReviewUnusualVo.setDevUsername(developChildOrderChangePo.getDevUsername());
        }
        developReviewUnusualVo.setReviewDate(developReviewOrderPo.getReviewDate());
        developReviewUnusualVo.setPoorAmount(developReviewOrderPo.getPoorAmount());
        developReviewUnusualVo.setSpu(developReviewOrderPo.getSpu());
        if (null != developChildOrderPo) {
            developReviewUnusualVo.setSku(developChildOrderPo.getSku());
            developReviewUnusualVo.setSkuEncode(developChildOrderPo.getSkuEncode());
        }
        if (CollectionUtils.isNotEmpty(developChildOrderAttrPoList)) {
            final List<DevelopChildOrderAttrVo> developChildOrderAttrVoList = convertDevChildAttrPoToVo(developChildOrderAttrPoList);
            developReviewUnusualVo.setDevelopChildOrderAttrVoList(developChildOrderAttrVoList);
        }
        final List<DevelopReviewSampleInfoVo> developReviewSampleInfoList = SupplierDevelopConverter.convertReviewSampleInfoPoToVo(developReviewSampleOrderInfoPoList);
        developReviewUnusualVo.setDevelopReviewSampleInfoList(developReviewSampleInfoList);
        developReviewUnusualVo.setDevelopSampleOrderNo(developReviewSampleOrderPo.getDevelopSampleOrderNo());
        developReviewUnusualVo.setDevelopSampleNewness(developReviewSampleOrderPo.getDevelopSampleNewness());
        developReviewUnusualVo.setDevelopSampleStage(developReviewSampleOrderPo.getDevelopSampleStage());
        developReviewUnusualVo.setDevelopReviewSampleSource(developReviewSampleOrderPo.getDevelopReviewSampleSource());
        developReviewUnusualVo.setDevelopSampleMethod(developReviewSampleOrderPo.getDevelopSampleMethod());
        developReviewUnusualVo.setDevelopSampleQuality(developReviewSampleOrderPo.getDevelopSampleQuality());
        developReviewUnusualVo.setDevelopSampleDemand(developReviewSampleOrderPo.getDevelopSampleDemand());

        if (null != developReviewOrderUnusualPo) {
            developReviewUnusualVo.setDevelopReviewOrderUnusualNo(developReviewOrderUnusualPo.getDevelopReviewOrderUnusualNo());
            developReviewUnusualVo.setPhenomena(developReviewOrderUnusualPo.getPhenomena());
            developReviewUnusualVo.setQualityAnalysis(developReviewOrderUnusualPo.getQualityAnalysis());
            developReviewUnusualVo.setQualityAnalysisUser(developReviewOrderUnusualPo.getQualityAnalysisUser());
            developReviewUnusualVo.setQualityAnalysisUsername(developReviewOrderUnusualPo.getQualityAnalysisUsername());
            developReviewUnusualVo.setQualityAnalysisDate(developReviewOrderUnusualPo.getQualityAnalysisDate());
            developReviewUnusualVo.setDemandAnalysis(developReviewOrderUnusualPo.getDemandAnalysis());
            developReviewUnusualVo.setDemandAnalysisUser(developReviewOrderUnusualPo.getDemandAnalysisUser());
            developReviewUnusualVo.setDemandAnalysisUsername(developReviewOrderUnusualPo.getDemandAnalysisUsername());
            developReviewUnusualVo.setDemandAnalysisDate(developReviewOrderUnusualPo.getDemandAnalysisDate());
            developReviewUnusualVo.setImprove(developReviewOrderUnusualPo.getImprove());
            developReviewUnusualVo.setImproveUser(developReviewOrderUnusualPo.getImproveUser());
            developReviewUnusualVo.setImproveUsername(developReviewOrderUnusualPo.getImproveUsername());
            developReviewUnusualVo.setImproveDate(developReviewOrderUnusualPo.getImproveDate());
            developReviewUnusualVo.setValidation(developReviewOrderUnusualPo.getValidation());
            developReviewUnusualVo.setValidationUser(developReviewOrderUnusualPo.getValidationUser());
            developReviewUnusualVo.setValidationUsername(developReviewOrderUnusualPo.getValidationUsername());
            developReviewUnusualVo.setValidationDate(developReviewOrderUnusualPo.getValidationDate());
        }


        return developReviewUnusualVo;
    }

    public static List<DevelopReviewOrderExportVo> convertDevSamplePoToExportVo(List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList,
                                                                                Map<String, DevelopReviewSearchVo> developReviewSearchNoVoMap,
                                                                                Map<String, String> devReviewSampleNoInfoMap,
                                                                                Map<String, String> platCodeNameMap) {
        return Optional.ofNullable(developReviewSampleOrderPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final DevelopReviewSearchVo developReviewSearchVo = developReviewSearchNoVoMap.get(po.getDevelopReviewOrderNo());
                    final DevelopReviewOrderExportVo developReviewOrderExportVo = new DevelopReviewOrderExportVo();
                    developReviewOrderExportVo.setDevelopReviewOrderNo(po.getDevelopReviewOrderNo());
                    developReviewOrderExportVo.setDevelopReviewOrderTypeStr(developReviewSearchVo.getDevelopReviewOrderType().getRemark());
                    developReviewOrderExportVo.setSpu(developReviewSearchVo.getSpu());
                    developReviewOrderExportVo.setDevelopChildOrderNo(po.getDevelopChildOrderNo());
                    developReviewOrderExportVo.setPamphletTimes(developReviewSearchVo.getPamphletTimes());
                    developReviewOrderExportVo.setDevelopParentOrderNo(developReviewSearchVo.getDevelopParentOrderNo());
                    developReviewOrderExportVo.setDevelopSampleNum(developReviewSearchVo.getDevelopSampleNum());
                    developReviewOrderExportVo.setPlatform(platCodeNameMap.get(developReviewSearchVo.getPlatform()));
                    developReviewOrderExportVo.setSupplierCode(developReviewSearchVo.getSupplierCode());
                    developReviewOrderExportVo.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(po.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    developReviewOrderExportVo.setCreateUsername(po.getCreateUsername());
                    developReviewOrderExportVo.setReviewUsername(developReviewSearchVo.getReviewUsername());
                    developReviewOrderExportVo.setReviewDateStr(ScmTimeUtil.localDateTimeToStr(developReviewSearchVo.getReviewDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    if (null != developReviewSearchVo.getReviewResult()) {
                        developReviewOrderExportVo.setReviewResultStr(developReviewSearchVo.getReviewResult().getRemark());
                    }
                    developReviewOrderExportVo.setDevelopSampleOrderNo(po.getDevelopSampleOrderNo());
                    if (null != po.getDevelopSampleMethod()) {
                        developReviewOrderExportVo.setDevelopSampleMethodStr(po.getDevelopSampleMethod().getRemark());
                    }
                    if (null != po.getDevelopSampleDemand()) {
                        developReviewOrderExportVo.setDevelopSampleDemandStr(po.getDevelopSampleDemand().getRemark());
                    }
                    if (null != po.getDevelopSampleNewness()) {
                        developReviewOrderExportVo.setDevelopSampleNewnessStr(po.getDevelopSampleNewness().getRemark());
                    }
                    if (null != po.getDevelopSampleStage()) {
                        developReviewOrderExportVo.setDevelopSampleStageStr(po.getDevelopSampleStage().getRemark());
                    }
                    developReviewOrderExportVo.setDevelopSampleDevOpinion(po.getDevelopSampleDevOpinion());
                    if (null != po.getDevelopReviewSampleSource()) {
                        developReviewOrderExportVo.setDevelopReviewSampleSourceStr(po.getDevelopReviewSampleSource().getRemark());
                    }
                    if (null != po.getDevelopSampleQuality()) {
                        developReviewOrderExportVo.setDevelopSampleQualityStr(po.getDevelopSampleQuality().getRemark());
                    }
                    developReviewOrderExportVo.setDevelopSampleQltyOpinion(po.getDevelopSampleQltyOpinion());
                    developReviewOrderExportVo.setAbnormalHair(po.getAbnormalHair());
                    developReviewOrderExportVo.setMeshCapFit(po.getMeshCapFit());
                    developReviewOrderExportVo.setHairFeel(po.getHairFeel());
                    developReviewOrderExportVo.setFloatingHair(po.getFloatingHair());
                    developReviewOrderExportVo.setSampleInfoStr(devReviewSampleNoInfoMap.get(po.getDevelopSampleOrderNo()));

                    return developReviewOrderExportVo;
                }).collect(Collectors.toList());
    }

    public static DevelopPrenatalFirstMsgVo convertPurchasePoToPrenatalFirstVo(PurchaseChildOrderPo prenatalPurchaseChildOrderPo,
                                                                               PurchaseChildOrderPo firstPurchaseChildOrderPo,
                                                                               String purchaseParentOrderNo,
                                                                               DevelopChildBaseMsgVo developChildBaseMsgVo) {
        final DevelopPrenatalFirstMsgVo developPrenatalFirstMsgVo = new DevelopPrenatalFirstMsgVo();

        if (null != prenatalPurchaseChildOrderPo) {
            developPrenatalFirstMsgVo.setPrenatalSampleOrderNo(prenatalPurchaseChildOrderPo.getPurchaseChildOrderNo());
            developPrenatalFirstMsgVo.setPrenatalPurchaseOrderStatus(prenatalPurchaseChildOrderPo.getPurchaseOrderStatus());
            developPrenatalFirstMsgVo.setPrenatalPurchaseParentOrderNo(prenatalPurchaseChildOrderPo.getPurchaseParentOrderNo());
        }
        if (null != firstPurchaseChildOrderPo) {
            developPrenatalFirstMsgVo.setFirstSampleOrderNo(firstPurchaseChildOrderPo.getPurchaseChildOrderNo());
            developPrenatalFirstMsgVo.setFirstPurchaseOrderStatus(firstPurchaseChildOrderPo.getPurchaseOrderStatus());
            developPrenatalFirstMsgVo.setFirstPurchaseParentOrderNo(firstPurchaseChildOrderPo.getPurchaseParentOrderNo());
        }

        if (StringUtils.isNotBlank(purchaseParentOrderNo)) {
            developPrenatalFirstMsgVo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        }

        // 判断是否可以继续下单
        developPrenatalFirstMsgVo.setSupportOrder(BooleanType.TRUE);
        // 以下情况不能下单
        if (StringUtils.isNotBlank(purchaseParentOrderNo)) {
            developPrenatalFirstMsgVo.setSupportOrder(BooleanType.FALSE);
        } else {
            if (null != prenatalPurchaseChildOrderPo && null != firstPurchaseChildOrderPo) {
                developPrenatalFirstMsgVo.setSupportOrder(BooleanType.FALSE);
            }
        }
        if (!DevelopChildOrderStatus.COMPLETE.equals(developChildBaseMsgVo.getDevelopChildOrderStatus())) {
            developPrenatalFirstMsgVo.setSupportOrder(BooleanType.FALSE);
        }
        if (StringUtils.isBlank(developChildBaseMsgVo.getSpu())) {
            developPrenatalFirstMsgVo.setSupportOrder(BooleanType.FALSE);
        }
        if (StringUtils.isBlank(developChildBaseMsgVo.getSku())) {
            developPrenatalFirstMsgVo.setSupportOrder(BooleanType.FALSE);
        }


        return developPrenatalFirstMsgVo;

    }

    public static List<DevelopReviewSampleOrderInfoPo> attrPoListToDevReviewInfoList(List<DevelopChildOrderAttrPo> developChildOrderAttrPoList,
                                                                                     List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList) {
        if (CollectionUtils.isEmpty(developReviewSampleOrderPoList)) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(developChildOrderAttrPoList)) {
            return Collections.emptyList();
        }
        List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = new ArrayList<>();
        for (DevelopReviewSampleOrderPo developReviewSampleOrderPo : developReviewSampleOrderPoList) {
            for (DevelopChildOrderAttrPo developChildOrderAttrPo : developChildOrderAttrPoList) {
                final DevelopReviewSampleOrderInfoPo developReviewSampleOrderInfoPo = new DevelopReviewSampleOrderInfoPo();
                developReviewSampleOrderInfoPo.setDevelopChildOrderNo(developReviewSampleOrderPo.getDevelopChildOrderNo());
                developReviewSampleOrderInfoPo.setDevelopParentOrderNo(developReviewSampleOrderPo.getDevelopParentOrderNo());
                developReviewSampleOrderInfoPo.setDevelopReviewOrderNo(developReviewSampleOrderPo.getDevelopReviewOrderNo());
                developReviewSampleOrderInfoPo.setDevelopSampleOrderNo(developReviewSampleOrderPo.getDevelopSampleOrderNo());
                developReviewSampleOrderInfoPo.setSampleInfoKey(developChildOrderAttrPo.getAttrName());
                developReviewSampleOrderInfoPo.setAttributeNameId(developChildOrderAttrPo.getAttributeNameId());

                developReviewSampleOrderInfoPoList.add(developReviewSampleOrderInfoPo);
            }
        }

        return developReviewSampleOrderInfoPoList;
    }

    public static DevelopReviewAndSampleBo sampleCreateReviewToPo(DevelopSampleOrderPo developSampleOrderPo,
                                                                  DevelopChildOrderPo developChildOrderPo,
                                                                  String developReviewOrderNo,
                                                                  List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoOldList,
                                                                  List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoOldList,
                                                                  Map<Long, List<String>> fileCodeListByEffectMap,
                                                                  Map<Long, List<String>> fileCodeListByDetailMap,
                                                                  IdGenerateService idGenerateService) {
        final DevelopReviewOrderPo developReviewOrderPo = new DevelopReviewOrderPo();
        developReviewOrderPo.setPrenatalSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
        developReviewOrderPo.setPlatform(developSampleOrderPo.getPlatform());
        developReviewOrderPo.setSpu(developSampleOrderPo.getSpu());
        developReviewOrderPo.setCategoryId(developChildOrderPo.getCategoryId());
        developReviewOrderPo.setCategory(developChildOrderPo.getCategory());
        developReviewOrderPo.setDevelopReviewOrderNo(developReviewOrderNo);
        developReviewOrderPo.setDevelopReviewOrderStatus(DevelopReviewOrderStatus.TO_BE_SUBMITTED_REVIEW);
        developReviewOrderPo.setDevelopReviewOrderType(DevelopReviewOrderType.PRENATAL_REVIEW);
        developReviewOrderPo.setDevelopReviewRelated(DevelopReviewRelated.DEVELOP_SAMPLE);
        developReviewOrderPo.setSupplierCode(developSampleOrderPo.getSupplierCode());
        developReviewOrderPo.setSupplierName(developSampleOrderPo.getSupplierName());

        // 效果图
        List<ScmImageBo> effectScmImageBoList = new ArrayList<>();

        // 细节图
        List<ScmImageBo> detailScmImageBoList = new ArrayList<>();

        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = Optional.ofNullable(developReviewSampleOrderPoOldList)
                .orElse(new ArrayList<>())
                .stream()
                .map(itemPo -> {
                    // 雪花id
                    long developReviewSampleOrderId = idGenerateService.getSnowflakeId();
                    final DevelopReviewSampleOrderPo developReviewSampleOrderPo = new DevelopReviewSampleOrderPo();
                    developReviewSampleOrderPo.setDevelopReviewSampleOrderId(developReviewSampleOrderId);
                    developReviewSampleOrderPo.setDevelopChildOrderNo(developSampleOrderPo.getDevelopChildOrderNo());
                    developReviewSampleOrderPo.setDevelopParentOrderNo(developSampleOrderPo.getDevelopParentOrderNo());
                    developReviewSampleOrderPo.setDevelopReviewOrderNo(developReviewOrderNo);
                    developReviewSampleOrderPo.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                    developReviewSampleOrderPo.setGramWeight(itemPo.getGramWeight());
                    developReviewSampleOrderPo.setDevelopSampleStage(itemPo.getDevelopSampleStage());

                    // 获取图片信息
                    List<String> fileCodeByEffectList = fileCodeListByEffectMap.get(itemPo.getDevelopReviewSampleOrderId());
                    if (CollectionUtils.isNotEmpty(fileCodeByEffectList)) {
                        ScmImageBo scmImageBo = new ScmImageBo();
                        scmImageBo.setFileCodeList(fileCodeByEffectList);
                        scmImageBo.setImageBizId(developReviewSampleOrderId);
                        effectScmImageBoList.add(scmImageBo);
                    }
                    List<String> fileCodeByDetailList = fileCodeListByDetailMap.get(itemPo.getDevelopReviewSampleOrderId());
                    if (CollectionUtils.isNotEmpty(fileCodeByDetailList)) {
                        ScmImageBo scmImageBo = new ScmImageBo();
                        scmImageBo.setFileCodeList(fileCodeByDetailList);
                        scmImageBo.setImageBizId(developReviewSampleOrderId);
                        detailScmImageBoList.add(scmImageBo);
                    }

                    return developReviewSampleOrderPo;
                }).collect(Collectors.toList());

        final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = Optional.ofNullable(developReviewSampleOrderInfoPoOldList)
                .orElse(new ArrayList<>())
                .stream()
                .map(itemInfoPo -> {
                    final DevelopReviewSampleOrderInfoPo developReviewSampleOrderInfoPo = new DevelopReviewSampleOrderInfoPo();
                    developReviewSampleOrderInfoPo.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                    developReviewSampleOrderInfoPo.setAttributeNameId(itemInfoPo.getAttributeNameId());
                    developReviewSampleOrderInfoPo.setSampleInfoKey(itemInfoPo.getSampleInfoKey());
                    developReviewSampleOrderInfoPo.setSampleInfoValue(itemInfoPo.getSampleInfoValue());
                    developReviewSampleOrderInfoPo.setEvaluationOpinion(itemInfoPo.getEvaluationOpinion());
                    return developReviewSampleOrderInfoPo;
                }).collect(Collectors.toList());


        return DevelopReviewAndSampleBo.builder()
                .developReviewOrderPo(developReviewOrderPo)
                .developReviewSampleOrderPoList(developReviewSampleOrderPoList)
                .developReviewSampleOrderInfoPoList(developReviewSampleOrderInfoPoList)
                .effectScmImageBoList(effectScmImageBoList)
                .detailScmImageBoList(detailScmImageBoList)
                .build();
    }

    public static List<DevelopOrderPriceVo> developOrderPricePoToVoList(List<DevelopOrderPricePo> developOrderPricePoList) {
        return Optional.ofNullable(developOrderPricePoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final DevelopOrderPriceVo developOrderPriceVo = new DevelopOrderPriceVo();
                    if (null != po.getChannelId() && 0 != po.getChannelId()) {
                        developOrderPriceVo.setChannelId(po.getChannelId());
                        developOrderPriceVo.setChannelName(po.getChannelName());
                    }
                    developOrderPriceVo.setPrice(po.getPrice());
                    return developOrderPriceVo;
                }).collect(Collectors.toList());
    }

    public static DevelopOrderPriceVo developOrderPricePoToVo(DevelopOrderPricePo developOrderPricePo) {
        final DevelopOrderPriceVo developOrderPriceVo = new DevelopOrderPriceVo();
        if (null == developOrderPricePo) {
            return developOrderPriceVo;
        }
        if (null != developOrderPricePo.getChannelId() && 0 != developOrderPricePo.getChannelId()) {
            developOrderPriceVo.setChannelId(developOrderPricePo.getChannelId());
            developOrderPriceVo.setChannelName(developOrderPricePo.getChannelName());
        }
        developOrderPriceVo.setPrice(developOrderPricePo.getPrice());
        return developOrderPriceVo;
    }
}
