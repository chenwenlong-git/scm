package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderRawPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPamphletOrderRawListVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPamphletRawDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 开发版单原料表转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
@Mapper
public interface DevelopPamphletOrderRawConverter {
    DevelopPamphletOrderRawConverter INSTANCE = Mappers.getMapper(DevelopPamphletOrderRawConverter.class);

    List<DevelopPamphletRawDetailVo> convert(List<DevelopPamphletOrderRawPo> pawPoList);

    List<DevelopPamphletOrderRawListVo> convertVoList(List<DevelopPamphletOrderRawPo> dtoList);

}
