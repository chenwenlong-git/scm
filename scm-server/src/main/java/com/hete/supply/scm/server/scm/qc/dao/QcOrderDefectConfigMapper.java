package com.hete.supply.scm.server.scm.qc.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.vo.QcOrderDefectConfigExportVo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderDefectConfigPo;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 质检次品配置 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Mapper
interface QcOrderDefectConfigMapper extends BaseDataMapper<QcOrderDefectConfigPo> {

    Integer getExportTotals(@Param("dto") ComPageDto dto);

    IPage<QcOrderDefectConfigExportVo> getExportList(Page<Void> page, @Param("dto") ComPageDto dto);
}
