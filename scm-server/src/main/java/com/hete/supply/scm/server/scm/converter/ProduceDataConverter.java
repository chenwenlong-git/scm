package com.hete.supply.scm.server.scm.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataAttrItemVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataDetailAttrVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataVo;
import com.hete.supply.scm.server.scm.entity.bo.*;
import com.hete.supply.scm.server.scm.entity.dto.*;
import com.hete.supply.scm.server.scm.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.support.api.enums.BooleanType;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/7/5 17:07
 */
@Slf4j
public class ProduceDataConverter {

    public static List<ProduceDataItemVo> itemPoToVo(List<ProduceDataItemPo> poList,
                                                     Map<Long, List<ProduceDataItemRawPo>> produceDataItemRawPoMap,
                                                     Map<Long, List<ProduceDataItemProcessPo>> produceDataItemProcessPoMap,
                                                     Map<Long, List<ProduceDataItemProcessDescPo>> produceDataItemProcessDescPoMap,
                                                     Map<Long, List<String>> produceDataItemEffectMap,
                                                     Map<Long, List<String>> produceDataItemDetailMap,
                                                     Map<String, String> skuEncodeMapByRaw,
                                                     List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList) {
        return Optional.ofNullable(poList)
                .orElse(Collections.emptyList())
                .stream()
                .map(itemPo -> {
                    final ProduceDataItemVo produceDataItemVo = new ProduceDataItemVo();
                    produceDataItemVo.setProduceDataItemId(itemPo.getProduceDataItemId());
                    produceDataItemVo.setVersion(itemPo.getVersion());
                    produceDataItemVo.setBusinessNo(itemPo.getBusinessNo());
                    produceDataItemVo.setCreateUsername(itemPo.getCreateUsername());
                    produceDataItemVo.setCreateTime(itemPo.getCreateTime());
                    produceDataItemVo.setBomName(itemPo.getBomName());
                    List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataItemRawPoMap.get(itemPo.getProduceDataItemId());
                    if (CollectionUtils.isNotEmpty(produceDataItemRawPoList)) {
                        List<ProduceDataItemRawListVo> produceDataItemRawListVoList = ProduceDataItemRawConverter.INSTANCE.convert(produceDataItemRawPoList);
                        for (ProduceDataItemRawListVo produceDataItemRawListVo : produceDataItemRawListVoList) {
                            produceDataItemRawListVo.setSkuEncode(skuEncodeMapByRaw.get(produceDataItemRawListVo.getSku()));
                        }
                        produceDataItemVo.setProduceDataItemRawList(produceDataItemRawListVoList);
                    }

                    List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessPoMap.get(itemPo.getProduceDataItemId());
                    if (CollectionUtils.isNotEmpty(produceDataItemProcessPoList)) {
                        produceDataItemVo.setProduceDataItemProcessList(ProduceDataItemProcessConverter.INSTANCE.convert(produceDataItemProcessPoList));
                    }

                    List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = produceDataItemProcessDescPoMap.get(itemPo.getProduceDataItemId());
                    if (CollectionUtils.isNotEmpty(produceDataItemProcessDescPoList)) {
                        produceDataItemVo.setProduceDataItemProcessDescList(ProduceDataItemProcessDescConverter.INSTANCE.convert(produceDataItemProcessDescPoList));
                    }

                    if (produceDataItemEffectMap.containsKey(itemPo.getProduceDataItemId())) {
                        produceDataItemVo.setEffectFileCodeList(produceDataItemEffectMap.get(itemPo.getProduceDataItemId()));
                    }

                    if (produceDataItemDetailMap.containsKey(itemPo.getProduceDataItemId())) {
                        produceDataItemVo.setDetailFileCodeList(produceDataItemDetailMap.get(itemPo.getProduceDataItemId()));
                    }

                    // 关联供应商
                    List<ProduceDataItemSupplierVo> produceDataItemSupplierList = Optional.of(produceDataItemSupplierPoList)
                            .orElse(new ArrayList<>())
                            .stream()
                            .filter(produceDataItemSupplierPo -> produceDataItemSupplierPo.getProduceDataItemId().equals(itemPo.getProduceDataItemId()))
                            .map(produceDataItemSupplierPo -> {
                                ProduceDataItemSupplierVo produceDataItemSupplierVo = new ProduceDataItemSupplierVo();
                                produceDataItemSupplierVo.setSupplierCode(produceDataItemSupplierPo.getSupplierCode());
                                produceDataItemSupplierVo.setSupplierName(produceDataItemSupplierPo.getSupplierName());
                                return produceDataItemSupplierVo;
                            }).collect(Collectors.toList());
                    produceDataItemVo.setProduceDataItemSupplierList(produceDataItemSupplierList);

                    return produceDataItemVo;
                }).collect(Collectors.toList());
    }

