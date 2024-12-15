package com.hete.supply.scm.server.supplier.sample.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleDeliverSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleDeliverExportVo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 样品发货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Mapper
interface SampleDeliverOrderMapper extends BaseDataMapper<SampleDeliverOrderPo> {


    IPage<SampleDeliverVo> searchDeliver(Page<Void> page,
                                         @Param("dto") SampleDeliverSearchDto dto,
                                         @Param("deliverNoList") List<String> deliverNoList);

    Integer getExportTotals(@Param("dto") SampleDeliverSearchDto dto, @Param("deliverNoList") List<String> deliverNoList);

    IPage<SampleDeliverExportVo> getExportList(Page<Void> page, @Param("dto") SampleDeliverSearchDto dto, @Param("deliverNoList") List<String> deliverNoList);
}
