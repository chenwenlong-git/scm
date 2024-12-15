package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportVo;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductOrderExportBo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 扣款单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-05
 */
@Mapper
interface DeductOrderMapper extends BaseDataMapper<DeductOrderPo> {

    /**
     * 分页查询补款单信息
     *
     * @author ChenWenLong
     * @date 2022/11/4 10:23
     */
    IPage<DeductOrderVo> selectDeductOrderPage(Page<Void> page, @Param("dto") DeductOrderDto deductOrderDto);

    /**
     * 统计导出的数量
     *
     * @param dto
     * @return
     */
    Integer getExportTotals(@Param("dto") DeductOrderQueryByApiDto dto);

    /**
     * 统计导出的列表
     *
     * @param dto
     * @return
     */
    IPage<DeductOrderExportVo> getExportList(Page<Void> page, @Param("dto") DeductOrderQueryByApiDto dto);


    List<DeductOrderExportBo> getPurchaseBatchDeductOrderNo(@Param("deductOrderNoList") List<String> deductOrderNoList);

    List<DeductOrderExportBo> getQualityBatchDeductOrderNo(@Param("deductOrderNoList") List<String> deductOrderNoList);

    /**
     * 按sku导出
     *
     * @param dto
     * @return
     */
    Integer getExportSkuTotals(@Param("dto") DeductOrderDto dto);

    /**
     * 按sku导出列表
     *
     * @param dto
     * @return
     */
    IPage<DeductOrderPo> getExportSkuList(Page<Void> page, @Param("dto") DeductOrderDto dto);
}