    public static List<ProduceDataItemRawPo> itemDtoToRawPo(List<ProduceDataItemDto> dtoList, String spu) {
        List<ProduceDataItemRawPo> produceDataItemRawPoList = new ArrayList<>();
        for (ProduceDataItemDto produceDataItemDto : dtoList) {
            if (CollectionUtils.isNotEmpty(produceDataItemDto.getProduceDataItemRawList())) {
                for (ProduceDataItemRawListDto produceDataItemRawDto : produceDataItemDto.getProduceDataItemRawList()) {
                    final ProduceDataItemRawPo produceDataItemRawPo = new ProduceDataItemRawPo();
                    produceDataItemRawPo.setProduceDataItemId(produceDataItemDto.getProduceDataItemId());
                    produceDataItemRawPo.setMaterialType(produceDataItemRawDto.getMaterialType());
                    produceDataItemRawPo.setSpu(spu);
                    produceDataItemRawPo.setSku(produceDataItemRawDto.getSku());
                    produceDataItemRawPo.setSkuCnt(produceDataItemRawDto.getSkuCnt());
                    produceDataItemRawPoList.add(produceDataItemRawPo);
                }
            }
        }
        return produceDataItemRawPoList;
    }


    public static List<ProduceDataItemProcessPo> itemDtoToProcessPo(List<ProduceDataItemDto> dtoList, String spu, String sku) {
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = new ArrayList<>();
        for (ProduceDataItemDto produceDataItemDto : dtoList) {
            List<ProduceDataItemProcessListDto> produceDataItemProcessList = Optional.ofNullable(produceDataItemDto.getProduceDataItemProcessList()).orElse(new ArrayList<>());
            for (ProduceDataItemProcessListDto produceDataItemProcessListDto : produceDataItemProcessList) {
                final ProduceDataItemProcessPo produceDataItemProcessPo = new ProduceDataItemProcessPo();
                produceDataItemProcessPo.setProduceDataItemId(produceDataItemDto.getProduceDataItemId());
                produceDataItemProcessPo.setSpu(spu);
                produceDataItemProcessPo.setSku(sku);
                produceDataItemProcessPo.setProcessCode(produceDataItemProcessListDto.getProcessCode());
                produceDataItemProcessPo.setProcessName(produceDataItemProcessListDto.getProcessName());
                produceDataItemProcessPo.setProcessSecondCode(produceDataItemProcessListDto.getProcessSecondCode());
                produceDataItemProcessPo.setProcessSecondName(produceDataItemProcessListDto.getProcessSecondName());
                produceDataItemProcessPo.setProcessFirst(produceDataItemProcessListDto.getProcessFirst());
                produceDataItemProcessPo.setProcessLabel(produceDataItemProcessListDto.getProcessLabel());
                produceDataItemProcessPoList.add(produceDataItemProcessPo);
            }
        }
        return produceDataItemProcessPoList;
    }

