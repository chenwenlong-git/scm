package com.hete.supply.scm.server.scm.adjust.dao;

import com.hete.supply.scm.server.scm.adjust.entity.dto.GoodsPriceGetListDto;
import com.hete.supply.scm.server.scm.adjust.entity.dto.GoodsPriceGetPurchaseListDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceGetListVo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceGetPurchaseListVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品价格表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Mapper
interface GoodsPriceMapper extends BaseDataMapper<GoodsPricePo> {
    List<GoodsPriceGetListVo> getGoodsPriceList(@Param("dto") GoodsPriceGetListDto dto);

    List<GoodsPriceGetPurchaseListVo> getGoodsPricePurchaseList(@Param("dto") GoodsPriceGetPurchaseListDto dto);
}
