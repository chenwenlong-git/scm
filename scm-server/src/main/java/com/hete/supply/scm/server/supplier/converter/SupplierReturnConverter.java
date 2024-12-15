package com.hete.supply.scm.server.supplier.converter;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingType;
import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderBo;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderItemBo;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderResultBo;
import com.hete.supply.scm.server.supplier.entity.dto.ReceiveOrderRejectMqDto;
import com.hete.supply.scm.server.supplier.entity.dto.ReturnOrderMqDto;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnPrintVo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * t
 *
 * @author weiwenxin
 * @date 2022/12/14 14:58
 */
public class SupplierReturnConverter {

    public static ReturnOrderResultBo purchaseBoToPo(ReturnOrderBo returnOrderBo) {

        List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = new ArrayList<>();

        final PurchaseReturnOrderPo purchaseReturnOrderPo = new PurchaseReturnOrderPo();
        purchaseReturnOrderPo.setReturnOrderNo(returnOrderBo.getReturnOrderNo());
        purchaseReturnOrderPo.setReturnOrderStatus(ReturnOrderStatus.WAIT_MOVING);
        purchaseReturnOrderPo.setExpectedReturnCnt(returnOrderBo.getExpectedReturnCnt());
        purchaseReturnOrderPo.setSupplierCode(returnOrderBo.getSupplierCode());
        purchaseReturnOrderPo.setSupplierName(returnOrderBo.getSupplierName());
        purchaseReturnOrderPo.setReturnType(returnOrderBo.getReturnType());
        purchaseReturnOrderPo.setReturnBizNo(returnOrderBo.getReturnBizNo());
        purchaseReturnOrderPo.setReturnCreateUser(returnOrderBo.getOperator());
        purchaseReturnOrderPo.setReturnCreateUsername(returnOrderBo.getOperatorUsername());
        purchaseReturnOrderPo.setPurchaseChildOrderNo(returnOrderBo.getPurchaseChildOrderNo());
        purchaseReturnOrderPo.setPlatform(returnOrderBo.getPlatform());

        final List<ReturnOrderItemBo> purchaseReturnOrderItemBoList = returnOrderBo.getPurchaseReturnOrderItemBoList();
        if (CollectionUtils.isEmpty(purchaseReturnOrderItemBoList)) {
            return ReturnOrderResultBo.builder()
                    .purchaseReturnOrderPo(purchaseReturnOrderPo)
                    .build();
        }

        purchaseReturnOrderItemBoList.forEach(itemBo -> {
            final PurchaseReturnOrderItemPo purchaseReturnOrderItemPo = new PurchaseReturnOrderItemPo();
            purchaseReturnOrderItemPo.setReturnOrderNo(returnOrderBo.getReturnOrderNo());
            purchaseReturnOrderItemPo.setSku(itemBo.getSku());
            purchaseReturnOrderItemPo.setSkuEncode(itemBo.getSkuEncode());
            purchaseReturnOrderItemPo.setSkuBatchCode(itemBo.getSkuBatchCode());
            purchaseReturnOrderItemPo.setReturnBizNo(itemBo.getReturnBizNo());
            purchaseReturnOrderItemPo.setExpectedReturnCnt(itemBo.getExpectedReturnCnt());
            purchaseReturnOrderItemPo.setSettlePrice(itemBo.getSettlePrice());
            purchaseReturnOrderItemPo.setDeductPrice(itemBo.getDeductPrice());
            purchaseReturnOrderItemPo.setCreateUser(returnOrderBo.getOperator());
            purchaseReturnOrderItemPo.setBizDetailId(itemBo.getBizDetailId());

            purchaseReturnOrderItemPoList.add(purchaseReturnOrderItemPo);
        });

        return ReturnOrderResultBo.builder()
                .purchaseReturnOrderPo(purchaseReturnOrderPo)
                .purchaseReturnOrderItemPoList(purchaseReturnOrderItemPoList)
                .build();
    }

