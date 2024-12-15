package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SupCapacityPageDto;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupCapRuleBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityRulePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierCapacityPageVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 供应商产能规则表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Mapper
interface SupplierCapacityRuleMapper extends BaseDataMapper<SupplierCapacityRulePo> {

    IPage<SupplierCapacityPageVo> page(Page<Void> page, @Param("dto") SupCapacityPageDto dto);

    int getSupCapacityRuleExportTotal(@Param("dto") SupCapacityPageDto dto);

    List<SupCapRuleBo> listBySupplierCodes(Collection<String> supplierCodeList);
}
