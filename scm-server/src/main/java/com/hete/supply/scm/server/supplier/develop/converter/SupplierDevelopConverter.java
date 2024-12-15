package com.hete.supply.scm.server.supplier.develop.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveType;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.develop.converter.DevelopChildConverter;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.*;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.supplier.entity.dto.PamphletReturnRawDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.core.holder.GlobalContext;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/17 18:31
 */
public class SupplierDevelopConverter {


    public static ReceiveOrderCreateMqDto rawDtoToReceiptMqDto(PamphletReturnRawDto dto,
                                                               DevelopPamphletOrderPo developPamphletOrderPo,
                                                               long snowflakeId,
                                                               Map<String, WmsEnum.QcType> warehouseQcTypeMap,
                                                               @NotNull DevelopChildOrderPo developChildOrderPo) {
        final ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
        receiveOrderCreateItem.setReceiveType(ReceiveType.PROCESS_MATERIAL);
        receiveOrderCreateItem.setScmBizNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        receiveOrderCreateItem.setWarehouseCode(dto.getWarehouseCode());
        receiveOrderCreateItem.setSupplierCode(developPamphletOrderPo.getSupplierCode());
        receiveOrderCreateItem.setSupplierName(developPamphletOrderPo.getSupplierName());
        receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
        receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
        receiveOrderCreateItem.setPlaceOrderTime(LocalDateTime.now());
        receiveOrderCreateItem.setSendTime(LocalDateTime.now());
        receiveOrderCreateItem.setQcType(warehouseQcTypeMap.get(dto.getWarehouseCode()));
        receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
        receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());
        receiveOrderCreateItem.setUnionKey(developPamphletOrderPo.getDevelopPamphletOrderNo() +
                ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK +
                ScmBizReceiveOrderType.DEVELOP_SAMPLE_RAW.name() +
                ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK +
                snowflakeId);


        final List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> detailList = dto.getRawItemList()
                .stream()
                .map(item -> {
                    final ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
                    receiveOrderDetail.setBatchCode(item.getSkuBatchCode());
                    receiveOrderDetail.setSkuCode(item.getSku());
                    receiveOrderDetail.setPurchaseAmount(item.getReturnCnt());
                    receiveOrderDetail.setDeliveryAmount(item.getReturnCnt());
                    receiveOrderDetail.setPlatCode(developChildOrderPo.getPlatform());
                    return receiveOrderDetail;
                }).collect(Collectors.toList());
        receiveOrderCreateItem.setDetailList(detailList);

        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(Collections.singletonList(receiveOrderCreateItem));

