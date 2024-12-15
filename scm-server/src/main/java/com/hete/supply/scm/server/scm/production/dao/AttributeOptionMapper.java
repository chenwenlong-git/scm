package com.hete.supply.scm.server.scm.production.dao;

import com.hete.supply.scm.server.scm.production.entity.po.AttributeOptionPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 供应链属性可选值表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-14
 */
@Mapper
interface AttributeOptionMapper extends BaseDataMapper<AttributeOptionPo> {

    List<Long> getIdsByAttrVal(String attributeValue);
}
