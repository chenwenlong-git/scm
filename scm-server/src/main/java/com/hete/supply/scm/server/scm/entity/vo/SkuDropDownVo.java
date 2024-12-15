package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class SkuDropDownVo {

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "单据号明细列表")
    private List<SkuDropDownItemVo> skuDropDownItemList;

    @Data
    public static class SkuDropDownItemVo {
        @ApiModelProperty(value = "spu")
        private String spu;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "结算单价")
        private BigDecimal settleUnitPrice;

        @ApiModelProperty(value = "上架数")
        private Integer receiveAmount;

    }


}
