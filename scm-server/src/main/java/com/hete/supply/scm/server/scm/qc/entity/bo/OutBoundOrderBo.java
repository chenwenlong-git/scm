package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/6/28.
 */
@Data
@ApiModel(description = "出库单业务对象")
public class OutBoundOrderBo {
    @ApiModelProperty(value = "出库单号", required = true, example = "OUT123456")
    private String outboundOrderNo;

    @ApiModelProperty(value = "仓库编码", required = true, example = "WH123456")
    private String warehouseCode;

    @ApiModelProperty(value = "出库单明细列表", required = true)
    private List<OutBoundOrderDetailBo> orderDetailList;

    @Data
    @ApiModel(description = "出库单明细")
    public static class OutBoundOrderDetailBo {
        @ApiModelProperty(value = "SKU", required = true, example = "SKU12345")
        private String sku;

        @ApiModelProperty(value = "批次码", required = true, example = "BATCH123456")
        private String batchCode;

        @ApiModelProperty(value = "数量", required = true, example = "100")
        private Integer quantity;

        @ApiModelProperty(value = "供应商编码", example = "PO123456", required = true)
        private String supplierCode;
    }
}
