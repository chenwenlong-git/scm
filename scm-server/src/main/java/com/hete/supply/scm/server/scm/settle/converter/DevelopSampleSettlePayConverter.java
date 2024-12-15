package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettlePayAddDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettlePayPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/4 14:41
 */
@Mapper
public interface DevelopSampleSettlePayConverter {
    DevelopSampleSettlePayConverter INSTANCE = Mappers.getMapper(DevelopSampleSettlePayConverter.class);

    List<DevelopSampleSettleOrderDetailVo.DevelopSampleSettleOrderPayVo> convert(List<DevelopSampleSettlePayPo> poList);

    DevelopSampleSettlePayPo addPay(DevelopSampleSettlePayAddDto po);
}
