package com.hete.supply.scm.server.scm.production.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.production.entity.dto.GetAttributePageDto;
import com.hete.supply.scm.server.scm.production.entity.po.AttributePo;
import com.hete.supply.scm.server.scm.production.entity.vo.AttributePageVo;
import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import com.hete.supply.scm.server.scm.production.enums.AttributeScope;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 供应链属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-14
 */
@Mapper
interface AttributeMapper extends BaseDataMapper<AttributePo> {

    List<Long> getIdsByAttrIdsAndScope(List<Long> attrIds, AttributeScope scope, AttributeStatus status);

    IPage<AttributePageVo> getByPage(Page<Void> page, @Param("dto") GetAttributePageDto dto);

    List<Long> listIdsByIdsAndInputType(List<Long> attrIds,
                                        List<AttributeInputType> attributeInputTypeList,
                                        AttributeStatus attrStatus);
}
