package com.hete.supply.scm.server.scm.production.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsPlatVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmNormalSkuVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmVariantVo;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataItemRawCompareVo;
import com.hete.supply.scm.server.scm.converter.ProduceDataItemProcessConverter;
import com.hete.supply.scm.server.scm.converter.ProduceDataItemProcessDescConverter;
import com.hete.supply.scm.server.scm.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.bo.ProduceDataItemRawCompareBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProduceDataItemRawComparePo;
import com.hete.supply.scm.server.scm.production.entity.dto.ProduceDataItemInfoDto;
import com.hete.supply.scm.server.scm.production.entity.dto.ProduceDataItemRawCompareDto;
import com.hete.supply.scm.server.scm.production.entity.dto.ProduceDataItemRawInfoDto;
import com.hete.supply.scm.server.scm.production.entity.vo.*;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseLatestPriceItemBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductCompareVo;
import com.hete.support.api.enums.BooleanType;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/9/25 11:40
 */
@Slf4j
public class SkuProdConverter {

    public static SkuTopDetailVo skuDataToSkuTopDetail(PlmSkuPo plmSkuPo,
                                                       PlmVariantVo plmVariantVo,
                                                       Map<String, PlmCategoryVo> categoriesVoMap,
                                                       Map<String, List<String>> skuSaleFileCodeMap,
                                                       List<SupplierProductComparePo> supplierProductComparePoList) {
        final SkuTopDetailVo skuTopDetailVo = new SkuTopDetailVo();
        skuTopDetailVo.setSku(plmSkuPo.getSku());
        skuTopDetailVo.setSpu(plmVariantVo.getSpuCode());
        skuTopDetailVo.setSkuEncode(plmVariantVo.getSkuEncode());
        skuTopDetailVo.setSkuDevType(plmVariantVo.getSkuDevType());

        PlmCategoryVo plmCategoryVo = categoriesVoMap.get(plmSkuPo.getSku());
        if (null != plmCategoryVo) {
            skuTopDetailVo.setCategoryId(plmCategoryVo.getCategoryId());
            skuTopDetailVo.setCategoryName(plmCategoryVo.getCategoryNameCn());
        }

        List<SupplierProductCompareVo> supplierProductCompareVoList = new ArrayList<>();
        for (SupplierProductComparePo supplierProductComparePo : supplierProductComparePoList) {
            SupplierProductCompareVo supplierProductCompareVo = new SupplierProductCompareVo();
            supplierProductCompareVo.setSupplierProductCompareId(supplierProductComparePo.getSupplierProductCompareId());
            supplierProductCompareVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
            supplierProductCompareVo.setSupplierCode(supplierProductComparePo.getSupplierCode());
            supplierProductCompareVo.setVersion(supplierProductComparePo.getVersion());
            supplierProductCompareVo.setSku(supplierProductComparePo.getSku());
            supplierProductCompareVo.setSupplierProductCompareStatus(supplierProductComparePo.getSupplierProductCompareStatus());
            supplierProductCompareVoList.add(supplierProductCompareVo);
        }
        skuTopDetailVo.setSupplierProductCompareList(supplierProductCompareVoList);

        skuTopDetailVo.setFileCodeList(skuSaleFileCodeMap.get(plmSkuPo.getSku()));
        return skuTopDetailVo;
    }