    public static List<ProduceDataItemProcessDescPo> itemDtoToProcessDescPo(List<ProduceDataItemDto> dtoList, String spu, String sku) {
        List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = new ArrayList<>();
        for (ProduceDataItemDto produceDataItemDto : dtoList) {
            List<ProduceDataItemProcessDescListDto> produceDataItemProcessDescListDtoList = Optional.ofNullable(produceDataItemDto.getProduceDataItemProcessDescList()).orElse(new ArrayList<>());
            for (ProduceDataItemProcessDescListDto produceDataItemProcessDescListDto : produceDataItemProcessDescListDtoList) {
                final ProduceDataItemProcessDescPo produceDataItemProcessDescPo = new ProduceDataItemProcessDescPo();
                produceDataItemProcessDescPo.setProduceDataItemId(produceDataItemDto.getProduceDataItemId());
                produceDataItemProcessDescPo.setSpu(spu);
                produceDataItemProcessDescPo.setSku(sku);
                produceDataItemProcessDescPo.setName(produceDataItemProcessDescListDto.getName());
                produceDataItemProcessDescPo.setDescValue(produceDataItemProcessDescListDto.getDescValue());
                produceDataItemProcessDescPoList.add(produceDataItemProcessDescPo);
            }
        }
        return produceDataItemProcessDescPoList;
    }

