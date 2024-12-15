package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierWareSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierWarehousePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierWareSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 供应商仓库表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Mapper
interface SupplierWarehouseMapper extends BaseDataMapper<SupplierWarehousePo> {

    IPage<SupplierWareSearchVo> searchSupplierWarehouse(Page<SupplierWarehousePo> page,
                                                        @Param("dto") SupplierWareSearchDto dto);

    IPage<SupplierWareSearchVo> searchSupplierWarehousePage(Page<SupplierWarehousePo> page,
                                                            @Param("dto") SupplierWareSearchDto dto);
}
