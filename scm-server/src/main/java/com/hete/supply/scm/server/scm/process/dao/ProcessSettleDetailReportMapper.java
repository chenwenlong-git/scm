package com.hete.supply.scm.server.scm.process.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleDetailExportVo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessSettleDetailReportPo;
import com.hete.supply.scm.api.scm.entity.dto.GetProcessSettleOrderDetailAndScanSettleDto;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderDetailAndScanSettleVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 加工结算详情报表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-18
 */
@Mapper
interface ProcessSettleDetailReportMapper extends BaseDataMapper<ProcessSettleDetailReportPo> {

}
