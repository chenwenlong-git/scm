package com.hete.supply.scm.server.scm.production.dao;

import com.hete.supply.scm.server.scm.production.entity.po.SkuRiskPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 商品风险表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-29
 */
@Mapper
interface SkuRiskMapper extends BaseDataMapper<SkuRiskPo> {

    List<Long> selectAllIds();
}
