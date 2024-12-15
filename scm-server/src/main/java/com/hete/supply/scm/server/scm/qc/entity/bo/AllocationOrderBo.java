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
@ApiModel(description = "调拨单业务对象")
public class AllocationOrderBo {
    @ApiModelProperty(value = "调拨单号", example = "ALLOC123456")
    private String allocationOrderNo;

    @ApiModelProperty(value = "仓库编码", example = "WH123456")
    private String warehouseCode;

    @ApiModelProperty(value = "供应商编码", example = "PO123456", required = true)
    private String supplierCode;

    @ApiModelProperty(value = "调拨单明细信息")
    private List<AllocationOrderDetailBo> orderDetailList;

    @Data
    @ApiModel(description = "调拨单明细信息")
    public static class AllocationOrderDetailBo {

        @ApiModelProperty(value = "SKU", example = "SKU12345")
        private String sku;

        @ApiModelProperty(value = "批次码", example = "BATCH123456")
        private String batchCode;

        @ApiModelProperty(value = "数量", example = "100")
        private Integer quantity;

        @ApiModelProperty(value = "容器编码", example = "C123456")
        private String containerCode;
    }
}
