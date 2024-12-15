package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SearchInventoryDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryChangeBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SearchInventoryVo;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeItemDto;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventorySubItemDto;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 供应商库存表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Mapper
interface SupplierInventoryMapper extends BaseDataMapper<SupplierInventoryPo> {

    IPage<SearchInventoryVo> searchInventory(Page<Void> page, @Param("dto") SearchInventoryDto dto);

    void selfProvideInventoryChange(@Param("list") List<InventoryChangeItemDto> inventoryChangeItemList);

    void stockUpInventoryChange(@Param("list") List<InventoryChangeItemDto> inventoryChangeItemList);

    void defectiveInventoryChange(@Param("list") List<InventoryChangeItemDto> inventoryChangeItemList);

    void returnGoodsStockUp(@Param("supplierCode") String supplierCode,
                            @Param("sku") String sku,
                            @Param("warehousingCnt") Integer warehousingCnt);

    Integer getOtherSupplierInventory(@Param("sku") String sku, @Param("supplierCode") String supplierCode);

    int subInventoryBySkuAndSupplier(@Param("list") List<InventorySubItemDto> inventorySubItemList);

    Integer getExportTotals(@Param("dto") SearchInventoryDto dto);

    IPage<SupplierInventoryExportVo> getExportList(Page<SupplierInventoryExportVo> page, @Param("dto") SearchInventoryDto dto);

    void inventoryChange(@Param("list") List<SupplierInventoryChangeBo> supplierInventoryChangeBoList);
}
