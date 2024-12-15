package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopPricingOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopPricingOrderExportVo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPricingGroupByStatusBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPricingOrderSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 核价单表列表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Mapper
interface DevelopPricingOrderMapper extends BaseDataMapper<DevelopPricingOrderPo> {

    IPage<DevelopPricingOrderSearchVo> search(Page<Void> page, @Param("dto") DevelopPricingOrderSearchDto dto);

    Integer getExportTotals(@Param("dto") DevelopPricingOrderSearchDto dto);

    IPage<DevelopPricingOrderExportVo> getExportList(Page<Void> page, @Param("dto") DevelopPricingOrderSearchDto dto);

    List<DevelopPricingGroupByStatusBo> getListByGroupByStatus(@Param("supplierCodeList") List<String> supplierCodeList);
}
