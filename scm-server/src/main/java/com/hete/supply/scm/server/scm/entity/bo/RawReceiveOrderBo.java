package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/3/27 20:10
 */
@Data
@NoArgsConstructor
public class RawReceiveOrderBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "入库总数")
    private Integer amount;

    @ApiModelProperty(value = "入库单单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "入库数")
    private Integer receiveAmount;

    @ApiModelProperty(value = "状态")
    private WmsEnum.ReceiveOrderState receiveOrderState;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
}
