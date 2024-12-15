package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/2/5.
 */
@Data
@ApiModel(value = "RepairOrderMaterialBindingBo", description = "返修单绑定信息业务对象")
public class RepairOrderBindingInfoBo {

    @ApiModelProperty(value = "批次码", example = "BATCH001")
    private String batchCode;

    @ApiModelProperty(value = "SKU", example = "ABC123")
    private String sku;

    @ApiModelProperty(value = "出库数量", example = "20")
    private int outboundQuantity;

    @ApiModelProperty(value = "收货数量", example = "30")
    private int receivedQuantity;

    @ApiModelProperty(value = "返修原料消耗数量", example = "10")
    private int materialUsageQuantity;

    @ApiModelProperty(value = "已归还数量", example = "5")
    private int returnedQuantity;

    public int getAvailableForReturn() {
        return receivedQuantity - materialUsageQuantity - returnedQuantity;
    }
}
