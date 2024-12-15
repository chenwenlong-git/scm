package com.hete.supply.scm.server.scm.defect.service.base;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DefectHandingSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingProgramme;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveType;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.defect.entity.bo.DefectHandlingPageBo;
import com.hete.supply.scm.server.scm.defect.entity.dto.DefectHandlingExchangeGoodsDto;
import com.hete.supply.scm.server.scm.defect.entity.dto.DefectHandlingScrappedDto;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.handler.WmsReceiptHandler;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.service.base.WmsMqBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.vo.SkuVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/6/22 14:04
 */
@Service
@RequiredArgsConstructor
@Validated
public class DefectBaseService {
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final DefectHandlingDao defectHandlingDao;
    private final WmsMqBaseService wmsMqBaseService;
    private final ScmImageBaseService scmImageBaseService;


    /**
     * 次品换货的MQ推送
     *
     * @param dto:
     * @param defectHandlingPoList:
     * @param skuBatchCodeMap:
     * @return void
     * @author ChenWenLong
     * @date 2023/6/28 10:21
     */
    public void exchangeGoodsMq(DefectHandlingExchangeGoodsDto dto,
                                List<DefectHandlingPo> defectHandlingPoList,
                                Map<String, List<SkuVo>> skuBatchCodeMap,
                                Map<String, String> defectHandlingNoSkuMap,
                                Map<String, BigDecimal> skuBatchPriceMap) {

        Map<String, DefectHandlingPo> defectHandlingPoMap = defectHandlingPoList.stream()
                .collect(Collectors.toMap(DefectHandlingPo::getDefectHandlingNo, defectHandlingPo -> defectHandlingPo));
        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        List<ReceiveOrderCreateMqDto.ReceiveOrderCreateItem> receiveOrderCreateItemList = new ArrayList<>();

        Map<String, List<DefectHandlingPo>> defectQcOrderNoMap = defectHandlingPoList.stream()
                .collect(Collectors.groupingBy(DefectHandlingPo::getQcOrderNo));

        // 更新WMS的批次码单价
        List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice> updateBatchCodeCostPriceList = new ArrayList<>();

        defectQcOrderNoMap.forEach((String qcOrderNo, List<DefectHandlingPo> poList) -> {
            final ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
            DefectHandlingPo defectHandlingPo = poList.get(0);
            receiveOrderCreateItem.setReceiveType(ReceiveType.CHANGE_GOODS);
            receiveOrderCreateItem.setScmBizNo(qcOrderNo);
            receiveOrderCreateItem.setSupplierCode(defectHandlingPoMap.get(defectHandlingPo.getDefectHandlingNo()).getSupplierCode());
            receiveOrderCreateItem.setSupplierName(defectHandlingPoMap.get(defectHandlingPo.getDefectHandlingNo()).getSupplierName());
            receiveOrderCreateItem.setWarehouseCode(dto.getWarehouseCode());
            receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
            receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
            receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);
            receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
            receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());

            receiveOrderCreateItem.setQcOrderNo(qcOrderNo);
            String defectHandlingNoString = poList.stream().sorted(Comparator.comparing(DefectHandlingPo::getDefectHandlingId).reversed()).map(DefectHandlingPo::getDefectHandlingNo).collect(Collectors.joining(","));
            String defectHandlingNoMd5 = DigestUtils.md5Hex(defectHandlingNoString);
            if (DefectHandlingType.PROCESS_DEFECT.equals(defectHandlingPoMap.get(defectHandlingPo.getDefectHandlingNo()).getDefectHandlingType())) {
                receiveOrderCreateItem.setScmBizReceiveOrderType(ScmBizReceiveOrderType.PROCESS_DEFECT_RECORD);
                receiveOrderCreateItem.setUnionKey(defectHandlingNoMd5 + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.PROCESS_DEFECT_RECORD.name());
            } else if (DefectHandlingType.MATERIAL_DEFECT.equals(defectHandlingPoMap.get(defectHandlingPo.getDefectHandlingNo()).getDefectHandlingType())) {
                receiveOrderCreateItem.setScmBizReceiveOrderType(ScmBizReceiveOrderType.MATERIAL_DEFECT);
                receiveOrderCreateItem.setUnionKey(defectHandlingNoMd5 + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.MATERIAL_DEFECT.name());
            } else {
                receiveOrderCreateItem.setScmBizReceiveOrderType(ScmBizReceiveOrderType.INSIDE_CHECK);
                receiveOrderCreateItem.setUnionKey(defectHandlingNoMd5 + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.INSIDE_CHECK.name());
            }

            final LocalDateTime now = LocalDateTime.now();
            receiveOrderCreateItem.setSendTime(now);
            receiveOrderCreateItem.setPlaceOrderTime(now);

            List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> detailList = new ArrayList<>();

            for (DefectHandlingPo handlingPo : poList) {
                final String sku = defectHandlingNoSkuMap.get(handlingPo.getDefectHandlingNo());
                final String skuBatchCode = skuBatchCodeMap.get(handlingPo.getDefectHandlingNo()).get(0).getBatchCode();
                ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
                receiveOrderDetail.setBatchCode(skuBatchCode);
                receiveOrderDetail.setOldSkuCode(handlingPo.getSku());
                receiveOrderDetail.setSkuCode(sku);
                receiveOrderDetail.setPurchaseAmount(defectHandlingPoMap.get(handlingPo.getDefectHandlingNo()).getNotPassCnt());
                receiveOrderDetail.setDeliveryAmount(defectHandlingPoMap.get(handlingPo.getDefectHandlingNo()).getNotPassCnt());
                receiveOrderDetail.setDeliverOrderNo(handlingPo.getReceiveOrderNo());
                receiveOrderDetail.setDefectHandlingNo(handlingPo.getDefectHandlingNo());
                receiveOrderDetail.setPlatCode(handlingPo.getPlatform());
                detailList.add(receiveOrderDetail);

                handlingPo.setHandleSku(sku);
                handlingPo.setHandleSkuBatchCode(skuBatchCode);


                // 更新WMS的批次码单价
                UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice updateBatchCodeCostPrice = new UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice();
                updateBatchCodeCostPrice.setBatchCode(receiveOrderDetail.getBatchCode());
                updateBatchCodeCostPrice.setPrice(skuBatchPriceMap.get(handlingPo.getSkuBatchCode()));
                updateBatchCodeCostPriceList.add(updateBatchCodeCostPrice);

            }

            receiveOrderCreateItem.setDetailList(detailList);

            receiveOrderCreateItemList.add(receiveOrderCreateItem);

        });

        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(receiveOrderCreateItemList);
        receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(ScmConstant.DEFECT_HANDLING_NO_PREFIX));
        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);

        if (CollectionUtils.isNotEmpty(updateBatchCodeCostPriceList)) {
            UpdateBatchCodePriceDto updateBatchCodePriceDto = new UpdateBatchCodePriceDto();

            Map<String, List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice>> groupedByBatchCode = updateBatchCodeCostPriceList.stream()
                    .collect(Collectors.groupingBy(UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice::getBatchCode));

            List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice> averagedList = groupedByBatchCode.entrySet().stream()
                    .map(entry -> {
                        String batchCode = entry.getKey();
                        List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice> prices = entry.getValue();
                        BigDecimal total = prices.stream()
                                .map(UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal averagePrice = total.divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.DOWN);

                        UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice updateBatchCodeCostPrice = new UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice();
                        updateBatchCodeCostPrice.setBatchCode(batchCode);
                        updateBatchCodeCostPrice.setPrice(averagePrice);
                        return updateBatchCodeCostPrice;

                    })
                    .collect(Collectors.toList());

            updateBatchCodePriceDto.setBatchCodePriceList(averagedList);
            wmsMqBaseService.execSendUpdateBatchCodePriceMq(updateBatchCodePriceDto);
        }

        //更新数据
        this.updateDefectHandlingPoList(defectHandlingPoList, DefectHandlingProgramme.EXCHANGE_GOODS, null);

    }

    /**
     * 次品报废的MQ推送
     *
     * @param dto:
     * @param defectHandlingPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/6/28 09:53
     */
    public void scrappedMq(DefectHandlingScrappedDto dto, List<DefectHandlingPo> defectHandlingPoList) {

        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        List<ReceiveOrderCreateMqDto.ReceiveOrderCreateItem> receiveOrderCreateItemList = new ArrayList<>();

        Map<String, List<DefectHandlingPo>> defectQcOrderNoMap = defectHandlingPoList.stream().collect(Collectors.groupingBy(DefectHandlingPo::getQcOrderNo));

        defectQcOrderNoMap.forEach((String qcOrderNo, List<DefectHandlingPo> poList) -> {
            final ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
            DefectHandlingPo defectHandlingPo = poList.get(0);
            receiveOrderCreateItem.setReceiveType(ReceiveType.DEFECTIVE_PROCESS_PRODUCT);
            receiveOrderCreateItem.setScmBizNo(qcOrderNo);
            receiveOrderCreateItem.setSupplierCode(defectHandlingPo.getSupplierCode());
            receiveOrderCreateItem.setSupplierName(defectHandlingPo.getSupplierName());
            receiveOrderCreateItem.setWarehouseCode(dto.getWarehouseCode());
            receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
            receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
            receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);
            receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
            receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());
            String defectHandlingNoString = poList.stream().sorted(Comparator.comparing(DefectHandlingPo::getDefectHandlingId).reversed()).map(DefectHandlingPo::getDefectHandlingNo).collect(Collectors.joining(","));
            String defectHandlingNoMd5 = DigestUtils.md5Hex(defectHandlingNoString);
            if (DefectHandlingType.PROCESS_DEFECT.equals(defectHandlingPo.getDefectHandlingType())) {
                receiveOrderCreateItem.setScmBizReceiveOrderType(ScmBizReceiveOrderType.PROCESS_DEFECT_RECORD);
                receiveOrderCreateItem.setUnionKey(defectHandlingNoMd5 + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.PROCESS_DEFECT_RECORD.name());
            } else if (DefectHandlingType.MATERIAL_DEFECT.equals(defectHandlingPo.getDefectHandlingType())) {
                receiveOrderCreateItem.setScmBizReceiveOrderType(ScmBizReceiveOrderType.MATERIAL_DEFECT);
                receiveOrderCreateItem.setUnionKey(defectHandlingNoMd5 + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.MATERIAL_DEFECT.name());
            } else {
                receiveOrderCreateItem.setScmBizReceiveOrderType(ScmBizReceiveOrderType.INSIDE_CHECK);
                receiveOrderCreateItem.setUnionKey(defectHandlingNoMd5 + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.INSIDE_CHECK.name());
            }
            receiveOrderCreateItem.setQcOrderNo(qcOrderNo);


            final LocalDateTime now = LocalDateTime.now();
            receiveOrderCreateItem.setSendTime(now);
            receiveOrderCreateItem.setPlaceOrderTime(now);


            List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> detailList = new ArrayList<>();

            for (DefectHandlingPo handlingPo : poList) {
                ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
                receiveOrderDetail.setBatchCode(handlingPo.getSkuBatchCode());
                receiveOrderDetail.setOldSkuCode(handlingPo.getSku());
                receiveOrderDetail.setSkuCode(handlingPo.getSku());
                receiveOrderDetail.setPurchaseAmount(handlingPo.getNotPassCnt());
                receiveOrderDetail.setDeliveryAmount(handlingPo.getNotPassCnt());
                receiveOrderDetail.setDeliverOrderNo(handlingPo.getReceiveOrderNo());
                receiveOrderDetail.setDefectHandlingNo(handlingPo.getDefectHandlingNo());
                receiveOrderDetail.setPlatCode(handlingPo.getPlatform());
                detailList.add(receiveOrderDetail);
            }

            receiveOrderCreateItem.setDetailList(detailList);
            receiveOrderCreateItemList.add(receiveOrderCreateItem);

        });
        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(receiveOrderCreateItemList);
        receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(ScmConstant.DEFECT_HANDLING_NO_PREFIX));

        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);

        //更新数据
        this.updateDefectHandlingPoList(defectHandlingPoList, DefectHandlingProgramme.SCRAPPED, null);

    }

    /**
     * 次品换货更新确认记录的状态
     *
     * @param defectHandlingPoList:
     * @param defectHandlingProgramme:
     * @return void
     * @author ChenWenLong
     * @date 2023/6/28 10:25
     */
    public void updateDefectHandlingPoList(List<DefectHandlingPo> defectHandlingPoList,
                                           DefectHandlingProgramme defectHandlingProgramme,
                                           String returnOrderNo) {
        defectHandlingPoList.forEach(po -> {
            po.setDefectHandlingStatus(po.getDefectHandlingStatus().toConfirmed());
            po.setDefectHandlingProgramme(defectHandlingProgramme);
            po.setConfirmUser(GlobalContext.getUserKey());
            po.setConfirmUsername(GlobalContext.getUsername());
            po.setConfirmTime(LocalDateTimeUtil.now());
            po.setReturnOrderNo(returnOrderNo);
            po.setRelatedOrderNo(returnOrderNo);
        });
        defectHandlingDao.updateBatchByIdVersion(defectHandlingPoList);
    }

    /**
     * 更新关联单号
     *
     * @param defectHandlingPoList
     * @param relatedOrderNo
     */
    public void updateDefectHandlingRelatedNo(List<DefectHandlingPo> defectHandlingPoList,
                                              String relatedOrderNo) {
        defectHandlingPoList = defectHandlingPoList.stream()
                .peek(po -> po.setRelatedOrderNo(relatedOrderNo)).collect(Collectors.toList());
        defectHandlingDao.updateBatchByIdVersion(defectHandlingPoList);
    }


    /**
     * 同步WMS的收货单信息
     *
     * @param message:
     * @return void
     * @author ChenWenLong
     * @date 2023/6/28 13:59
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncReceiptMsg(ReceiveOrderChangeMqDto message) {
        List<ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto> list = message.getPurchaseReceiptSyncItemList();
        String receiveOrderNo = message.getReceiveOrderNo();
        List<DefectHandlingPo> receiveOrderNoPoList = defectHandlingDao.getListByRelatedOrderNo(receiveOrderNo);
        //如果已绑定发货单就不做逻辑处理
        if (CollectionUtils.isNotEmpty(receiveOrderNoPoList)) {
            return;
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new BizException("次品记录为空，wms推送收货单的mq数据存在问题！");
        }
        List<String> defectHandlingNoList = list.stream().map(ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto::getDefectHandlingNo).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(defectHandlingNoList)) {
            throw new BizException("次品记录为空，wms推送收货单的mq数据存在问题！");
        }
        List<DefectHandlingPo> defectHandlingPoList = defectHandlingDao.getByDefectHandlingNos(defectHandlingNoList);
        if (defectHandlingNoList.stream().distinct().count() != defectHandlingPoList.size()) {
            throw new BizException("存在查找不到对应的次品记录，wms推送收货单的mq数据存在问题！");
        }
        for (DefectHandlingPo defectHandlingPo : defectHandlingPoList) {
            defectHandlingPo.setRelatedOrderNo(message.getReceiveOrderNo());
        }
        defectHandlingDao.updateBatchByIdVersion(defectHandlingPoList);
    }

    /**
     * 导出和列表共用
     *
     * @param dto:
     * @return DefectHandlingPageBo
     * @author ChenWenLong
     * @date 2024/3/28 15:17
     */
    public DefectHandlingPageBo searchDefectPage(DefectHandingSearchDto dto) {
        final IPage<DefectHandlingPo> pageResult = defectHandlingDao.searchDefect(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<DefectHandlingPo> records = pageResult.getRecords();
        final List<Long> idList = records.stream()
                .map(DefectHandlingPo::getDefectHandlingId)
                .collect(Collectors.toList());

        final Map<Long, List<String>> idFileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEFECT_HANDLING, idList);
        DefectHandlingPageBo defectHandlingPageBo = new DefectHandlingPageBo();
        defectHandlingPageBo.setPageResult(pageResult);
        defectHandlingPageBo.setIdFileCodeMap(idFileCodeMap);
        return defectHandlingPageBo;
    }

}
