package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SearchInventoryDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCodeAndSkuBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryChangeBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SearchInventoryVo;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeItemDto;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventorySubItemDto;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 供应商库存表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Component
@Validated
public class SupplierInventoryDao extends BaseDao<SupplierInventoryMapper, SupplierInventoryPo> {

    public CommonPageResult.PageInfo<SearchInventoryVo> searchInventory(Page<Void> page, SearchInventoryDto dto) {
        final IPage<SearchInventoryVo> pageResult = baseMapper.searchInventory(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 更新自备库存
     *
     * @param inventoryChangeItemList
     */
    public void selfProvideInventoryChange(List<InventoryChangeItemDto> inventoryChangeItemList) {
        if (CollectionUtils.isEmpty(inventoryChangeItemList)) {
            return;
        }
        baseMapper.selfProvideInventoryChange(inventoryChangeItemList);
    }

    /**
     * 更新备货库存
     *
     * @param inventoryChangeItemList
     */
    public void stockUpInventoryChange(List<InventoryChangeItemDto> inventoryChangeItemList) {
        if (CollectionUtils.isEmpty(inventoryChangeItemList)) {
            return;
        }
        baseMapper.stockUpInventoryChange(inventoryChangeItemList);
    }

    /**
     * 更新不良品库存
     *
     * @param inventoryChangeItemList
     */
    public void defectiveInventoryChange(List<InventoryChangeItemDto> inventoryChangeItemList) {
        if (CollectionUtils.isEmpty(inventoryChangeItemList)) {
            return;
        }
        baseMapper.defectiveInventoryChange(inventoryChangeItemList);
    }

    public List<SupplierInventoryPo> getInventoryBySkuAndSupplier(List<SupplierCodeAndSkuBo> supplierCodeAndSkuBoList) {
        if (CollectionUtils.isEmpty(supplierCodeAndSkuBoList)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SupplierInventoryPo> queryWrapper = new LambdaQueryWrapper<>();
        for (SupplierCodeAndSkuBo bo : supplierCodeAndSkuBoList) {
            queryWrapper.or().eq(SupplierInventoryPo::getSupplierCode, bo.getSupplierCode())
                    .eq(SupplierInventoryPo::getSku, bo.getSku());
        }

        return baseMapper.selectList(queryWrapper);
    }

    public SupplierInventoryPo getInventoryBySkuAndSupplier(String supplierCode, String sku) {
        if (StringUtils.isBlank(supplierCode) || StringUtils.isBlank(sku)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<SupplierInventoryPo>lambdaQuery()
                .eq(SupplierInventoryPo::getSupplierCode, supplierCode)
                .eq(SupplierInventoryPo::getSku, sku));
    }

    public List<SupplierInventoryPo> getInventoryBySkuAndSupplier(String supplierCode, List<String> skuList) {
        if (StringUtils.isBlank(supplierCode) || CollectionUtils.isEmpty(skuList)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<SupplierInventoryPo>lambdaQuery()
                .eq(SupplierInventoryPo::getSupplierCode, supplierCode)
                .in(SupplierInventoryPo::getSku, skuList));
    }


    public void returnGoodsStockUp(String supplierCode, String sku, Integer warehousingCnt) {
        baseMapper.returnGoodsStockUp(supplierCode, sku, warehousingCnt);
    }

    public Integer getOtherSupplierInventory(String sku, String supplierCode) {
        return baseMapper.getOtherSupplierInventory(sku, supplierCode);
    }

    public List<SupplierInventoryPo> getOtherInventoryBySupplierAndSku(String supplierCode, List<String> skuList) {
        return baseMapper.selectList(Wrappers.<SupplierInventoryPo>lambdaQuery()
                .eq(SupplierInventoryPo::getSupplierCode, supplierCode)
                .in(SupplierInventoryPo::getSku, skuList));
    }

    public void subInventoryBySkuAndSupplier(List<InventorySubItemDto> inventorySubItemList) {
        baseMapper.subInventoryBySkuAndSupplier(inventorySubItemList);
    }

    public Integer getExportTotals(SearchInventoryDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<SupplierInventoryExportVo> getExportList(Page<SupplierInventoryExportVo> page, SearchInventoryDto dto) {
        final IPage<SupplierInventoryExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public void inventoryChange(List<SupplierInventoryChangeBo> supplierInventoryChangeBoList) {
        baseMapper.inventoryChange(supplierInventoryChangeBoList);
    }
}
