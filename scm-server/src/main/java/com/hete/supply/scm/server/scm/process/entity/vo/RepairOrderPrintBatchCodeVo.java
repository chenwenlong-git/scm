package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/1/19 11:00
 */
@Data
@NoArgsConstructor
public class RepairOrderPrintBatchCodeVo {

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "返修单号的明细批次码列表")
    private List<RepairOrderPrintBatchCodeItemVo> repairOrderPrintBatchCodeItemList;

    @Data
    public static class RepairOrderPrintBatchCodeItemVo {
        @ApiModelProperty(value = "批次码")
        private String batchCode;

        @ApiModelProperty(value = "SKU")
        private String sku;
    }


}
