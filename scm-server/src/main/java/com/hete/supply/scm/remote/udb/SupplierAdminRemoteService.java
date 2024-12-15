package com.hete.supply.scm.remote.udb;

import com.hete.supply.udb.api.entity.dto.SupplierUpdateDto;
import com.hete.supply.udb.api.service.SupplierAdminFacade;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * 供应商服务
 *
 * @author ChenWenLong
 * @date 2022/11/28 16:39
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SupplierAdminRemoteService {

    @DubboReference(check = false)
    private SupplierAdminFacade supplierAdminFacade;

    @Nullable
    public CommonResult<Void> insertUpdateSupplier(SupplierUpdateDto dto) {
        return supplierAdminFacade.insertUpdateSupplier(dto);
    }
}
