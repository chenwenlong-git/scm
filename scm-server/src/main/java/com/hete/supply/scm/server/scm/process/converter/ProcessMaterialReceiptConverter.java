package com.hete.supply.scm.server.scm.process.converter;


import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptCreateDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptItemVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: RockyHuas
 * @date: 2022/11/4 10:01
 */
@Mapper
public interface ProcessMaterialReceiptConverter {

    ProcessMaterialReceiptConverter INSTANCE = Mappers.getMapper(ProcessMaterialReceiptConverter.class);

    ProcessMaterialReceiptPo convert(ProcessMaterialReceiptCreateDto dto);

    ProcessMaterialReceiptDetailVo convert(ProcessMaterialReceiptPo po);

    ProcessMaterialReceiptItemVo convert(ProcessMaterialReceiptItemPo po);

}
