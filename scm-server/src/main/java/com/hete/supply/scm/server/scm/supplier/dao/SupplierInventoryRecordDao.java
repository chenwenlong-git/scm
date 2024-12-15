package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.InventoryRecordDto;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlReason;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryRecordExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.InventoryRecordVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商库存记录表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Component
@Validated
public class SupplierInventoryRecordDao extends BaseDao<SupplierInventoryRecordMapper, SupplierInventoryRecordPo> {

    public CommonPageResult.PageInfo<InventoryRecordVo> searchInventoryRecord(Page<Void> page, InventoryRecordDto dto) {
        final IPage<InventoryRecordVo> pageResult = baseMapper.searchInventoryRecord(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Integer getExportTotals(InventoryRecordDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<SupplierInventoryRecordExportVo> getExportList(Page<SupplierInventoryRecordExportVo> page, InventoryRecordDto dto) {
        final IPage<SupplierInventoryRecordExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<SupplierInventoryRecordPo> getListByIdList(List<Long> recordIdList) {
        if (CollectionUtils.isEmpty(recordIdList)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<SupplierInventoryRecordPo>lambdaQuery()
                .in(SupplierInventoryRecordPo::getSupplierInventoryRecordId, recordIdList));
    }

    public List<SupplierInventoryRecordPo> getListByIdList(List<Long> recordIdList, SupplierInventoryCtrlType supplierInventoryCtrlType) {
        if (CollectionUtils.isEmpty(recordIdList)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<SupplierInventoryRecordPo>lambdaQuery()
                .in(SupplierInventoryRecordPo::getSupplierInventoryRecordId, recordIdList)
                .eq(SupplierInventoryRecordPo::getSupplierInventoryCtrlType, supplierInventoryCtrlType));
    }

    public List<SupplierInventoryRecordPo> getListByRelateNo(String relateNo, SupplierInventoryCtrlReason reason) {
        if (StringUtils.isBlank(relateNo)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<SupplierInventoryRecordPo>lambdaQuery()
                .eq(SupplierInventoryRecordPo::getRelateNo, relateNo)
                .eq(SupplierInventoryRecordPo::getSupplierInventoryCtrlReason, reason));
    }

    public List<SupplierInventoryRecordPo> getListByRelateNo(String relateNo, SupplierInventoryCtrlReason reason,
                                                             SupplierInventoryCtrlType supplierInventoryCtrlType) {
        if (StringUtils.isBlank(relateNo)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<SupplierInventoryRecordPo>lambdaQuery()
                .eq(SupplierInventoryRecordPo::getRelateNo, relateNo)
                .eq(SupplierInventoryRecordPo::getSupplierInventoryCtrlReason, reason)
                .eq(SupplierInventoryRecordPo::getSupplierInventoryCtrlType, supplierInventoryCtrlType)
                .orderByDesc(SupplierInventoryRecordPo::getCreateTime));
    }

    public List<SupplierInventoryRecordPo> getListByRelateNo(String relateNo,
                                                             SupplierInventoryCtrlType supplierInventoryCtrlType,
                                                             SupplierWarehouse supplierWarehouse) {
        if (StringUtils.isBlank(relateNo)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<SupplierInventoryRecordPo>lambdaQuery()
                .eq(SupplierInventoryRecordPo::getRelateNo, relateNo)
                .eq(SupplierInventoryRecordPo::getSupplierWarehouse, supplierWarehouse)
                .eq(SupplierInventoryRecordPo::getSupplierInventoryCtrlType, supplierInventoryCtrlType)
                .orderByDesc(SupplierInventoryRecordPo::getCreateTime));
    }
}
