package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewSampleOrderInfoPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopReviewSampleInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 开发审版关联样品单属性表转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
@Mapper
public interface DevelopReviewSampleOrderInfoConverter {
    DevelopReviewSampleOrderInfoConverter INSTANCE = Mappers.getMapper(DevelopReviewSampleOrderInfoConverter.class);

    List<DevelopReviewSampleInfoVo> convert(List<DevelopReviewSampleOrderInfoPo> poList);

}
