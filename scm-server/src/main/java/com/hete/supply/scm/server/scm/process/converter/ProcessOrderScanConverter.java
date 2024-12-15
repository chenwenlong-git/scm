package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanBackVo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderScanPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SettleProcessOrderScanVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 采购结算单明细转换器
 *
 * @author ChenWenLong
 * @date 2022/11/8 11:58
 */
@Mapper
public interface ProcessOrderScanConverter {
    ProcessOrderScanConverter INSTANCE = Mappers.getMapper(ProcessOrderScanConverter.class);

    ProcessOrderScanBackVo convert(ProcessOrderScanPo processOrderScanPo);

    List<SettleProcessOrderScanVo.SettleProcessOrderScanDetail> settleProcessOrderScanList(List<ProcessSettleOrderScanPo> poList);
}
