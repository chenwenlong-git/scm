package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.api.scm.entity.enums.DevelopPamphletOrderStatus;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderRawPo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 开发版单表转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
public class DevelopPamphletOrderConverter {

    public static DevelopPamphletOrderPo convertToPo(DevelopPamphletOrderPo developPamphletOrderPo,
                                                     String supplierCode,
                                                     String supplierName,
                                                     String developPamphletOrderNo) {
        if (developPamphletOrderPo == null) {
            return new DevelopPamphletOrderPo();
        }
        DevelopPamphletOrderPo po = new DevelopPamphletOrderPo();
        po.setDevelopPamphletOrderNo(developPamphletOrderNo);
        po.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
        po.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
        po.setDevelopPamphletOrderStatus(DevelopPamphletOrderStatus.TO_BE_CONFIRMED_PAMPHLET);
        po.setSupplierCode(supplierCode);
        po.setSupplierName(supplierName);
        po.setDevelopSampleNum(developPamphletOrderPo.getDevelopSampleNum());
        po.setProposedPrice(developPamphletOrderPo.getProposedPrice());
        po.setExpectedOnShelvesDate(developPamphletOrderPo.getExpectedOnShelvesDate());
        po.setDemandDesc(developPamphletOrderPo.getDemandDesc());
        po.setRefuseReason(developPamphletOrderPo.getRefuseReason());
        return po;
    }

    public static List<DevelopPamphletOrderRawPo> convertRawToPo(DevelopPamphletOrderPo developPamphletOrderPo,
                                                                 List<DevelopPamphletOrderRawPo> rawPoList) {
        return Optional.ofNullable(rawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final DevelopPamphletOrderRawPo developPamphletOrderRawPo = new DevelopPamphletOrderRawPo();
                    developPamphletOrderRawPo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                    developPamphletOrderRawPo.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
                    developPamphletOrderRawPo.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
                    developPamphletOrderRawPo.setMaterialType(po.getMaterialType());
                    developPamphletOrderRawPo.setSku(po.getSku());
                    developPamphletOrderRawPo.setSkuBatchCode(po.getSkuBatchCode());
                    developPamphletOrderRawPo.setSkuCnt(po.getSkuCnt());
                    developPamphletOrderRawPo.setWarehouseCode(po.getWarehouseCode());
                    developPamphletOrderRawPo.setWarehouseName(po.getWarehouseName());
                    developPamphletOrderRawPo.setSampleRawBizType(po.getSampleRawBizType());
                    return developPamphletOrderRawPo;
                }).collect(Collectors.toList());

    }


}
