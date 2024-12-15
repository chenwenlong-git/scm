package com.hete.supply.scm.server.scm.stockup.dao;

import com.hete.supply.scm.server.scm.stockup.entity.bo.StockUpCntBo;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderItemPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 备货单项目表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Mapper
interface StockUpOrderItemMapper extends BaseDataMapper<StockUpOrderItemPo> {

    List<StockUpCntBo> getSumCntByNoList(@Param("noList") List<String> stockUpOrderNoList);
}
