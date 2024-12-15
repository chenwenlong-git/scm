package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.api.scm.entity.vo.PurchaseChildDeliverExportVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseDeliverExportVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: weiwenxin
 * @date: 2023-08-29 16:06:50
 */
@Mapper
public interface PurchaseDeliverConverter {
    PurchaseDeliverConverter INSTANCE = Mappers.getMapper(PurchaseDeliverConverter.class);

    PurchaseChildDeliverExportVo convert(PurchaseDeliverExportVo purchaseDeliverExportVo);
}