    public static ReturnOrderMqDto returnOrderBoToMqDto(ReturnOrderBo returnOrderBo) {

        final List<ReturnOrderItemBo> purchaseReturnOrderItemBoList = returnOrderBo.getPurchaseReturnOrderItemBoList();

        List<ReturnOrderMqDto.ReturnOrderMqItemDto> returnOrderMqItemDtoList = purchaseReturnOrderItemBoList.stream()
                .map(itemBo -> {
                    final ReturnOrderMqDto.ReturnOrderMqItemDto returnOrderMqItemDto = new ReturnOrderMqDto.ReturnOrderMqItemDto();
                    returnOrderMqItemDto.setSku(itemBo.getSku());
                    returnOrderMqItemDto.setSkuEncode(itemBo.getSkuEncode());
                    returnOrderMqItemDto.setSkuBatchCode(itemBo.getSkuBatchCode());
                    returnOrderMqItemDto.setExpectedReturnCnt(itemBo.getExpectedReturnCnt());
                    returnOrderMqItemDto.setSettlePrice(itemBo.getSettlePrice());
                    returnOrderMqItemDto.setBizDetailId(itemBo.getBizDetailId());
                    returnOrderMqItemDto.setQcOrderNo(itemBo.getQcOrderNo());
                    returnOrderMqItemDto.setReceiveOrderNo(returnOrderBo.getReceiveOrderNo());
                    returnOrderMqItemDto.setProcessOrderNo(itemBo.getProcessOrderNo());
                    returnOrderMqItemDto.setDefectHandlingNo(itemBo.getDefectHandlingNo());

                    return returnOrderMqItemDto;
                }).collect(Collectors.toList());

        final ReturnOrderMqDto returnOrderMqDto = new ReturnOrderMqDto();
        returnOrderMqDto.setExpectedReturnCnt(returnOrderBo.getExpectedReturnCnt());
        returnOrderMqDto.setSupplierCode(returnOrderBo.getSupplierCode());
        returnOrderMqDto.setSupplierName(returnOrderBo.getSupplierName());
        returnOrderMqDto.setReturnType(returnOrderBo.getReturnType());
        returnOrderMqDto.setPurchaseReturnOrderItemDtoList(returnOrderMqItemDtoList);
        returnOrderMqDto.setReturnOrderNo(returnOrderBo.getReturnOrderNo());
        returnOrderMqDto.setOperator(returnOrderBo.getOperator());
        returnOrderMqDto.setOperatorName(returnOrderBo.getOperatorUsername());
        returnOrderMqDto.setWarehouseCode(returnOrderBo.getWarehouseCode());

        return returnOrderMqDto;
    }

