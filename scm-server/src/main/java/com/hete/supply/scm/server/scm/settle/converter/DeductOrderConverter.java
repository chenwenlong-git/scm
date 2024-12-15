package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderAddDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderEditDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderBillPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SettleDeductOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 扣款单转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface DeductOrderConverter {
    DeductOrderConverter INSTANCE = Mappers.getMapper(DeductOrderConverter.class);

    DeductOrderDetailVo convert(DeductOrderPo po);

    List<DeductOrderDetailVo> poToListDetail(List<DeductOrderPo> poList);

    DeductOrderPo create(DeductOrderAddDto dto);

    DeductOrderPo edit(DeductOrderEditDto dto);

    List<SettleDeductOrderVo.DeductOrderDetail> deductOrderBillList(List<ProcessSettleOrderBillPo> poList);

}
