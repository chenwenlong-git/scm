package com.hete.supply.scm.server.scm.production.dao;

import com.hete.supply.scm.server.scm.production.entity.po.AttributeRiskPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 供应链属性风险表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-26
 */
@Mapper
interface AttributeRiskMapper extends BaseDataMapper<AttributeRiskPo> {

    List<AttributeRiskPo> listAll(int maxLimit);
}
