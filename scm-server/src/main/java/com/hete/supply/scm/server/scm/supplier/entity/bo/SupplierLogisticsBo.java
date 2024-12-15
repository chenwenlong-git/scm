package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 供应商物流时效
 *
 * @author yanjiawei
 * Created on 2024/8/12.
 */
@Data
@NoArgsConstructor
public class SupplierLogisticsBo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "物流时效（天）")
    private Integer logisticsDays;

    public long toLogisticsDays() {
        return logisticsDays == null ? 0 : logisticsDays;
    }
}
