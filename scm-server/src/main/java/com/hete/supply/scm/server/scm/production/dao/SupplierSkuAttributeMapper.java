package com.hete.supply.scm.server.scm.production.dao;

import com.hete.supply.scm.server.scm.production.entity.po.SupplierSkuAttributePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * <p>
 * 供应商商品属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Mapper
interface SupplierSkuAttributeMapper extends BaseDataMapper<SupplierSkuAttributePo> {

    Set<String> getSupCodeListBySku(String sku);
}
