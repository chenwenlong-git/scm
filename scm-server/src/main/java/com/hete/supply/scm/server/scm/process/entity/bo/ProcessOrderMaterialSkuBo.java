package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author RockyHuas
 * @date 2022/12/06 18:12
 */
@Data
@NoArgsConstructor
public class ProcessOrderMaterialSkuBo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "库位号/货架号")
    private String shelfCode;
}