        return receiveOrderCreateMqDto;
    }

    public static DevelopReviewDetailVo developReviewPoToVo(DevelopReviewOrderPo developReviewOrderPo,
                                                            List<DevelopChildOrderAttrPo> developChildOrderAttrPoList,
                                                            List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList,
                                                            Map<String, List<DevelopReviewSampleOrderInfoPo>> devSampleNoInfoPoListMap,
                                                            Map<Long, List<String>> pamphletImageStyleMap,
                                                            Map<Long, List<String>> pamphletImageColorMap,
                                                            DevelopPamphletOrderPo developPamphletOrderPo,
                                                            Map<Long, List<String>> pamphletSampleImageEffectMap,
                                                            Map<Long, List<String>> pamphletSampleImageDetailMap,
                                                            DevelopChildOrderPo developChildOrderPo,
                                                            Map<Long, List<String>> childImageStyleMap,
                                                            Map<Long, List<String>> childImageColorMap,
                                                            String sampleOrderNoJoining,
                                                            List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList,
                                                            Map<String, DevelopSampleOrderPo> developSampleOrderPoMap,
                                                            Map<String, DevelopReviewSampleOrderPo> developChildOrderSealSampleMap,
                                                            List<DevelopOrderPricePo> developOrderPricePoList) {
        final DevelopReviewDetailVo developReviewDetailVo = new DevelopReviewDetailVo();

        developReviewDetailVo.setDevelopReviewOrderNo(developReviewOrderPo.getDevelopReviewOrderNo());
        developReviewDetailVo.setVersion(developReviewOrderPo.getVersion());
        developReviewDetailVo.setDevelopReviewOrderStatus(developReviewOrderPo.getDevelopReviewOrderStatus());
        developReviewDetailVo.setDevelopReviewOrderType(developReviewOrderPo.getDevelopReviewOrderType());
        developReviewDetailVo.setPlatform(developReviewOrderPo.getPlatform());
        developReviewDetailVo.setSupplierCode(developReviewOrderPo.getSupplierCode());
        developReviewDetailVo.setSupplierName(developReviewOrderPo.getSupplierName());
        developReviewDetailVo.setSpu(developReviewOrderPo.getSpu());
        developReviewDetailVo.setPamphletTimes(developReviewOrderPo.getPamphletTimes());
        developReviewDetailVo.setCategoryId(developReviewOrderPo.getCategoryId());
        developReviewDetailVo.setCategory(developReviewOrderPo.getCategory());
        developReviewDetailVo.setPrenatalSampleOrderNo(developReviewOrderPo.getPrenatalSampleOrderNo());
        developReviewDetailVo.setReviewResult(developReviewOrderPo.getReviewResult());
        developReviewDetailVo.setDevelopReviewRelated(developReviewOrderPo.getDevelopReviewRelated());
        developReviewDetailVo.setSampleOrderNoJoining(sampleOrderNoJoining);
        //产前样的样品单
        if (ScmConstant.PROCESS_SUPPLIER_CODE.equals(developReviewOrderPo.getSupplierCode())
                && developChildOrderPo != null) {
            developReviewDetailVo.setStyleReferenceFileCodeList(childImageStyleMap.get(developChildOrderPo.getDevelopChildOrderId()));
            developReviewDetailVo.setColorReferenceFileCodeList(childImageColorMap.get(developChildOrderPo.getDevelopChildOrderId()));
        }

        if (null != developPamphletOrderPo) {
            developReviewDetailVo.setStyleReferenceFileCodeList(pamphletImageStyleMap.get(developPamphletOrderPo.getDevelopPamphletOrderId()));
            developReviewDetailVo.setColorReferenceFileCodeList(pamphletImageColorMap.get(developPamphletOrderPo.getDevelopPamphletOrderId()));
            developReviewDetailVo.setDemandDesc(developPamphletOrderPo.getDemandDesc());
        }

        final List<DevelopChildOrderAttrVo> developChildOrderAttrVoList = DevelopChildConverter.convertDevChildAttrPoToVo(developChildOrderAttrPoList);
        developReviewDetailVo.setDevelopChildOrderAttrList(developChildOrderAttrVoList);

        final List<DevelopReviewSampleVo> developReviewSampleList = Optional.ofNullable(developReviewSampleOrderPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final DevelopReviewSampleVo developReviewSampleVo = new DevelopReviewSampleVo();
                    developReviewSampleVo.setDevelopSampleOrderNo(po.getDevelopSampleOrderNo());
                    developReviewSampleVo.setGramWeight(po.getGramWeight());
                    developReviewSampleVo.setDevelopSampleDemand(po.getDevelopSampleDemand());
                    developReviewSampleVo.setDevelopSampleQuality(po.getDevelopSampleQuality());
                    developReviewSampleVo.setDevelopSampleMethod(po.getDevelopSampleMethod());
                    developReviewSampleVo.setDevelopSampleNewness(po.getDevelopSampleNewness());
                    developReviewSampleVo.setDevelopSampleStage(po.getDevelopSampleStage());
                    developReviewSampleVo.setDevelopReviewSampleSource(po.getDevelopReviewSampleSource());
                    developReviewSampleVo.setDevelopSampleDevOpinion(po.getDevelopSampleDevOpinion());
                    developReviewSampleVo.setDevelopSampleQltyOpinion(po.getDevelopSampleQltyOpinion());
                    developReviewSampleVo.setAbnormalHair(po.getAbnormalHair());
                    developReviewSampleVo.setFloatingHair(po.getFloatingHair());
                    developReviewSampleVo.setMeshCapFit(po.getMeshCapFit());
                    developReviewSampleVo.setHairFeel(po.getHairFeel());
                    if (developSampleOrderPoMap.containsKey(po.getDevelopSampleOrderNo())) {
                        DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoMap.get(po.getDevelopSampleOrderNo());
                        developReviewSampleVo.setDevelopSampleType(developSampleOrderPo.getDevelopSampleType());
                    }

                    // 产前样样品单取开发子单的样品类型
                    List<DevelopOrderPricePo> developOrderPricePoFilterDevelopList = developOrderPricePoList.stream()
                            .filter(developOrderPricePo -> DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                            .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(po.getDevelopChildOrderNo()))
                            .collect(Collectors.toList());
                    List<DevelopOrderPriceVo> developOrderPriceDevelopList = DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoFilterDevelopList);
                    developReviewSampleVo.setDevelopOrderPriceList(developOrderPriceDevelopList);

                    // 默认获取开发子单封样的样品单号大货价格
                    DevelopReviewSampleOrderPo developReviewSampleOrderPo = developChildOrderSealSampleMap.get(po.getDevelopChildOrderNo());
                    if (null != developReviewSampleOrderPo) {
                        List<DevelopOrderPricePo> developOrderPricePoFilterList = developOrderPricePoList.stream()
                                .filter(developOrderPricePo -> DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                                .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developReviewSampleOrderPo.getDevelopSampleOrderNo()))
                                .collect(Collectors.toList());
                        List<DevelopOrderPriceVo> developOrderPriceList = DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoFilterList);
                        developReviewSampleVo.setDevelopOrderPriceList(developOrderPriceList);
                    }

                    // 查询是否已经设置审版样品单渠道大货价格, 如果已经设置更新渠道价格
                    List<DevelopOrderPricePo> developOrderPricePoExistList = developOrderPricePoList.stream()
                            .filter(developOrderPricePo -> DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                            .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(po.getDevelopSampleOrderNo()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(developOrderPricePoExistList)) {
                        List<DevelopOrderPriceVo> developOrderPriceList = DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoExistList);
                        developReviewSampleVo.setDevelopOrderPriceList(developOrderPriceList);
                    }


                    final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = devSampleNoInfoPoListMap.get(po.getDevelopSampleOrderNo());
                    final List<DevelopReviewSampleInfoVo> developReviewSampleInfoList = convertReviewSampleInfoPoToVo(developReviewSampleOrderInfoPoList);
                    developReviewSampleVo.setDevelopReviewSampleInfoList(developReviewSampleInfoList);

                    developReviewSampleVo.setEffectFileCodeList(pamphletSampleImageEffectMap.get(po.getDevelopReviewSampleOrderId()));
                    developReviewSampleVo.setDetailFileCodeList(pamphletSampleImageDetailMap.get(po.getDevelopReviewSampleOrderId()));

                    return developReviewSampleVo;
                }).collect(Collectors.toList());
        developReviewDetailVo.setDevelopReviewSampleList(developReviewSampleList);

        List<DevelopPamphletRawDetailVo> developPamphletRawDetailList = Optional.ofNullable(developPamphletOrderRawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(rawPo -> {
                    final DevelopPamphletRawDetailVo developPamphletRawDetailVo = new DevelopPamphletRawDetailVo();
                    developPamphletRawDetailVo.setMaterialType(rawPo.getMaterialType());
                    developPamphletRawDetailVo.setSku(rawPo.getSku());
                    developPamphletRawDetailVo.setSkuCnt(rawPo.getSkuCnt());
                    developPamphletRawDetailVo.setSkuBatchCode(rawPo.getSkuBatchCode());
                    return developPamphletRawDetailVo;
                }).collect(Collectors.toList());
        developReviewDetailVo.setDevelopPamphletRawDetailList(developPamphletRawDetailList);

        return developReviewDetailVo;
    }

    public static List<DevelopReviewSampleInfoVo> convertReviewSampleInfoPoToVo(List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList) {
        return Optional.ofNullable(developReviewSampleOrderInfoPoList)
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
    }
}
