package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleSettleOrderExportVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleSearchVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleOrderPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 样品结算结算单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Mapper
interface DevelopSampleSettleOrderMapper extends BaseDataMapper<DevelopSampleSettleOrderPo> {

    IPage<DevelopSampleSettleSearchVo> search(Page<Void> page, @Param("dto") DevelopSampleSettleSearchDto dto);

    Integer getExportTotals(@Param("dto") DevelopSampleSettleSearchDto dto);

    IPage<DevelopSampleSettleOrderExportVo> getExportList(Page<Void> page, @Param("dto") DevelopSampleSettleSearchDto dto);
}
