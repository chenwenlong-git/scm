package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/11/25.
 */
@Data
@AllArgsConstructor
public class DeliveryDetailKeyBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode = "";
}
