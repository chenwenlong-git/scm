package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 采购结算单转换器
 *
 * @author ChenWenLong
 * @date 2022/11/8 11:58
 */
@Mapper
public interface ProcessSettleOrderItemConverter {
    ProcessSettleOrderItemConverter INSTANCE = Mappers.getMapper(ProcessSettleOrderItemConverter.class);

    List<ProcessSettleOrderDetailVo.ProcessSettleOrderItem> processSettleOrderItemList(List<ProcessSettleOrderItemPo> poList);


}
