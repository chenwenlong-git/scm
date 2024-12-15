package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.SampleChildOrderResultSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildResultExportVo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderResultPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderResultSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 样品子单结果列表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-12-20
 */
@Mapper
interface SampleChildOrderResultMapper extends BaseDataMapper<SampleChildOrderResultPo> {
    IPage<SampleChildOrderResultSearchVo> searchSampleChildOrderResult(Page<Void> page, @Param("dto") SampleChildOrderResultSearchDto dto);

    Integer getExportTotals(@Param("dto") SampleChildOrderResultSearchDto dto);

    IPage<SampleChildResultExportVo> getExportList(Page<Void> page, @Param("dto") SampleChildOrderResultSearchDto dto);

}
