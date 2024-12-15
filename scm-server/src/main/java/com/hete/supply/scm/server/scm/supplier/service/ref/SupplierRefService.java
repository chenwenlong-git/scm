package com.hete.supply.scm.server.scm.supplier.service.ref;

import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.udb.api.enums.EnableStateEnum;
import com.hete.support.api.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author yanjiawei
 * Created on 2024/8/10.
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierRefService {
    /**
     * 调用UDB获取对应的枚举
     *
     * @author ChenWenLong
     * @date 2022/11/28 15:59
     */
    public EnableStateEnum getEnableStateEnumByStatus(SupplierStatus supplierStatus) {
        if (SupplierStatus.ENABLED.equals(supplierStatus)) {
            return EnableStateEnum.ENABLED;
        }
        if (SupplierStatus.DISABLED.equals(supplierStatus)) {
            return EnableStateEnum.DISABLED;
        }
        throw new BizException("状态错误，存在非法状态");
    }
}