    public static ReturnOrderBo defectHandlingPoToReturnBo(@NotEmpty List<DefectHandlingPo> defectHandlingPoList,
                                                           Map<String, String> skuEncodeMap,
                                                           SupplierPo supplierPo) {
        final List<ReturnOrderItemBo> returnOrderItemBoList = defectHandlingPoList.stream()
                .map(po -> {
                    final ReturnOrderItemBo returnOrderItemBo = new ReturnOrderItemBo();
                    returnOrderItemBo.setSku(po.getSku());
                    returnOrderItemBo.setSkuEncode(skuEncodeMap.get(po.getSku()));
                    returnOrderItemBo.setSkuBatchCode(po.getSkuBatchCode());
                    returnOrderItemBo.setExpectedReturnCnt(po.getNotPassCnt());
                    returnOrderItemBo.setSettlePrice(po.getSettlePrice());
                    returnOrderItemBo.setBizDetailId(po.getBizDetailId());
                    returnOrderItemBo.setQcOrderNo(po.getQcOrderNo());
                    returnOrderItemBo.setReturnBizNo(po.getDefectHandlingNo());
                    returnOrderItemBo.setDefectHandlingNo(po.getDefectHandlingNo());
                    if (DefectHandlingType.PROCESS_DEFECT.equals(po.getDefectHandlingType())) {
                        returnOrderItemBo.setProcessOrderNo(po.getDefectBizNo());
                    }

                    return returnOrderItemBo;
                }).collect(Collectors.toList());

        final ReturnOrderBo returnOrderBo = new ReturnOrderBo();
        final DefectHandlingPo defectHandlingPo = defectHandlingPoList.get(0);
        // 预计退货总数
        final int expectedReturnCnt = defectHandlingPoList.stream()
                .mapToInt(DefectHandlingPo::getNotPassCnt)
                .sum();
        returnOrderBo.setExpectedReturnCnt(expectedReturnCnt);
        // 针对原料次品加工没有供应商逻辑处理
        if (supplierPo != null) {
            returnOrderBo.setSupplierCode(supplierPo.getSupplierCode());
            returnOrderBo.setSupplierName(supplierPo.getSupplierName());
        } else {
            returnOrderBo.setSupplierCode(defectHandlingPo.getSupplierCode());
            returnOrderBo.setSupplierName(defectHandlingPo.getSupplierName());
        }
        returnOrderBo.setReturnType(ReturnType.defectHandlingTypeConvert(defectHandlingPo.getDefectHandlingType()));
        returnOrderBo.setPurchaseReturnOrderItemBoList(returnOrderItemBoList);
        returnOrderBo.setReceiveOrderNo(defectHandlingPo.getReceiveOrderNo());
        returnOrderBo.setOperator(GlobalContext.getUserKey());
        returnOrderBo.setOperatorUsername(GlobalContext.getUsername());
        returnOrderBo.setReturnBizNo(defectHandlingPo.getDefectBizNo());
        returnOrderBo.setWarehouseCode(defectHandlingPo.getWarehouseCode());

        return returnOrderBo;
    }


    /**
     * 打印退货单
     *
     * @param purchaseReturnOrderPoList
     * @param returnItemMap
     * @return
     */
    public static List<PurchaseReturnPrintVo> returnToPrintVo(@NotEmpty List<PurchaseReturnOrderPo> purchaseReturnOrderPoList,
                                                              Map<String, List<PurchaseReturnOrderItemPo>> returnItemMap) {
        return purchaseReturnOrderPoList.stream().map(po -> {
            PurchaseReturnPrintVo purchaseReturnPrintVo = new PurchaseReturnPrintVo();
            purchaseReturnPrintVo.setPurchaseReturnOrderId(po.getPurchaseReturnOrderId());
            purchaseReturnPrintVo.setVersion(po.getVersion());
            purchaseReturnPrintVo.setReturnOrderNo(po.getReturnOrderNo());
            purchaseReturnPrintVo.setReturnOrderStatus(po.getReturnOrderStatus());
            purchaseReturnPrintVo.setLogistics(po.getLogistics());
            purchaseReturnPrintVo.setTrackingNo(po.getTrackingNo());
            purchaseReturnPrintVo.setSupplierCode(po.getSupplierCode());
            purchaseReturnPrintVo.setSupplierName(po.getSupplierName());
            purchaseReturnPrintVo.setNote(po.getNote());
            purchaseReturnPrintVo.setPrintUser(GlobalContext.getUserKey());
            purchaseReturnPrintVo.setPrintUsername(GlobalContext.getUsername());
            purchaseReturnPrintVo.setPrintTime(new DateTime().toLocalDateTime());
            purchaseReturnPrintVo.setRealityReturnCnt(po.getRealityReturnCnt());
            purchaseReturnPrintVo.setReturnType(po.getReturnType());

            List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = returnItemMap.get(po.getReturnOrderNo());
            if (CollectionUtils.isNotEmpty(purchaseReturnOrderItemPoList)) {
                List<PurchaseReturnPrintVo.PurchaseReturnItem> returnItemList = purchaseReturnOrderItemPoList.stream().map(item -> {
                    PurchaseReturnPrintVo.PurchaseReturnItem purchaseReturnItem = new PurchaseReturnPrintVo.PurchaseReturnItem();
                    purchaseReturnItem.setPurchaseReturnOrderItemId(item.getPurchaseReturnOrderItemId());
                    purchaseReturnItem.setReturnBizNo(item.getReturnBizNo());
                    purchaseReturnItem.setRealityReturnCnt(item.getRealityReturnCnt());
                    purchaseReturnItem.setSku(item.getSku());
                    purchaseReturnItem.setSkuBatchCode(item.getSkuBatchCode());
                    purchaseReturnItem.setSkuEncode(item.getSkuEncode());
                    purchaseReturnItem.setVersion(item.getVersion());
                    return purchaseReturnItem;
                }).collect(Collectors.toList());
                purchaseReturnPrintVo.setPurchaseReturnItemList(returnItemList);
            }
            return purchaseReturnPrintVo;
        }).collect(Collectors.toList());
    }


