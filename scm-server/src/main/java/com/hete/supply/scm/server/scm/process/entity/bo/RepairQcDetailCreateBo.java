package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/1/9.
 */
@Data
public class RepairQcDetailCreateBo {

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "待质检数量")
    private Integer pendingQcQuantity;

    @ApiModelProperty(value = "质检合格数量")
    private Integer qcPassQuantity;

    @ApiModelProperty(value = "质检不合格数量")
    private Integer qcFailQuantity;

    @ApiModelProperty(value = "返修明细id")
    private Long repairOrderItemId;

}
