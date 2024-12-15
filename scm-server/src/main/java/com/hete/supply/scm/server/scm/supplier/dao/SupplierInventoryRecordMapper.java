package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.InventoryRecordDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryRecordExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.InventoryRecordVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 供应商库存记录表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Mapper
interface SupplierInventoryRecordMapper extends BaseDataMapper<SupplierInventoryRecordPo> {

    IPage<InventoryRecordVo> searchInventoryRecord(Page<Void> page, @Param("dto") InventoryRecordDto dto);

    Integer getExportTotals(@Param("dto") InventoryRecordDto dto);

    IPage<SupplierInventoryRecordExportVo> getExportList(Page<SupplierInventoryRecordExportVo> page, @Param("dto") InventoryRecordDto dto);
}