    public static ReturnOrderStatus updateReturnMsgToNextStatus(Integer returnCnt, WmsEnum.ReturnState returnState) {
        if (returnCnt == 0 && WmsEnum.ReturnState.HANDLED.equals(returnState)) {
            return ReturnOrderStatus.LOST;
        }

        if (WmsEnum.ReturnState.WAIT_HAND_OVER.equals(returnState)) {
            return ReturnOrderStatus.WAIT_MOVING;
        }
        if (WmsEnum.ReturnState.TO_BE_HANDLED.equals(returnState)) {
            return ReturnOrderStatus.WAIT_HANDLE;
        }
        if (WmsEnum.ReturnState.HANDLED.equals(returnState)) {
            return ReturnOrderStatus.WAIT_RECEIVE;
        }

        throw new BizException("错误的退货单状态：{}，操作失败！", returnState.getRemark());
    }

    public static ReturnOrderBo receiveDefectDtoToBo(ReceiveOrderRejectMqDto dto, ReturnType returnType,
                                                     Map<String, BigDecimal> skuBatchCodePriceMap,
                                                     PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                                                     String platform) {
        final List<ReceiveOrderRejectMqDto.ReturnGood> returnGoodList = dto.getReturnGoodList();
        if (CollectionUtils.isEmpty(returnGoodList)) {
            throw new BizException("wms推送拒收信息中拒收列表为空");
        }
        final List<ReturnOrderItemBo> returnOrderItemBoList = returnGoodList.stream()
                .map(returnGood -> {
                    final ReturnOrderItemBo returnOrderItemBo = new ReturnOrderItemBo();
                    returnOrderItemBo.setSku(returnGood.getSkuCode());
                    returnOrderItemBo.setSkuBatchCode(returnGood.getBatchCode());
                    returnOrderItemBo.setExpectedReturnCnt(returnGood.getReturnAmount());
                    returnOrderItemBo.setBizDetailId(returnGood.getBizDetailId());
                    BigDecimal settlePrice = Optional.ofNullable(skuBatchCodePriceMap.get(returnGood.getBatchCode())).orElse(BigDecimal.ZERO);
                    returnOrderItemBo.setSettlePrice(settlePrice);
                    returnOrderItemBo.setReturnBizNo(dto.getReceiveOrderNo());
                    returnOrderItemBo.setDefectHandlingNo(returnGood.getBizDetailId() + "");
                    return returnOrderItemBo;
                }).collect(Collectors.toList());


        final ReturnOrderBo returnOrderBo = new ReturnOrderBo();
        returnOrderBo.setExpectedReturnCnt(dto.getTotalReturnAmount());
        returnOrderBo.setSupplierCode(dto.getSupplierCode());
        returnOrderBo.setSupplierName(dto.getSupplierName());
        returnOrderBo.setReturnType(returnType);
        returnOrderBo.setPurchaseReturnOrderItemBoList(returnOrderItemBoList);
        returnOrderBo.setReturnBizNo(dto.getScmBizNo());
        returnOrderBo.setOperator(dto.getOperator());
        returnOrderBo.setOperatorUsername(dto.getOperatorName());
        returnOrderBo.setReceiveOrderNo(dto.getReceiveOrderNo());
        returnOrderBo.setPurchaseChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        returnOrderBo.setWarehouseCode(purchaseDeliverOrderPo.getWarehouseCode());
        returnOrderBo.setPlatform(platform);

        return returnOrderBo;
    }
}
