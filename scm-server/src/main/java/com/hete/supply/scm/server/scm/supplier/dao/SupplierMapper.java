package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SupplierSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierLogisticsBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierQuickSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierQuickSearchVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 供应商信息表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Mapper
interface SupplierMapper extends BaseDataMapper<SupplierPo> {

    /**
     * 分页查询信息
     *
     * @author ChenWenLong
     * @date 2022/11/26 10:23
     */
    IPage<SupplierVo> selectSupplierPage(Page<Void> page, @Param("dto") SupplierDto dto);

    /**
     * 下拉查询信息
     *
     * @author ChenWenLong
     * @date 2022/11/26 10:23
     */
    IPage<SupplierQuickSearchVo> getSupplierQuickSearch(Page<Void> page, @Param("dto") SupplierQuickSearchDto dto);

    /**
     * 统计导出的数量
     *
     * @param dto
     * @return
     */
    Integer getExportTotals(@Param("dto") SupplierSearchDto dto);

    /**
     * 导出的列表
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<SupplierExportVo> getExportList(Page<Void> page, @Param("dto") SupplierSearchDto dto);

    List<SupplierLogisticsBo> listByLogisticsBo(Collection<String> supplierCodeList);
}
