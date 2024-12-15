package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessExportVo;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessPo;
import com.hete.supply.scm.server.scm.entity.vo.GoodsProcessVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品工序管理表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Mapper
interface GoodsProcessMapper extends BaseDataMapper<GoodsProcessPo> {

    IPage<GoodsProcessVo> getByPage(Page<Void> page, @Param("params") GoodsProcessQueryDto goodsProcessQueryDto);

    /**
     * 统计导出的数量
     *
     * @param dto
     * @return
     */
    Integer getExportTotals(@Param("params") GoodsProcessQueryDto dto);

    /**
     * 统计导出的列表
     *
     * @param dto
     * @return
     */
    IPage<GoodsProcessExportVo> getExportList(Page<Void> page, @Param("params") GoodsProcessQueryByApiDto dto);
}
