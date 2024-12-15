package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/3/27 18:12
 */
@Data
@NoArgsConstructor
public class RawDeliverOrderBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数")
    private Integer amount;

    @ApiModelProperty(value = "收货数")
    private Integer receiptNum;

    @ApiModelProperty(value = "出库单单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "数量")
    private Integer deliveryAmount;

    @ApiModelProperty(value = "状态")
    private WmsEnum.DeliveryState deliveryState;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;
}
