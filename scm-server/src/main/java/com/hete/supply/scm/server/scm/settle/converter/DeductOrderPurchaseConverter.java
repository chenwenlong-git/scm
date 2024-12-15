package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderPurchaseDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPurchasePo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 补款单明细采购转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface DeductOrderPurchaseConverter {
    DeductOrderPurchaseConverter INSTANCE = Mappers.getMapper(DeductOrderPurchaseConverter.class);

    DeductOrderDetailVo.DeductOrderPurchaseVo convert(DeductOrderPurchasePo po);

    List<DeductOrderDetailVo.DeductOrderPurchaseVo> deductOrderPurchaseList(List<DeductOrderPurchasePo> poList);

    List<DeductOrderPurchasePo> create(List<DeductOrderPurchaseDto> dto);

    List<DeductOrderPurchasePo> edit(List<DeductOrderPurchaseDto> dto);
}
