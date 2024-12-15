package com.hete.supply.scm.server.scm.supplier.converter;

import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierSubjectCreateDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierSubjectEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierSubjectPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierSubjectVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/5/16 11:25
 */
public class SupplierSubjectConverter {

    public static List<SupplierSubjectPo> createDtoToPo(List<SupplierSubjectCreateDto> supplierSubjectDtoList) {
        return Optional.ofNullable(supplierSubjectDtoList)
                .orElse(new ArrayList<>())
                .stream().map(dto -> {
                    final SupplierSubjectPo supplierSubjectPo = new SupplierSubjectPo();
                    supplierSubjectPo.setSupplierCode(dto.getSupplierCode());
                    supplierSubjectPo.setSupplierSubjectType(dto.getSupplierSubjectType());
                    supplierSubjectPo.setSubject(dto.getSubject());
                    supplierSubjectPo.setLegalPerson(dto.getLegalPerson());
                    supplierSubjectPo.setContactsName(dto.getContactsName());
                    supplierSubjectPo.setContactsPhone(dto.getContactsPhone());
                    supplierSubjectPo.setRegisterMoney(dto.getRegisterMoney());
                    supplierSubjectPo.setBusinessScope(dto.getBusinessScope());
                    supplierSubjectPo.setBusinessAddress(dto.getBusinessAddress());
                    supplierSubjectPo.setCreditCode(dto.getCreditCode());
                    supplierSubjectPo.setSupplierExport(dto.getSupplierExport());
                    supplierSubjectPo.setSupplierInvoicing(dto.getSupplierInvoicing());
                    supplierSubjectPo.setTaxPoint(dto.getTaxPoint());
                    return supplierSubjectPo;
                }).collect(Collectors.toList());
    }

    public static List<SupplierSubjectVo> poListToVoList(List<SupplierSubjectPo> supplierSubjectPoList,
                                                         Map<Long, List<String>> businessFileCodeMap) {
        return Optional.ofNullable(supplierSubjectPoList)
                .orElse(new ArrayList<>())
                .stream().map(supplierSubjectPo -> {
                    final SupplierSubjectVo supplierSubjectVo = new SupplierSubjectVo();
                    supplierSubjectVo.setSupplierSubjectId(supplierSubjectPo.getSupplierSubjectId());
                    supplierSubjectVo.setVersion(supplierSubjectPo.getVersion());
                    supplierSubjectVo.setSupplierCode(supplierSubjectPo.getSupplierCode());
                    supplierSubjectVo.setSupplierSubjectType(supplierSubjectPo.getSupplierSubjectType());
                    supplierSubjectVo.setSubject(supplierSubjectPo.getSubject());
                    supplierSubjectVo.setLegalPerson(supplierSubjectPo.getLegalPerson());
                    supplierSubjectVo.setContactsName(supplierSubjectPo.getContactsName());
                    supplierSubjectVo.setContactsPhone(supplierSubjectPo.getContactsPhone());
                    supplierSubjectVo.setRegisterMoney(supplierSubjectPo.getRegisterMoney());
                    supplierSubjectVo.setBusinessScope(supplierSubjectPo.getBusinessScope());
                    supplierSubjectVo.setBusinessAddress(supplierSubjectPo.getBusinessAddress());
                    supplierSubjectVo.setCreditCode(supplierSubjectPo.getCreditCode());
                    supplierSubjectVo.setSupplierExport(supplierSubjectPo.getSupplierExport());
                    supplierSubjectVo.setSupplierInvoicing(supplierSubjectPo.getSupplierInvoicing());
                    supplierSubjectVo.setTaxPoint(supplierSubjectPo.getTaxPoint());

                    if (businessFileCodeMap.containsKey(supplierSubjectPo.getSupplierSubjectId())) {
                        supplierSubjectVo.setBusinessFileCodeList(businessFileCodeMap.get(supplierSubjectPo.getSupplierSubjectId()));
                    }

                    return supplierSubjectVo;
                }).collect(Collectors.toList());

    }

    public static List<SupplierSubjectPo> editDtoToPo(List<SupplierSubjectEditDto> supplierSubjectDtoList) {
        return Optional.ofNullable(supplierSubjectDtoList)
                .orElse(new ArrayList<>())
                .stream().map(dto -> {
                    final SupplierSubjectPo supplierSubjectPo = new SupplierSubjectPo();
                    supplierSubjectPo.setSupplierSubjectId(dto.getSupplierSubjectId());
                    supplierSubjectPo.setVersion(dto.getVersion());
                    supplierSubjectPo.setSupplierCode(dto.getSupplierCode());
                    supplierSubjectPo.setSupplierSubjectType(dto.getSupplierSubjectType());
                    supplierSubjectPo.setSubject(dto.getSubject());
                    supplierSubjectPo.setLegalPerson(dto.getLegalPerson());
                    supplierSubjectPo.setContactsName(dto.getContactsName());
                    supplierSubjectPo.setContactsPhone(dto.getContactsPhone());
                    supplierSubjectPo.setRegisterMoney(dto.getRegisterMoney());
                    supplierSubjectPo.setBusinessScope(dto.getBusinessScope());
                    supplierSubjectPo.setBusinessAddress(dto.getBusinessAddress());
                    supplierSubjectPo.setCreditCode(dto.getCreditCode());
                    supplierSubjectPo.setSupplierExport(dto.getSupplierExport());
                    supplierSubjectPo.setSupplierInvoicing(dto.getSupplierInvoicing());
                    supplierSubjectPo.setTaxPoint(dto.getTaxPoint());
                    return supplierSubjectPo;
                }).collect(Collectors.toList());
    }
}