    public static List<ProduceDataSkuDetailVo> detailBoToDetailVo(List<ProduceDataDetailBo> produceDataDetailBoList,
                                                                  Map<String, String> skuEncodeMap) {
        return Optional.ofNullable(produceDataDetailBoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(itemBo -> {
                    final ProduceDataSkuDetailVo vo = new ProduceDataSkuDetailVo();
                    vo.setSku(itemBo.getSku());

                    //生产信息
                    List<ProduceDataAttrBo> produceDataAttrBoList = itemBo.getProduceDataAttrBoList();
                    List<ProduceDataDetailAttrVo> produceAttrList = Optional.ofNullable(produceDataAttrBoList)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(produceAttrBo -> {
                                ProduceDataDetailAttrVo produceDataDetailAttrVo = new ProduceDataDetailAttrVo();
                                produceDataDetailAttrVo.setAttributeNameId(produceAttrBo.getAttributeNameId());
                                produceDataDetailAttrVo.setAttrName(produceAttrBo.getAttrName());
                                produceDataDetailAttrVo.setAttrValue(produceAttrBo.getAttrValue());
                                return produceDataDetailAttrVo;
                            }).collect(Collectors.toList());
                    vo.setProduceAttrList(produceAttrList);

                    List<ProduceDataItemBo> produceDataItemBoList = itemBo.getProduceDataItemBoList();
                    if (CollectionUtils.isNotEmpty(produceDataItemBoList)) {
                        //取优先级最高第一条
                        ProduceDataItemBo produceDataItemBo = produceDataItemBoList.get(0);
                        //效果图
                        List<String> effectFileCodeList = produceDataItemBo.getEffectFileCodeList();
                        if (CollectionUtils.isNotEmpty(effectFileCodeList)) {
                            vo.setFileCodeList(effectFileCodeList);
                        } else {
                            vo.setFileCodeList(itemBo.getSpuFileCodeList());
                        }

                        //原料
                        List<ProduceDataItemRawListBo> produceDataItemRawBoList = produceDataItemBo.getProduceDataItemRawBoList();
                        List<ProduceDataDetailRawVo> rawVoList = Optional.ofNullable(produceDataItemRawBoList)
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(rawBo -> {
                                    ProduceDataDetailRawVo produceDataDetailRawVo = new ProduceDataDetailRawVo();
                                    produceDataDetailRawVo.setSku(rawBo.getSku());
                                    produceDataDetailRawVo.setDeliveryCnt(rawBo.getSkuCnt());
                                    produceDataDetailRawVo.setSkuEncode(skuEncodeMap.get(rawBo.getSku()));
                                    return produceDataDetailRawVo;
                                }).collect(Collectors.toList());
                        vo.setRawVoList(rawVoList);

                        //工序
                        List<ProduceDataItemProcessListBo> produceDataItemProcessList = produceDataItemBo.getProduceDataItemProcessBoList();
                        List<ProduceDataDetailProcessVo> processVoList = Optional.ofNullable(produceDataItemProcessList)
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(processBo -> {
                                    ProduceDataDetailProcessVo produceDataDetailProcessVo = new ProduceDataDetailProcessVo();
                                    produceDataDetailProcessVo.setProcessCode(processBo.getProcessCode());
                                    produceDataDetailProcessVo.setProcessName(processBo.getProcessName());
                                    produceDataDetailProcessVo.setProcessSecondCode(processBo.getProcessSecondCode());
                                    produceDataDetailProcessVo.setProcessSecondName(processBo.getProcessSecondName());
                                    produceDataDetailProcessVo.setProcessFirst(processBo.getProcessFirst());
                                    produceDataDetailProcessVo.setProcessLabel(processBo.getProcessLabel());
                                    return produceDataDetailProcessVo;
                                }).collect(Collectors.toList());
                        vo.setProcessVoList(processVoList);

                        //工序描述
                        List<ProduceDataItemProcessDescListBo> produceDataItemProcessDescList = produceDataItemBo.getProduceDataItemProcessDescBoList();
                        List<ProduceDataDetailProcessDescVo> processDescVoList = Optional.ofNullable(produceDataItemProcessDescList)
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(processDescBo -> {
                                    ProduceDataDetailProcessDescVo produceDataDetailProcessDescVo = new ProduceDataDetailProcessDescVo();
                                    produceDataDetailProcessDescVo.setName(processDescBo.getName());
                                    produceDataDetailProcessDescVo.setDescValue(processDescBo.getDescValue());
                                    return produceDataDetailProcessDescVo;
                                }).collect(Collectors.toList());
                        vo.setProcessDescVoList(processDescVoList);

                    }

                    return vo;
                }).collect(Collectors.toList());
    }

    public static List<ProduceDataItemSupplierPo> itemDtoToSupplierPo(List<ProduceDataItemDto> dtoList, String spu, String sku) {
        List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList = new ArrayList<>();
        for (ProduceDataItemDto produceDataItemDto : dtoList) {
            if (CollectionUtils.isNotEmpty(produceDataItemDto.getProduceDataItemSupplierList())) {
                for (ProduceDataItemSupplierDto produceDataItemSupplierDto : produceDataItemDto.getProduceDataItemSupplierList()) {
                    final ProduceDataItemSupplierPo produceDataItemSupplierPo = new ProduceDataItemSupplierPo();
                    produceDataItemSupplierPo.setProduceDataItemId(produceDataItemDto.getProduceDataItemId());
                    produceDataItemSupplierPo.setSpu(spu);
                    produceDataItemSupplierPo.setSku(sku);
                    produceDataItemSupplierPo.setSupplierCode(produceDataItemSupplierDto.getSupplierCode());
                    produceDataItemSupplierPo.setSupplierName(produceDataItemSupplierDto.getSupplierName());
                    produceDataItemSupplierPoList.add(produceDataItemSupplierPo);
                }
            }
        }
        return produceDataItemSupplierPoList;
    }

    public static List<ProduceDataSpecSupplierPo> specSupplierDtoToPo(List<ProduceDataSpecSupplierDto> produceDataSpecSupplierDtoList,
                                                                      ProduceDataSpecPo produceDataSpecPo) {
        return Optional.ofNullable(produceDataSpecSupplierDtoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(produceDataSpecSupplierDto -> {
                    ProduceDataSpecSupplierPo produceDataSpecSupplierPo = new ProduceDataSpecSupplierPo();
                    produceDataSpecSupplierPo.setProduceDataSpecId(produceDataSpecPo.getProduceDataSpecId());
                    produceDataSpecSupplierPo.setSpu(produceDataSpecPo.getSpu());
                    produceDataSpecSupplierPo.setSku(produceDataSpecPo.getSku());
                    produceDataSpecSupplierPo.setSupplierCode(produceDataSpecSupplierDto.getSupplierCode());
                    produceDataSpecSupplierPo.setSupplierName(produceDataSpecSupplierDto.getSupplierName());
                    return produceDataSpecSupplierPo;
                }).collect(Collectors.toList());
    }

    public static List<ProduceDataVo> poListToProduceDataVoList(List<ProduceDataPo> poList,
                                                                Map<String, List<ProduceDataAttrPo>> produceDataAttrPoMap,
                                                                Map<String, List<ProduceDataItemPo>> produceDataItemPoMap) {
        return Optional.ofNullable(poList)
                .orElse(new ArrayList<>()).stream().map(produceDataPo -> {
                    ProduceDataVo produceDataVo = new ProduceDataVo();
                    produceDataVo.setSku(produceDataPo.getSku());
                    produceDataVo.setWeight(produceDataPo.getWeight());
                    // 生产属性
                    List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrPoMap.get(produceDataPo.getSku());
                    List<ProduceDataAttrItemVo> produceDataAttrItemList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(produceDataAttrPoList)) {
                        Map<Long, List<ProduceDataAttrPo>> produceDataAttrPoAttributeNameIdMap = produceDataAttrPoList.stream()
                                .collect(Collectors.groupingBy(ProduceDataAttrPo::getAttributeNameId));
                        produceDataAttrPoAttributeNameIdMap.forEach((Long attributeNameId, List<ProduceDataAttrPo> produceDataAttrPos) -> {
                            ProduceDataAttrItemVo produceDataAttrItemVo = new ProduceDataAttrItemVo();
                            produceDataAttrItemVo.setAttributeNameId(attributeNameId);
                            produceDataAttrPos.stream()
                                    .findFirst()
                                    .ifPresent(produceDataAttrPo -> produceDataAttrItemVo.setAttrName(produceDataAttrPo.getAttrName()));
                            List<String> attrValueList = produceDataAttrPos.stream()
                                    .map(ProduceDataAttrPo::getAttrValue)
                                    .collect(Collectors.toList());
                            produceDataAttrItemVo.setAttrValueList(attrValueList);
                            produceDataAttrItemList.add(produceDataAttrItemVo);
                        });
                    }

                    // Bom信息
                    List<ProduceDataItemPo> produceDataItemPoList = produceDataItemPoMap.get(produceDataPo.getSku());
                    produceDataVo.setIsExistBom(BooleanType.FALSE);
                    if (CollectionUtils.isNotEmpty(produceDataItemPoList)) {
                        produceDataVo.setIsExistBom(BooleanType.TRUE);
                    }

                    produceDataVo.setProduceDataAttrItemList(produceDataAttrItemList);
                    return produceDataVo;
                }).collect(Collectors.toList());
    }

    public static ProduceDataAttrPo attrImportationDtoToPo(String spu,
                                                           String sku,
                                                           Long attributeNameId,
                                                           String attrName,
                                                           String attrValue) {
        ProduceDataAttrPo produceDataAttrPo = new ProduceDataAttrPo();
        produceDataAttrPo.setSpu(spu);
        produceDataAttrPo.setSku(sku);
        produceDataAttrPo.setAttributeNameId(attributeNameId);
        produceDataAttrPo.setAttrName(attrName);
        produceDataAttrPo.setAttrValue(attrValue);
        return produceDataAttrPo;
    }

    public static ProduceDataItemProcessPo importationToProcessPo(ProduceDataItemPo produceDataItemPo,
                                                                  ProcessPo processPo) {
        ProduceDataItemProcessPo produceDataItemProcessPo = new ProduceDataItemProcessPo();
        produceDataItemProcessPo.setProduceDataItemId(produceDataItemPo.getProduceDataItemId());
        produceDataItemProcessPo.setSpu(produceDataItemPo.getSpu());
        produceDataItemProcessPo.setSku(produceDataItemPo.getSku());
        produceDataItemProcessPo.setProcessCode(processPo.getProcessCode());
        // 原前端和后端赋值逻辑，这里只是遵从原赋值逻辑
        produceDataItemProcessPo.setProcessName(processPo.getProcessSecondName());
        produceDataItemProcessPo.setProcessSecondCode(processPo.getProcessSecondCode());
        produceDataItemProcessPo.setProcessSecondName(processPo.getProcessSecondName());
        produceDataItemProcessPo.setProcessFirst(processPo.getProcessFirst());
        produceDataItemProcessPo.setProcessLabel(processPo.getProcessLabel());
        return produceDataItemProcessPo;
    }
}
