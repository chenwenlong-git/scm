package com.hete.supply.scm.server.scm.dao;

import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuCapBo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * sku关联的业务信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-28
 */
@Mapper
interface SkuInfoMapper extends BaseDataMapper<SkuInfoPo> {

    List<SkuCapBo> listByCapBo(Collection<String> skuList);
}
