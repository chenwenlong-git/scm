package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SupCapacityPageDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupCapDateRangeQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityResBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 供应商产能表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Mapper
interface SupplierCapacityMapper extends BaseDataMapper<SupplierCapacityPo> {

    void updateSupNorAvailCapacity(String supplierCode, LocalDate operationDate, BigDecimal operateValue);

    List<String> filterSupplierCodes(BigDecimal restCap30PerStart, BigDecimal restCap30PerEnd,
                                     BigDecimal restCap60PerStart, BigDecimal restCap60PerEnd,
                                     BigDecimal restCap90PerStart, BigDecimal restCap90PerEnd);

    int getSupCapacityExportTotal(@Param("dto") SupCapacityPageDto dto);

    IPage<SupplierCapacityExportVo> getSupCapacityExportList(Page<Void> page, @Param("dto") SupCapacityPageDto dto);

    List<SupplierCapacityResBo> listBySupplierCodeAndDate(List<SupplierCapacityQueryBo> queryParams);

    List<SupplierCapacityBo> listBySupCapWithDateRange(String supplierCode, LocalDate capacityBeginDate, LocalDate capacityEndDate);
}
