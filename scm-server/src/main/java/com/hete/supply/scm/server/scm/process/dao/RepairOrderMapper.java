package com.hete.supply.scm.server.scm.process.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.RepairOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderResultExportVo;
import com.hete.supply.scm.server.scm.process.entity.dto.RepairOrderNoPageDto;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderPo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderPrintBatchCodeVo;
import com.hete.supply.scm.server.scm.process.entity.vo.RepairOrderSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 返修单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-29
 */
@Mapper
interface RepairOrderMapper extends BaseDataMapper<RepairOrderPo> {

    IPage<RepairOrderSearchVo> searchRepairOrder(Page<Void> page, @Param("dto") RepairOrderSearchDto dto);

    Integer getExportRepairOrderTotals(@Param("dto") RepairOrderSearchDto dto);

    Integer getExportRepairOrderResultTotals(@Param("dto") RepairOrderSearchDto dto);

    IPage<RepairOrderResultExportVo> getExportRepairOrderResultList(Page<Void> page, @Param("dto") RepairOrderSearchDto dto);

    IPage<RepairOrderPrintBatchCodeVo> getRepairOrderPrintBatchCode(Page<Void> page, @Param("dto") RepairOrderNoPageDto dto);
}
