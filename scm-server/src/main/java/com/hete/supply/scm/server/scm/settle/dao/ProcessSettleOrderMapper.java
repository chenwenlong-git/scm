package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ProcessSettleOrderDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleOrderExportVo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 加工结算单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-10
 */
@Mapper
interface ProcessSettleOrderMapper extends BaseDataMapper<ProcessSettleOrderPo> {

    /**
     * 分页查询信息
     *
     * @author ChenWenLong
     * @date 2022/11/10 10:23
     */
    IPage<ProcessSettleOrderVo> selectProcessSettleOrderPage(Page<Void> page, @Param("dto") ProcessSettleOrderDto processSettleOrderDto);


    /**
     * 统计导出的数量
     *
     * @param dto
     * @return
     */
    Integer getExportTotals(@Param("dto") ProcessSettleOrderDto dto);

    /**
     * 导出的列表
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ProcessSettleOrderExportVo> getExportList(Page<Void> page, @Param("dto") ProcessSettleOrderDto dto);

}
