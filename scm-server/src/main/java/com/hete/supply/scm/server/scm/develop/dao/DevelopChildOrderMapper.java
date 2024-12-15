package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopChildSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopChildOrderExportVo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopChildGroupByStatusBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopChildSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 开发子单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-31
 */
@Mapper
interface DevelopChildOrderMapper extends BaseDataMapper<DevelopChildOrderPo> {

    IPage<DevelopChildSearchVo> searchDevelopChild(Page<Void> page, @Param("dto") DevelopChildSearchDto dto);

    Integer getExportTotals(@Param("dto") DevelopChildSearchDto dto);

    IPage<DevelopChildOrderExportVo> getExportList(Page<Void> page, @Param("dto") DevelopChildSearchDto dto);

    List<DevelopChildGroupByStatusBo> getListByGroupByStatus(@Param("supplierCodeList") List<String> supplierCodeList);
}
