package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderAddDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderEditDto;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderBillPo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SettleSupplementOrderVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 补款单转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface SupplementOrderConverter {
    SupplementOrderConverter INSTANCE = Mappers.getMapper(SupplementOrderConverter.class);

    SupplementOrderDetailVo convert(SupplementOrderPo po);

    SupplementOrderPo create(SupplementOrderAddDto dto);

    SupplementOrderPo edit(SupplementOrderEditDto dto);

    List<SettleSupplementOrderVo.SupplementOrderDetail> supplementOrderBillList(List<ProcessSettleOrderBillPo> poList);


}
