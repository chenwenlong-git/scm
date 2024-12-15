package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessMaterialReceiptExportVo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 加工原料收货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Mapper
interface ProcessMaterialReceiptMapper extends BaseDataMapper<ProcessMaterialReceiptPo> {

    /**
     * 分页查询
     *
     * @param page
     * @param processMaterialReceiptQueryDto
     * @return
     */
    IPage<ProcessMaterialReceiptVo> getByPage(Page<Void> page, @Param("params") ProcessMaterialReceiptQueryDto processMaterialReceiptQueryDto);

    /**
     * 统计导出的数量
     *
     * @param dto
     * @return
     */
    Integer getExportTotals(@Param("params") ProcessMaterialReceiptQueryByApiDto dto);

    /**
     * 统计导出的列表
     *
     * @param dto
     * @return
     */
    IPage<ProcessMaterialReceiptExportVo> getExportList(Page<Void> page, @Param("params") ProcessMaterialReceiptQueryByApiDto dto);
}