    public static List<ProduceDataAttrItemVo> arrPoToArrVo(List<ProduceDataAttrPo> produceDataAttrPoList) {
        return Optional.ofNullable(produceDataAttrPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(produceDataAttrPo -> {
                    ProduceDataAttrItemVo produceDataAttrItemVo = new ProduceDataAttrItemVo();
                    produceDataAttrItemVo.setAttributeNameId(produceDataAttrPo.getAttributeNameId());
                    produceDataAttrItemVo.setAttrName(produceDataAttrPo.getAttrName());
                    produceDataAttrItemVo.setAttrValue(produceDataAttrPo.getAttrValue());
                    return produceDataAttrItemVo;
                }).collect(Collectors.toList());
    }

    public static List<ProduceDataItemInfoVo> itemPoToVo(List<ProduceDataItemPo> poList,
                                                         Map<Long, List<ProduceDataItemRawPo>> produceDataItemRawPoMap,
                                                         Map<Long, List<ProduceDataItemProcessPo>> produceDataItemProcessPoMap,
                                                         Map<Long, List<ProduceDataItemProcessDescPo>> produceDataItemProcessDescPoMap,
                                                         Map<Long, List<String>> produceDataItemEffectMap,
                                                         Map<Long, List<String>> produceDataItemDetailMap,
                                                         Map<String, String> skuEncodeMapByRaw,
                                                         List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList,
                                                         List<ProduceDataItemRawComparePo> prodRawComparePoList) {
        return Optional.ofNullable(poList)
                .orElse(Collections.emptyList())
                .stream()
                .map(itemPo -> {
                    final ProduceDataItemInfoVo produceDataItemInfoVo = new ProduceDataItemInfoVo();
                    produceDataItemInfoVo.setProduceDataItemId(itemPo.getProduceDataItemId());
                    produceDataItemInfoVo.setVersion(itemPo.getVersion());
                    produceDataItemInfoVo.setBusinessNo(itemPo.getBusinessNo());
                    produceDataItemInfoVo.setCreateUser(itemPo.getCreateUser());
                    produceDataItemInfoVo.setCreateUsername(itemPo.getCreateUsername());
                    produceDataItemInfoVo.setCreateTime(itemPo.getCreateTime());
                    produceDataItemInfoVo.setUpdateUser(itemPo.getUpdateUser());
                    produceDataItemInfoVo.setUpdateUsername(itemPo.getUpdateUsername());
                    produceDataItemInfoVo.setUpdateTime(itemPo.getUpdateTime());
                    produceDataItemInfoVo.setBomName(itemPo.getBomName());
                    List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataItemRawPoMap.get(itemPo.getProduceDataItemId());
                    if (CollectionUtils.isNotEmpty(produceDataItemRawPoList)) {
                        //生产资料bom原料列表
                        List<ProduceDataItemRawInfoVo> prodRawInfoVoList = SkuProdConverter.toProduceDataItemRawInfoVoList(produceDataItemRawPoList);
                        for (ProduceDataItemRawInfoVo produceDataItemRawInfoVo : prodRawInfoVoList) {
                            produceDataItemRawInfoVo.setSkuEncode(skuEncodeMapByRaw.get(produceDataItemRawInfoVo.getSku()));

                            //生产资料原料对照关系
                            Long prodRawId = produceDataItemRawInfoVo.getProduceDataItemRawId();
                            List<ProduceDataItemRawComparePo> matchProdRawPoList = prodRawComparePoList.stream()
                                    .filter(prodRawComparePo -> Objects.equals(prodRawId, prodRawComparePo.getProduceDataItemRawId()))
                                    .collect(Collectors.toList());
                            List<ProduceDataItemRawCompareVo> prodRawCompareVoList = SkuProdConverter.buildProdRawCompareVoList(matchProdRawPoList, skuEncodeMapByRaw);
                            produceDataItemRawInfoVo.setProdRawCompareVoList(prodRawCompareVoList);
                        }
                        produceDataItemInfoVo.setProduceDataItemRawInfoList(prodRawInfoVoList);
                    }

                    List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessPoMap.get(itemPo.getProduceDataItemId());
                    if (CollectionUtils.isNotEmpty(produceDataItemProcessPoList)) {
                        produceDataItemInfoVo.setProcessInfoList(ProduceDataItemProcessConverter.INSTANCE.convertInfoVo(produceDataItemProcessPoList));
                    }

                    List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = produceDataItemProcessDescPoMap.get(itemPo.getProduceDataItemId());
                    if (CollectionUtils.isNotEmpty(produceDataItemProcessDescPoList)) {
                        produceDataItemInfoVo.setProcessDescInfoList(ProduceDataItemProcessDescConverter.INSTANCE.convertInfoVo(produceDataItemProcessDescPoList));
                    }

                    if (produceDataItemEffectMap.containsKey(itemPo.getProduceDataItemId())) {
                        produceDataItemInfoVo.setEffectFileCodeList(produceDataItemEffectMap.get(itemPo.getProduceDataItemId()));
                    }

                    if (produceDataItemDetailMap.containsKey(itemPo.getProduceDataItemId())) {
                        produceDataItemInfoVo.setDetailFileCodeList(produceDataItemDetailMap.get(itemPo.getProduceDataItemId()));
                    }

                    // 关联供应商
                    List<ProduceDataItemSupplierInfoVo> produceDataItemSupplierList = Optional.of(produceDataItemSupplierPoList)
                            .orElse(new ArrayList<>())
                            .stream()
                            .filter(produceDataItemSupplierPo -> produceDataItemSupplierPo.getProduceDataItemId().equals(itemPo.getProduceDataItemId()))
                            .map(produceDataItemSupplierPo -> {
                                ProduceDataItemSupplierInfoVo produceDataItemSupplierInfoVo = new ProduceDataItemSupplierInfoVo();
                                produceDataItemSupplierInfoVo.setSupplierCode(produceDataItemSupplierPo.getSupplierCode());
                                produceDataItemSupplierInfoVo.setSupplierName(produceDataItemSupplierPo.getSupplierName());
                                return produceDataItemSupplierInfoVo;
                            }).collect(Collectors.toList());
                    produceDataItemInfoVo.setProduceDataItemSupplierInfoList(produceDataItemSupplierList);

                    return produceDataItemInfoVo;
                }).collect(Collectors.toList());
    }

    private static List<ProduceDataItemRawCompareVo> buildProdRawCompareVoList(List<ProduceDataItemRawComparePo> matchProdRawPoList,
                                                                               Map<String, String> skuEncodeMapByRaw) {
        if (CollectionUtils.isEmpty(matchProdRawPoList)) {
            return Collections.emptyList();
        }
        return matchProdRawPoList.stream().map(prodRawComparePo -> {
            ProduceDataItemRawCompareVo prodRawCompareVo = new ProduceDataItemRawCompareVo();
            prodRawCompareVo.setSkuCompareId(prodRawComparePo.getProduceDataItemRawCompareId());
            prodRawCompareVo.setSku(prodRawComparePo.getSku());
            prodRawCompareVo.setSkuEncode(skuEncodeMapByRaw.get(prodRawComparePo.getSku()));
            prodRawCompareVo.setQuantity(prodRawComparePo.getQuantity());
            return prodRawCompareVo;
        }).collect(Collectors.toList());
    }

    private static List<ProduceDataItemRawInfoVo> toProduceDataItemRawInfoVoList(List<ProduceDataItemRawPo> produceDataItemRawPoList) {
        if (CollectionUtils.isEmpty(produceDataItemRawPoList)) {
            return Collections.emptyList();
        }
        return produceDataItemRawPoList.stream().map(rawPo -> {
            ProduceDataItemRawInfoVo produceDataItemRawInfoVo = new ProduceDataItemRawInfoVo();
            produceDataItemRawInfoVo.setProduceDataItemRawId(rawPo.getProduceDataItemRawId());
            produceDataItemRawInfoVo.setMaterialType(rawPo.getMaterialType());
            produceDataItemRawInfoVo.setSku(rawPo.getSku());
            produceDataItemRawInfoVo.setSkuCnt(rawPo.getSkuCnt());
            return produceDataItemRawInfoVo;
        }).collect(Collectors.toList());
    }

    public static ProduceDataItemRawPo prodDtoToRawPo(Long produceDataItemId, ProduceDataItemRawInfoDto prodRawDto, String spu) {
        if (Objects.isNull(prodRawDto)) {
            log.info("生产信息详情原料数据为空，请检查数据！");
            return null;
        }
        if (Objects.isNull(produceDataItemId)) {
            log.info("生产资料produceDataItemId为空，请检查数据！");
            return null;
        }
        ProduceDataItemRawPo produceDataItemRawPo = new ProduceDataItemRawPo();
        produceDataItemRawPo.setProduceDataItemId(produceDataItemId);
        produceDataItemRawPo.setMaterialType(MaterialType.SKU);
        produceDataItemRawPo.setSpu(spu);
        produceDataItemRawPo.setSku(prodRawDto.getSku());
        produceDataItemRawPo.setSkuCnt(prodRawDto.getSkuCnt());
        return produceDataItemRawPo;
    }

    public static List<ProduceDataItemProcessPo> itemDtoToProcessPo(List<ProduceDataItemInfoDto> dtoList, String spu, String sku) {
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = new ArrayList<>();
        for (ProduceDataItemInfoDto produceDataItemInfoDto : dtoList) {
            Optional.ofNullable(produceDataItemInfoDto.getProcessInfoList())
                    .orElse(new ArrayList<>())
                    .forEach(processInfoDto -> {
                        final ProduceDataItemProcessPo produceDataItemProcessPo = new ProduceDataItemProcessPo();
                        produceDataItemProcessPo.setProduceDataItemId(produceDataItemInfoDto.getProduceDataItemId());
                        produceDataItemProcessPo.setSpu(spu);
                        produceDataItemProcessPo.setSku(sku);
                        produceDataItemProcessPo.setProcessCode(processInfoDto.getProcessCode());
                        produceDataItemProcessPo.setProcessName(processInfoDto.getProcessName());
                        produceDataItemProcessPo.setProcessSecondCode(processInfoDto.getProcessSecondCode());
                        produceDataItemProcessPo.setProcessSecondName(processInfoDto.getProcessSecondName());
                        produceDataItemProcessPo.setProcessFirst(processInfoDto.getProcessFirst());
                        produceDataItemProcessPo.setProcessLabel(processInfoDto.getProcessLabel());
                        produceDataItemProcessPoList.add(produceDataItemProcessPo);
                    });
        }
        return produceDataItemProcessPoList;
    }

    public static List<ProduceDataItemProcessDescPo> itemDtoToProcessDescPo(List<ProduceDataItemInfoDto> dtoList, String spu, String sku) {
        List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = new ArrayList<>();
        for (ProduceDataItemInfoDto produceDataItemInfoDto : dtoList) {
            Optional.ofNullable(produceDataItemInfoDto.getProcessDescInfoList())
                    .orElse(new ArrayList<>())
                    .forEach(processDescInfoDto -> {
                        final ProduceDataItemProcessDescPo produceDataItemProcessDescPo = new ProduceDataItemProcessDescPo();
                        produceDataItemProcessDescPo.setProduceDataItemId(produceDataItemInfoDto.getProduceDataItemId());
                        produceDataItemProcessDescPo.setSpu(spu);
                        produceDataItemProcessDescPo.setSku(sku);
                        produceDataItemProcessDescPo.setName(processDescInfoDto.getName());
                        produceDataItemProcessDescPo.setDescValue(processDescInfoDto.getDescValue());
                        produceDataItemProcessDescPoList.add(produceDataItemProcessDescPo);
                    });
        }
        return produceDataItemProcessDescPoList;
    }

    public static List<ProduceDataItemSupplierPo> itemDtoToSupplierPo(List<ProduceDataItemInfoDto> dtoList, String spu, String sku) {
        List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList = new ArrayList<>();
        for (ProduceDataItemInfoDto produceDataItemInfoDto : dtoList) {
            Optional.ofNullable(produceDataItemInfoDto.getProduceDataItemSupplierInfoList())
                    .orElse(new ArrayList<>())
                    .forEach(produceDataItemSupplierInfoDto -> {
                        final ProduceDataItemSupplierPo produceDataItemSupplierPo = new ProduceDataItemSupplierPo();
                        produceDataItemSupplierPo.setProduceDataItemId(produceDataItemInfoDto.getProduceDataItemId());
                        produceDataItemSupplierPo.setSpu(spu);
                        produceDataItemSupplierPo.setSku(sku);
                        produceDataItemSupplierPo.setSupplierCode(produceDataItemSupplierInfoDto.getSupplierCode());
                        produceDataItemSupplierPo.setSupplierName(produceDataItemSupplierInfoDto.getSupplierName());
                        produceDataItemSupplierPoList.add(produceDataItemSupplierPo);
                    });
        }
        return produceDataItemSupplierPoList;
    }

    public static void searchToPlmSkuSearchVo(PlmSkuSearchVo record,
                                              Map<String, PlmCategoryVo> categoriesVoMap,
                                              Map<String, List<String>> skuSaleFileCodeMap,
                                              List<PlmNormalSkuVo> plmNormalSkuVoList,
                                              Map<String, SkuRisk> skuRiskBySkuMap,
                                              Map<String, SkuInfoPo> singleCapacityMap,
                                              List<SupplierProductComparePo> supplierProductComparePoList,
                                              List<PurchaseLatestPriceItemBo> purchasePriceBySkuList) {

        PlmCategoryVo plmCategoryVo = categoriesVoMap.get(record.getSku());
        if (null != plmCategoryVo) {
            record.setCategoryId(plmCategoryVo.getCategoryId());
            record.setCategoryName(plmCategoryVo.getCategoryNameCn());
        }

        record.setFileCodeList(skuSaleFileCodeMap.get(record.getSku()));

        plmNormalSkuVoList.stream()
                .filter(skuInfoBySku -> skuInfoBySku.getSkuCode().equals(record.getSku()))
                .findFirst()
                .ifPresent(skuInfoBySku -> {
                    record.setSkuEncode(skuInfoBySku.getSkuEncode());
                    record.setSkuDevType(skuInfoBySku.getSkuDevType());
                    List<String> platNameList = Optional.ofNullable(skuInfoBySku.getGoodsPlatVoList())
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(PlmGoodsPlatVo::getPlatName)
                            .distinct()
                            .collect(Collectors.toList());
                    record.setPlatNameList(platNameList);
                    if (CollectionUtils.isNotEmpty(platNameList)) {
                        record.setIsSale(BooleanType.TRUE);
                    } else {
                        record.setIsSale(BooleanType.FALSE);
                    }

                });

        record.setSkuRisk(skuRiskBySkuMap.get(record.getSku()));

        SkuInfoPo skuInfoPo = singleCapacityMap.get(record.getSku());
        if (null != skuInfoPo) {
            record.setSingleCapacity(skuInfoPo.getSingleCapacity());
        }

        List<PlmSkuSearchSupplierItemVo> plmSkuSearchSupplierItemList = Optional.ofNullable(supplierProductComparePoList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(supplierProductComparePo -> supplierProductComparePo.getSku().equals(record.getSku()))
                .map(supplierProductComparePo -> {
                    PlmSkuSearchSupplierItemVo plmSkuSearchSupplierItemVo = new PlmSkuSearchSupplierItemVo();
                    plmSkuSearchSupplierItemVo.setSupplierCode(supplierProductComparePo.getSupplierCode());
                    plmSkuSearchSupplierItemVo.setSupplierName(supplierProductComparePo.getSupplierName());
                    plmSkuSearchSupplierItemVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                    // 找到采购确认时间confirmTime最晚的
                    // 设置默认值
                    plmSkuSearchSupplierItemVo.setGoodsPurchasePrice(BigDecimal.ZERO);
                    purchasePriceBySkuList.stream()
                            .filter(itemBo -> itemBo.getSku().equals(record.getSku()))
                            .filter(itemBo -> itemBo.getSupplierCode().equals(supplierProductComparePo.getSupplierCode()))
                            .filter(item -> item.getConfirmTime() != null)
                            .max(Comparator.comparing(PurchaseLatestPriceItemBo::getConfirmTime))
                            .ifPresent(purchaseLatestPriceItemBo -> plmSkuSearchSupplierItemVo.setGoodsPurchasePrice(purchaseLatestPriceItemBo.getPurchasePrice()));
                    return plmSkuSearchSupplierItemVo;
                }).collect(Collectors.toList());
        record.setPlmSkuSearchSupplierItemList(plmSkuSearchSupplierItemList);


    }

    public static List<ProduceDataItemRawComparePo> toProdRawComparePoList(Long prodRawId, List<ProduceDataItemRawCompareDto> prodRawCompareDtoList) {
        if (CollectionUtils.isEmpty(prodRawCompareDtoList)) {
            return Collections.emptyList();
        }
        return prodRawCompareDtoList.stream().map(prodRawCompareDto -> {
            ProduceDataItemRawComparePo produceDataItemRawComparePo = new ProduceDataItemRawComparePo();
            produceDataItemRawComparePo.setProduceDataItemRawId(prodRawId);
            produceDataItemRawComparePo.setSku(prodRawCompareDto.getSku());
            produceDataItemRawComparePo.setQuantity(prodRawCompareDto.getQuantity());
            return produceDataItemRawComparePo;
        }).collect(Collectors.toList());
    }

    public static List<ProduceDataItemRawCompareBo> toProdRawCompareBoList(List<ProduceDataItemRawComparePo> matchProdRawComparePoList, Map<String, String> skuEncodeMap) {
        if (CollectionUtils.isEmpty(matchProdRawComparePoList)) {
            return Collections.emptyList();
        }
        return matchProdRawComparePoList.stream().map(matchProdRawComparePo -> {
            ProduceDataItemRawCompareBo bo = new ProduceDataItemRawCompareBo();
            bo.setProduceDataItemRawCompareId(matchProdRawComparePo.getProduceDataItemRawCompareId());
            bo.setProduceDataItemRawId(matchProdRawComparePo.getProduceDataItemRawId());

            String sku = matchProdRawComparePo.getSku();
            bo.setSku(sku);
            bo.setSkuEncode(skuEncodeMap.get(sku));
            bo.setQuantity(matchProdRawComparePo.getQuantity());
            return bo;
        }).collect(Collectors.toList());
    }
}
