package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderDescVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderPrintByWmsVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderProcedureVo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderEditDto;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderItemVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderMaterialVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderPrintVo;
import com.hete.supply.scm.server.scm.sample.entity.bo.SampleProcessAndRawBo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleProcessAndRawVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: RockyHuas
 * @date: 2022/11/9 17:01
 */
@Mapper
public interface ProcessOrderConverter {
    ProcessOrderConverter INSTANCE = Mappers.getMapper(ProcessOrderConverter.class);

    ProcessOrderPo convert(ProcessOrderCreateDto dto);

    ProcessOrderPo convert(ProcessOrderEditDto dto);

    ProcessOrderDetailVo convert(ProcessOrderPo processOrderPo);

    ProcessOrderItemVo convert(ProcessOrderItemPo processOrderItemPo);

    ProcessOrderMaterialVo convert(ProcessOrderMaterialPo processOrderMaterialPo);

    ProcessOrderDescVo convert(ProcessOrderDescPo processOrderDescPo);

    ProcessOrderProcedureVo convert(ProcessOrderProcedurePo processOrderProcedurePo);

    ProcessOrderPrintByWmsVo convert(ProcessOrderPrintVo processOrderPrintVo);

    SampleProcessAndRawVo convert(SampleProcessAndRawBo sampleProcessAndRawBo);
}
