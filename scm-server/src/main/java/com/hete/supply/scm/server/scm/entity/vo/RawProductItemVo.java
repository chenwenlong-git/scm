package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/15 20:31
 */
@Data
@NoArgsConstructor
public class RawProductItemVo {
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderRawId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "原料sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "原料提供方")
    private RawSupplier rawSupplier;

    @ApiModelProperty(value = "收货数（可归还数）")
    private Integer receiptCnt;
}
