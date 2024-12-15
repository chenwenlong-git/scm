package com.hete.supply.scm.server.scm.settle.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2022/11/24 20:39
 */
@Data
@NoArgsConstructor
public class SettleSupplierBo {

    @ApiModelProperty(value = "采购结算单id")
    private Long purchaseSettleOrderId;

    @ApiModelProperty(value = "采购结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
}
