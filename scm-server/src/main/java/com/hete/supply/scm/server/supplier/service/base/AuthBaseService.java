package com.hete.supply.scm.server.supplier.service.base;

import com.hete.supply.scm.remote.udb.UserService;
import com.hete.supply.udb.api.entity.vo.UserSupplierAuthInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author RockyHuas
 * @date 2023/01/03 11:30
 */
@Service
@RequiredArgsConstructor
@Validated
public class AuthBaseService {
    private final UserService userService;

    /**
     * 获取供应商权限 code
     *
     * @return
     */
    public List<String> getSupplierCodeList() {
        UserSupplierAuthInfoVo userSupplierAuthInfo = userService.getUserSupplierAuthInfo();
        List<String> supplierCodeList = userSupplierAuthInfo.getSupplierCodeList();
        return Optional.ofNullable(supplierCodeList).orElse(Collections.emptyList());
    }


}
