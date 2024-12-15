package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/4/29.
 */
@Data
public class PurchaseQcCreateRequestItemBo {
    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "质检数量")
    private int qcAmount;
}
