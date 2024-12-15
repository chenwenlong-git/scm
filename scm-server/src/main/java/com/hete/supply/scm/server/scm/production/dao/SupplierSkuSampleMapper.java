package com.hete.supply.scm.server.scm.production.dao;

import com.hete.supply.scm.server.scm.production.entity.po.SupplierSkuSamplePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * <p>
 * 供应商商品样品表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-29
 */
@Mapper
interface SupplierSkuSampleMapper extends BaseDataMapper<SupplierSkuSamplePo> {

    Set<String> getSupCodeListBySku(String sku);
}
