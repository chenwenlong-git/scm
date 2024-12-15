package com.hete.supply.scm.server.scm.supplier.builder;

import com.hete.supply.scm.api.scm.entity.dto.PurchasePreOrderDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchasePreOrderVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/8/19.
 */
public class SupplierBuilder {
    public static PurchasePreOrderVo buildPurchasePreOrderVo(PurchasePreOrderDto dto) {
        PurchasePreOrderVo resultVo = new PurchasePreOrderVo();
        List<PurchasePreOrderVo.PurchasePreOrderInfoVo> voList = Optional.ofNullable(dto.getPreOrderInfoDtoList())
                .orElse(new ArrayList<>())
                .stream()
                .map(preOrderInfoDto -> {
                    PurchasePreOrderVo.PurchasePreOrderInfoVo vo = new PurchasePreOrderVo.PurchasePreOrderInfoVo();
                    vo.setBusinessId(preOrderInfoDto.getBusinessId());
                    vo.setSku(preOrderInfoDto.getSku());
                    vo.setSupplierCode(preOrderInfoDto.getSupplierCode());
                    vo.setPreShelfQty(preOrderInfoDto.getPlaceOrderCnt());
                    return vo;
                }).collect(Collectors.toList());
        resultVo.setPreOrderInfoVoList(voList);

        return resultVo;
    }
}
