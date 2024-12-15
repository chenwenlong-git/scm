package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 采购结算单转换器
 *
 * @author ChenWenLong
 * @date 2022/11/8 11:58
 */
@Mapper
public interface ProcessSettleOrderConverter {
    ProcessSettleOrderConverter INSTANCE = Mappers.getMapper(ProcessSettleOrderConverter.class);

    ProcessSettleOrderDetailVo convert(ProcessSettleOrderPo po);


}
