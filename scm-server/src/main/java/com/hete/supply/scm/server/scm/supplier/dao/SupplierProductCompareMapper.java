package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 供应商产品对照表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Mapper
interface SupplierProductCompareMapper extends BaseDataMapper<SupplierProductComparePo> {

    IPage<SupplierProductComparePo> getInitData(Page<SupplierProductComparePo> page);
}
