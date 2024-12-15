package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/4/15.
 */
@Data
public class PurchaseCompleteQcItemBo {
    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "采购数量=质检数量")
    private int qcAmount;
}
