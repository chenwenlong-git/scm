package com.hete.supply.scm.server.scm.stockup.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.StockUpSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.StockUpExportVo;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderPo;
import com.hete.supply.scm.server.scm.stockup.entity.vo.StockUpSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 备货单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Mapper
interface StockUpOrderMapper extends BaseDataMapper<StockUpOrderPo> {

    IPage<StockUpSearchVo> searchStockUp(Page<StockUpSearchVo> page, @Param("dto") StockUpSearchDto dto);

    IPage<StockUpExportVo> getExportList(Page<StockUpExportVo> page, @Param("dto") StockUpSearchDto dto);

    Integer getExportTotals(@Param("dto") StockUpSearchDto dto);
}
