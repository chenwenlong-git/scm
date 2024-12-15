package com.hete.supply.scm.server.scm.supplier.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 供应商联系人转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface SupplierContactConverter {
    SupplierContactConverter INSTANCE = Mappers.getMapper(SupplierContactConverter.class);

}
