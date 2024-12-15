package com.hete.supply.scm.server.scm.supplier.converter;

import com.hete.supply.scm.api.scm.entity.vo.GetBySupplierCodeVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierImportationDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierAddDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 供应商转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface SupplierConverter {
    SupplierConverter INSTANCE = Mappers.getMapper(SupplierConverter.class);

    SupplierDetailVo poToDetail(SupplierPo po);

    SupplierPo create(SupplierAddDto dto);

    SupplierPo edit(SupplierEditDto dto);

    List<GetBySupplierCodeVo> poListToSupplierCodeVo(List<SupplierPo> poList);

    SupplierPo convert(SupplierImportationDto.ImportationDetail dto);

}
