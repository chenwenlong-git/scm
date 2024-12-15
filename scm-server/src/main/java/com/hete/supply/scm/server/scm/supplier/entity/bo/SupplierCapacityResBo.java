package com.hete.supply.scm.server.scm.supplier.entity.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2024/8/7.
 */
@Data
public class SupplierCapacityResBo {
    // 供应商编码
    private String supplierCode;
    // 产能日期
    private LocalDate capacityDate;
    // 剩余常规产能
    private BigDecimal normalAvailableCapacity = BigDecimal.ZERO;
}
