package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleOrderExportVo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 开发需求样品单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-03
 */
@Mapper
interface DevelopSampleOrderMapper extends BaseDataMapper<DevelopSampleOrderPo> {

    IPage<DevelopSampleOrderSearchVo> search(Page<Void> page, @Param("dto") DevelopSampleOrderSearchDto dto);

    Integer getExportTotals(@Param("dto") DevelopSampleOrderSearchDto dto);

    IPage<DevelopSampleOrderExportVo> getExportList(Page<Void> page, @Param("dto") DevelopSampleOrderSearchDto dto);
}
