package com.hete.supply.scm.server.scm.process.dao;

import com.hete.supply.scm.server.scm.process.entity.bo.ProcessProcedureComplexCoefficientBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderProcedurePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 加工单工序 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Mapper
interface ProcessOrderProcedureMapper extends BaseDataMapper<ProcessOrderProcedurePo> {

    List<ProcessProcedureComplexCoefficientBo> getProcessProcedureCapacityNumBo(List<String> processOrderNos);
}
