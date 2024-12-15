package com.hete.supply.scm.server.scm.defect.converter;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingStatus;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingType;
import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import com.hete.supply.scm.api.scm.entity.vo.DefectHandingExportVo;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.defect.entity.bo.DefectHandlingCreateBo;
import com.hete.supply.scm.server.scm.defect.entity.dto.DefectCompromiseDto;
import com.hete.supply.scm.server.scm.entity.dto.WmsOnShelvesMqDto;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.entity.vo.DefectHandingSearchVo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/6/21 14:35
 */
@Slf4j
public class DefectHandlingConverter {

    public static List<DefectHandingSearchVo> defectHandlingPoToVo(List<DefectHandlingPo> records,
                                                                   Map<Long, List<String>> idFileCodeMap,
                                                                   Map<String, String> receiveOrderNoWarehouseNameMap,
                                                                   Map<String, String> batchCodeSupplierCodeMap) {

        return Optional.ofNullable(records)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final DefectHandingSearchVo defectHandingSearchVo = new DefectHandingSearchVo();
                    defectHandingSearchVo.setDefectHandlingNo(po.getDefectHandlingNo());
                    defectHandingSearchVo.setDefectHandlingStatus(po.getDefectHandlingStatus());
                    defectHandingSearchVo.setDefectBizNo(po.getDefectBizNo());
                    defectHandingSearchVo.setDefectHandlingType(po.getDefectHandlingType());
                    defectHandingSearchVo.setDefectHandlingProgramme(po.getDefectHandlingProgramme());
                    defectHandingSearchVo.setRelatedOrderNo(po.getRelatedOrderNo());
                    defectHandingSearchVo.setSku(po.getSku());
                    defectHandingSearchVo.setSkuBatchCode(po.getSkuBatchCode());
                    defectHandingSearchVo.setSupplierCode(po.getSupplierCode());
                    defectHandingSearchVo.setSupplierName(po.getSupplierName());
                    defectHandingSearchVo.setReturnCnt(po.getReturnCnt());
                    defectHandingSearchVo.setAdverseReason(po.getAdverseReason());
                    defectHandingSearchVo.setConfirmUser(po.getConfirmUser());
                    defectHandingSearchVo.setConfirmUsername(po.getConfirmUsername());
                    defectHandingSearchVo.setConfirmTime(po.getConfirmTime());
                    defectHandingSearchVo.setCreateUser(po.getDefectCreateUser());
                    defectHandingSearchVo.setCreateUsername(po.getDefectCreateUsername());
                    defectHandingSearchVo.setFileCodeList(idFileCodeMap.get(po.getDefectHandlingId()));
                    defectHandingSearchVo.setSettlePrice(po.getSettlePrice());
                    defectHandingSearchVo.setCreateTime(po.getCreateTime());
                    defectHandingSearchVo.setFailReason(po.getFailReason());
                    defectHandingSearchVo.setWarehouseCode(po.getWarehouseCode());
                    defectHandingSearchVo.setQcOrderNo(po.getQcOrderNo());
                    defectHandingSearchVo.setQcCnt(po.getQcCnt());
                    defectHandingSearchVo.setNotPassCnt(po.getNotPassCnt());
                    defectHandingSearchVo.setReceiveOrderNo(po.getReceiveOrderNo());
                    defectHandingSearchVo.setReturnOrderNo(po.getReturnOrderNo());
                    defectHandingSearchVo.setRelatedWarehouseName(receiveOrderNoWarehouseNameMap.getOrDefault(po.getRelatedOrderNo(), ""));
                    if (DefectHandlingType.BULK_DEFECT.equals(po.getDefectHandlingType())) {
                        defectHandingSearchVo.setBatchCodeSupplierCode(po.getSupplierCode());
                    } else {
                        defectHandingSearchVo.setBatchCodeSupplierCode(batchCodeSupplierCodeMap.get(po.getSkuBatchCode()));
                    }

                    defectHandingSearchVo.setHandleSku(po.getHandleSku());
                    defectHandingSearchVo.setHandleSkuBatchCode(po.getHandleSkuBatchCode());
                    defectHandingSearchVo.setPlatform(po.getPlatform());

                    return defectHandingSearchVo;
                }).collect(Collectors.toList());
    }

    public static List<DefectHandlingPo> defectHandlingBoToPo(List<DefectHandlingCreateBo> defectHandlingCreateBoList,
                                                              Map<String, SupplierPo> supplierCodePoMap) {
        return Optional.ofNullable(defectHandlingCreateBoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(bo -> {
                    final DefectHandlingPo defectHandlingPo = new DefectHandlingPo();
                    defectHandlingPo.setDefectHandlingNo(bo.getDefectHandlingNo());
                    defectHandlingPo.setDefectHandlingStatus(DefectHandlingStatus.WAIT_CONFIRM);
                    defectHandlingPo.setDefectHandlingType(bo.getDefectHandlingType());
                    defectHandlingPo.setDefectBizNo(bo.getDefectBizNo());
                    defectHandlingPo.setQcOrderNo(bo.getQcOrderNo());
                    defectHandlingPo.setBizDetailId(bo.getBizDetailId());
                    defectHandlingPo.setRelatedOrderNo(bo.getRelatedOrderNo());
                    defectHandlingPo.setSupplierCode(bo.getSupplierCode());
                    final SupplierPo supplierPo = supplierCodePoMap.get(bo.getSupplierCode());
                    if (null != supplierPo) {
                        defectHandlingPo.setSupplierName(supplierPo.getSupplierName());
                    }
                    defectHandlingPo.setSku(bo.getSku());
                    defectHandlingPo.setSkuBatchCode(bo.getSkuBatchCode());
                    defectHandlingPo.setQcCnt(bo.getQcCnt());
                    defectHandlingPo.setPassCnt(bo.getPassCnt());
                    defectHandlingPo.setNotPassCnt(bo.getNotPassCnt());
                    defectHandlingPo.setAdverseReason(bo.getAdverseReason());
                    defectHandlingPo.setSettlePrice(bo.getSettlePrice());
                    defectHandlingPo.setDefectCreateUser(bo.getDefectCreateUser());
                    defectHandlingPo.setDefectCreateUsername(bo.getDefectCreateUsername());
                    defectHandlingPo.setReceiveOrderNo(bo.getReceiveOrderNo());
                    defectHandlingPo.setWarehouseCode(bo.getWarehouseCode());
                    defectHandlingPo.setPlatform(bo.getPlatform());

                    return defectHandlingPo;
                }).collect(Collectors.toList());
    }


    public static List<DefectHandlingCreateBo> qcDefectMqDtoToBo(QcOrderPo qcOrderPo,
                                                                 Map<String, BigDecimal> skuBatchCodePriceMap,
                                                                 List<QcDetailPo> qcDetailPoList,
                                                                 QcReceiveOrderPo qcReceiveOrderPo,
                                                                 Integer totalPassAmount,
                                                                 Map<String, String> batchCodeSupplierCodeMap,
                                                                 QcOriginProperty qcOriginProperty) {

        return qcDetailPoList.stream()
                .map(qcDetailPo -> {
                    final DefectHandlingCreateBo defectHandlingCreateBo = new DefectHandlingCreateBo();
                    if (QcOriginProperty.REPAIR.equals(qcOriginProperty)) {
                        defectHandlingCreateBo.setDefectHandlingType(DefectHandlingType.REPAIR);
                    } else {
                        defectHandlingCreateBo.setDefectHandlingType(DefectHandlingConverter.convertToDefectHandlingType(qcReceiveOrderPo.getReceiveType(),
                                WmsEnum.ReturnType.QC_NOT_PASSED));
                    }

                    defectHandlingCreateBo.setQcOrderNo(qcOrderPo.getQcOrderNo());
                    defectHandlingCreateBo.setBizDetailId(qcDetailPo.getQcDetailId());
                    if (DefectHandlingType.MATERIAL_DEFECT.equals(defectHandlingCreateBo.getDefectHandlingType())
                            || DefectHandlingType.INSIDE_DEFECT.equals(defectHandlingCreateBo.getDefectHandlingType())) {
                        defectHandlingCreateBo.setSupplierCode(batchCodeSupplierCodeMap.get(qcDetailPo.getBatchCode()));
                    } else {
                        defectHandlingCreateBo.setSupplierCode(qcReceiveOrderPo.getSupplierCode());
                    }
                    defectHandlingCreateBo.setSku(qcDetailPo.getSkuCode());
                    defectHandlingCreateBo.setSkuBatchCode(qcDetailPo.getBatchCode());
                    defectHandlingCreateBo.setQcCnt(qcOrderPo.getQcAmount());
                    defectHandlingCreateBo.setPassCnt(totalPassAmount);
                    defectHandlingCreateBo.setNotPassCnt(qcDetailPo.getNotPassAmount());
                    defectHandlingCreateBo.setAdverseReason(qcDetailPo.getQcNotPassedReason());
                    defectHandlingCreateBo.setDefectCreateUser(GlobalContext.getUserKey());
                    defectHandlingCreateBo.setDefectCreateUsername(GlobalContext.getUsername());
                    defectHandlingCreateBo.setConfirmTime(qcOrderPo.getTaskFinishTime());
                    defectHandlingCreateBo.setReceiveOrderNo(qcReceiveOrderPo.getReceiveOrderNo());
                    defectHandlingCreateBo.setFileCodeList(FormatStringUtil.string2List(qcDetailPo.getPictureIds(), ","));
                    BigDecimal settlePrice = Optional.ofNullable(skuBatchCodePriceMap.get(qcDetailPo.getBatchCode())).orElse(BigDecimal.ZERO);
                    //库内抽检的结算结算金额处理
                    if (DefectHandlingType.INSIDE_DEFECT.equals(defectHandlingCreateBo.getDefectHandlingType())) {
                        defectHandlingCreateBo.setDefectBizNo(qcOrderPo.getQcOrderNo());
                        defectHandlingCreateBo.setSettlePrice(settlePrice);
                        //原料入库处理
                    } else if (WmsEnum.ReceiveType.PROCESS_MATERIAL.equals(qcReceiveOrderPo.getReceiveType())) {
                        defectHandlingCreateBo.setDefectBizNo(qcReceiveOrderPo.getScmBizNo());
                        defectHandlingCreateBo.setSettlePrice(settlePrice);
                    } else {
                        defectHandlingCreateBo.setDefectBizNo(qcReceiveOrderPo.getScmBizNo());
                        defectHandlingCreateBo.setSettlePrice(settlePrice);
                    }
                    defectHandlingCreateBo.setWarehouseCode(qcOrderPo.getWarehouseCode());
                    defectHandlingCreateBo.setPlatform(qcDetailPo.getPlatform());
                    return defectHandlingCreateBo;
                }).collect(Collectors.toList());

    }

    private static DefectHandlingType convertToDefectHandlingType(WmsEnum.ReceiveType receiveType, WmsEnum.ReturnType returnType) {
        if (WmsEnum.ReceiveType.BULK.equals(receiveType) && WmsEnum.ReturnType.QC_NOT_PASSED.equals(returnType)) {
            return DefectHandlingType.BULK_DEFECT;
        }
        if (WmsEnum.ReceiveType.PROCESS_PRODUCT.equals(receiveType) && WmsEnum.ReturnType.QC_NOT_PASSED.equals(returnType)) {
            return DefectHandlingType.PROCESS_DEFECT;
        }
        if (WmsEnum.ReceiveType.INSIDE_CHECK.equals(receiveType)) {
            return DefectHandlingType.INSIDE_DEFECT;
        }
        if (WmsEnum.ReceiveType.PROCESS_MATERIAL.equals(receiveType) && WmsEnum.ReturnType.QC_NOT_PASSED.equals(returnType)) {
            return DefectHandlingType.MATERIAL_DEFECT;
        }

        return null;
    }

    public static WmsOnShelvesMqDto convertPoToWmsOnShelvesDto(DefectCompromiseDto dto, List<DefectHandlingPo> defectHandlingPoList) {
        if (CollectionUtils.isEmpty(defectHandlingPoList)) {
            return new WmsOnShelvesMqDto();
        }
        final WmsOnShelvesMqDto wmsOnShelvesMqDto = new WmsOnShelvesMqDto();
        final DefectHandlingPo defectHandlingPo = defectHandlingPoList.get(0);
        wmsOnShelvesMqDto.setContainerCode(dto.getContainerCode());
        wmsOnShelvesMqDto.setReceiveOrderNo(defectHandlingPo.getReceiveOrderNo());
        wmsOnShelvesMqDto.setQcOrderNo(defectHandlingPo.getQcOrderNo());
        wmsOnShelvesMqDto.setOperator(GlobalContext.getUserKey());
        wmsOnShelvesMqDto.setOperatorName(GlobalContext.getUsername());

        final List<WmsOnShelvesMqDto.OnShelvesDetail> onShelvesDetailList = defectHandlingPoList.stream()
                .map(po -> {
                    final WmsOnShelvesMqDto.OnShelvesDetail onShelvesDetail = new WmsOnShelvesMqDto.OnShelvesDetail();
                    onShelvesDetail.setSkuCode(po.getSku());
                    onShelvesDetail.setBatchCode(po.getSkuBatchCode());
                    onShelvesDetail.setAmount(po.getNotPassCnt());
                    onShelvesDetail.setDefectHandlingNo(po.getDefectHandlingNo());
                    onShelvesDetail.setBizDetailId(po.getBizDetailId());
                    onShelvesDetail.setPlatform(po.getPlatform());

                    return onShelvesDetail;
                }).collect(Collectors.toList());
        wmsOnShelvesMqDto.setOnShelvesDetailList(onShelvesDetailList);

        return wmsOnShelvesMqDto;
    }

    public static List<DefectHandingExportVo> defectHandlingPoToExportVo(List<DefectHandlingPo> records,
                                                                         Map<String, String> receiveOrderNoWarehouseNameMap,
                                                                         Map<String, String> receiveDeliveryNoMap) {
        return Optional.ofNullable(records)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final DefectHandingExportVo defectHandingExportVo = new DefectHandingExportVo();
                    defectHandingExportVo.setDefectHandlingNo(po.getDefectHandlingNo());
                    defectHandingExportVo.setDefectBizNo(po.getDefectBizNo());
                    if (null != po.getDefectHandlingStatus()) {
                        defectHandingExportVo.setDefectHandlingStatusName(po.getDefectHandlingStatus().getRemark());
                    }
                    if (null != po.getDefectHandlingProgramme()) {
                        defectHandingExportVo.setDefectHandlingProgrammeName(po.getDefectHandlingProgramme().getRemark());
                    }
                    if (null != po.getDefectHandlingType()) {
                        defectHandingExportVo.setDefectHandlingTypeName(po.getDefectHandlingType().getRemark());
                    }
                    defectHandingExportVo.setRelatedOrderNo(po.getRelatedOrderNo());
                    defectHandingExportVo.setSku(po.getSku());
                    defectHandingExportVo.setSkuBatchCode(po.getSkuBatchCode());
                    defectHandingExportVo.setSupplierCode(po.getSupplierCode());
                    defectHandingExportVo.setNotPassCnt(String.valueOf(po.getNotPassCnt()));
                    defectHandingExportVo.setAdverseReason(po.getAdverseReason());
                    defectHandingExportVo.setCreateUsername(po.getDefectCreateUsername());
                    defectHandingExportVo.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(po.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    defectHandingExportVo.setConfirmUsername(po.getConfirmUsername());
                    defectHandingExportVo.setConfirmTimeStr(ScmTimeUtil.localDateTimeToStr(po.getConfirmTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    defectHandingExportVo.setRelatedWarehouseName(receiveOrderNoWarehouseNameMap.getOrDefault(po.getRelatedOrderNo(), ""));
                    if (DefectHandlingType.MATERIAL_DEFECT.equals(po.getDefectHandlingType())) {
                        defectHandingExportVo.setOriginOrderNo(po.getReceiveOrderNo());
                    }
                    if (DefectHandlingType.BULK_DEFECT.equals(po.getDefectHandlingType())) {
                        defectHandingExportVo.setOriginOrderNo(po.getDefectBizNo());
                    }
                    if (DefectHandlingType.INSIDE_DEFECT.equals(po.getDefectHandlingType())) {
                        defectHandingExportVo.setOriginOrderNo(receiveDeliveryNoMap.get(po.getReceiveOrderNo()));
                    }

                    return defectHandingExportVo;
                }).collect(Collectors.toList());
    }
}
