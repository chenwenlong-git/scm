package com.hete.supply.scm.server.scm.dao;

import com.hete.supply.scm.server.scm.entity.bo.SkuAvgUpdateBo;
import com.hete.supply.scm.server.scm.entity.po.SkuAvgPricePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * sku均价表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-31
 */
@Mapper
interface SkuAvgPriceMapper extends BaseDataMapper<SkuAvgPricePo> {

    void updateSkuAvgPrice(@Param("list") List<SkuAvgUpdateBo> skuAvgUpdateBoList);
}
