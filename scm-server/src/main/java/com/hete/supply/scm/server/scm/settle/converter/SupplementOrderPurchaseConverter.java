package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderPurchaseDto;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPurchasePo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
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
public interface SupplementOrderPurchaseConverter {
    SupplementOrderPurchaseConverter INSTANCE = Mappers.getMapper(SupplementOrderPurchaseConverter.class);

    SupplementOrderDetailVo.SupplementOrderPurchaseVo convert(SupplementOrderPurchasePo po);

    List<SupplementOrderDetailVo.SupplementOrderPurchaseVo> detail(List<SupplementOrderPurchasePo> poList);


    List<SupplementOrderPurchasePo> create(List<SupplementOrderPurchaseDto> dto);

    List<SupplementOrderPurchasePo> edit(List<SupplementOrderPurchaseDto> dto);
}
