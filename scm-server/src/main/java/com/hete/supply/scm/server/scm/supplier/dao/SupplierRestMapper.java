package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ExportSupplierRestDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierRestExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierRestPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 供应商停工时间表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Mapper
interface SupplierRestMapper extends BaseDataMapper<SupplierRestPo> {

    int getExportTotals(@Param("dto") ExportSupplierRestDto dto);

    IPage<SupplierRestExportVo> getByPage(Page<SupplierRestExportVo> page, @Param("dto") ExportSupplierRestDto dto);
}
