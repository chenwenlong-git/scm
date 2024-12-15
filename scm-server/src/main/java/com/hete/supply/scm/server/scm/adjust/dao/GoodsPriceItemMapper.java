package com.hete.supply.scm.server.scm.adjust.dao;

import com.hete.supply.scm.server.scm.adjust.entity.dto.GoodsPriceItemSearchListDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPriceItemPo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceItemSearchListVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品价格操作明细记录表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Mapper
interface GoodsPriceItemMapper extends BaseDataMapper<GoodsPriceItemPo> {

    List<GoodsPriceItemSearchListVo> getGoodsPriceItemListByDto(@Param("dto") GoodsPriceItemSearchListDto dto);
}
