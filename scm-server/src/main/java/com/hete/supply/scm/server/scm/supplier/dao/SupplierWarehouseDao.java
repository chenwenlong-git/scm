package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierWareSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierWarehousePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierWareSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 供应商仓库表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Component
@Validated
public class SupplierWarehouseDao extends BaseDao<SupplierWarehouseMapper, SupplierWarehousePo> {
    public CommonPageResult.PageInfo<SupplierWareSearchVo> searchSupplierWarehousePage(Page<SupplierWarehousePo> page,
                                                                                       SupplierWareSearchDto dto) {
        final IPage<SupplierWareSearchVo> pageResult = baseMapper.searchSupplierWarehousePage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<SupplierWarehousePo> getListBySupplierCode(String supplierCode) {
        if (StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SupplierWarehousePo>lambdaQuery()
                .eq(SupplierWarehousePo::getSupplierCode, supplierCode));
    }

    public List<SupplierWarehousePo> getListByWarehouseCode(String warehouseCode) {
        if (StringUtils.isBlank(warehouseCode)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SupplierWarehousePo>lambdaQuery()
                .eq(SupplierWarehousePo::getWarehouseCode, warehouseCode));
    }

    public SupplierWarehousePo getOneBySupplierCodeAndSupplierWarehouse(String supplierCode,
                                                                        SupplierWarehouse supplierWarehouse) {
        if (StringUtils.isBlank(supplierCode)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<SupplierWarehousePo>lambdaQuery()
                .eq(SupplierWarehousePo::getSupplierCode, supplierCode)
                .eq(SupplierWarehousePo::getSupplierWarehouse, supplierWarehouse));
    }

    public List<SupplierWarehousePo> getListBySupplierCodeListAndType(List<String> supplierCodeList, SupplierWarehouse supplierWarehouse) {
        if (CollectionUtils.isEmpty(supplierCodeList) || null == supplierWarehouse) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SupplierWarehousePo>lambdaQuery()
                .in(SupplierWarehousePo::getSupplierCode, supplierCodeList)
                .eq(SupplierWarehousePo::getSupplierWarehouse, supplierWarehouse));
    }

    public List<SupplierWarehousePo> getListBySupplierCodeList(List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplierWarehousePo>lambdaQuery()
                .in(SupplierWarehousePo::getSupplierCode, supplierCodeList));
    }
}
