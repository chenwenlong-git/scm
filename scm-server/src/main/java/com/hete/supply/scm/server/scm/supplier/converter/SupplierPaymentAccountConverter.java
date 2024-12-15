package com.hete.supply.scm.server.scm.supplier.converter;

import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierPaymentAccountCreateDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierPaymentAccountEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierPaymentAccountVo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountStatus;
import com.hete.support.api.enums.BooleanType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/12/6 17:16
 */
public class SupplierPaymentAccountConverter {

    public static List<SupplierPaymentAccountPo> createDtoToPo(List<SupplierPaymentAccountCreateDto> dtoList,
                                                               Map<String, SupplierPo> supplierPoMap) {
        return Optional.ofNullable(dtoList)
                .orElse(new ArrayList<>())
                .stream().map(dto -> {
                    final SupplierPaymentAccountPo po = new SupplierPaymentAccountPo();
                    po.setAccount(dto.getAccount());
                    po.setSupplierCode(dto.getSupplierCode());
                    SupplierPo supplierPo = supplierPoMap.get(dto.getSupplierCode());
                    if (supplierPo != null) {
                        po.setSupplierName(supplierPo.getSupplierName());
                    }
                    po.setSupplierPaymentAccountStatus(SupplierPaymentAccountStatus.WAIT_EFFECTIVE_EXAMINE);
                    po.setSupplierPaymentAccountType(dto.getSupplierPaymentAccountType());
                    po.setSupplierPaymentCurrencyType(dto.getSupplierPaymentCurrencyType());
                    po.setBankName(dto.getBankName());
                    po.setAccountUsername(dto.getAccountUsername());
                    po.setBankSubbranchName(dto.getBankSubbranchName());
                    po.setSwiftCode(dto.getSwiftCode());
                    po.setBankProvince(dto.getBankProvince());
                    po.setBankCity(dto.getBankCity());
                    po.setBankArea(dto.getBankArea());
                    po.setIsDefault(BooleanType.FALSE);
                    po.setRemarks(dto.getRemarks());
                    po.setSubject(dto.getSubject());
                    return po;
                }).collect(Collectors.toList());
    }

    public static SupplierPaymentAccountPo editDtoToPo(SupplierPaymentAccountPo supplierPaymentAccountPo,
                                                       SupplierPaymentAccountEditDto dto,
                                                       SupplierPo supplierPo) {
        supplierPaymentAccountPo.setAccount(dto.getAccount());
        supplierPaymentAccountPo.setSupplierCode(dto.getSupplierCode());
        supplierPaymentAccountPo.setSupplierName(supplierPo.getSupplierName());
        supplierPaymentAccountPo.setSupplierPaymentAccountStatus(SupplierPaymentAccountStatus.WAIT_EFFECTIVE_EXAMINE);
        supplierPaymentAccountPo.setSupplierPaymentAccountType(dto.getSupplierPaymentAccountType());
        supplierPaymentAccountPo.setSupplierPaymentCurrencyType(dto.getSupplierPaymentCurrencyType());
        supplierPaymentAccountPo.setBankName(dto.getBankName());
        supplierPaymentAccountPo.setAccountUsername(dto.getAccountUsername());
        supplierPaymentAccountPo.setBankSubbranchName(dto.getBankSubbranchName());
        supplierPaymentAccountPo.setSwiftCode(dto.getSwiftCode());
        supplierPaymentAccountPo.setBankProvince(dto.getBankProvince());
        supplierPaymentAccountPo.setBankCity(dto.getBankCity());
        supplierPaymentAccountPo.setBankArea(dto.getBankArea());
        supplierPaymentAccountPo.setRemarks(dto.getRemarks());
        supplierPaymentAccountPo.setSubject(dto.getSubject());
        return supplierPaymentAccountPo;

    }

    public static List<SupplierPaymentAccountVo> poListToVoList(List<SupplierPaymentAccountPo> poList,
                                                                Map<Long, List<String>> fileCodePersonalMap,
                                                                Map<Long, List<String>> fileCodeAuthMap,
                                                                Map<Long, List<String>> fileCodeCompanyMap) {
        return Optional.ofNullable(poList)
                .orElse(new ArrayList<>())
                .stream().map(po -> {
                    final SupplierPaymentAccountVo supplierPaymentAccountVo = new SupplierPaymentAccountVo();
                    supplierPaymentAccountVo.setSupplierPaymentAccountId(po.getSupplierPaymentAccountId());
                    supplierPaymentAccountVo.setVersion(po.getVersion());
                    supplierPaymentAccountVo.setAccount(po.getAccount());
                    supplierPaymentAccountVo.setSupplierPaymentAccountStatus(po.getSupplierPaymentAccountStatus());
                    supplierPaymentAccountVo.setIsDefault(po.getIsDefault());
                    supplierPaymentAccountVo.setSupplierPaymentAccountType(po.getSupplierPaymentAccountType());
                    supplierPaymentAccountVo.setSupplierPaymentCurrencyType(po.getSupplierPaymentCurrencyType());
                    supplierPaymentAccountVo.setSupplierCode(po.getSupplierCode());
                    supplierPaymentAccountVo.setAccountUsername(po.getAccountUsername());
                    supplierPaymentAccountVo.setBankSubbranchName(po.getBankSubbranchName());
                    supplierPaymentAccountVo.setSwiftCode(po.getSwiftCode());
                    supplierPaymentAccountVo.setBankProvince(po.getBankProvince());
                    supplierPaymentAccountVo.setBankCity(po.getBankCity());
                    supplierPaymentAccountVo.setBankArea(po.getBankArea());
                    supplierPaymentAccountVo.setCreateUsername(po.getCreateUsername());
                    supplierPaymentAccountVo.setCreateTime(po.getCreateTime());
                    supplierPaymentAccountVo.setBankName(po.getBankName());
                    supplierPaymentAccountVo.setRemarks(po.getRemarks());
                    supplierPaymentAccountVo.setRemarks(po.getRemarks());
                    supplierPaymentAccountVo.setSubject(po.getSubject());

                    if (fileCodePersonalMap.containsKey(po.getSupplierPaymentAccountId())) {
                        supplierPaymentAccountVo.setPersonalFileCodeList(fileCodePersonalMap.get(po.getSupplierPaymentAccountId()));
                    }
                    if (fileCodeAuthMap.containsKey(po.getSupplierPaymentAccountId())) {
                        supplierPaymentAccountVo.setAuthFileCodeList(fileCodeAuthMap.get(po.getSupplierPaymentAccountId()));
                    }
                    if (fileCodeCompanyMap.containsKey(po.getSupplierPaymentAccountId())) {
                        supplierPaymentAccountVo.setCompanyFileCodeList(fileCodeCompanyMap.get(po.getSupplierPaymentAccountId()));
                    }

                    return supplierPaymentAccountVo;
                }).collect(Collectors.toList());
    }


}
