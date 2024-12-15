package com.hete.supply.scm.server.scm.qc.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/6/28.
 */
@ApiModel(description = "创建质检单的业务单据信息")
@Data
public class BizOrderCreateQcVo {
    @ApiModelProperty(value = "调拨单号", required = true, example = "BD123456")
    private String outBoundNo;

    @ApiModelProperty(value = "平台编码")
    private String platCode;

    @ApiModelProperty(value = "运单号")
    private String expressOrderNo;

    @ApiModelProperty(value = "质检标识", example = "PO123456", required = true)
    private QcOriginProperty qcOriginProperty;

    @ApiModelProperty(value = "仓库编码", example = "PO123456")
    private String warehouseCode;

    @ApiModelProperty(value = "单据明细信息列表", required = true)
    private List<BizOrderDetailVo> detailList;

    @Data
    public static class BizOrderDetailVo {
        @ApiModelProperty(value = "SKU", required = true, example = "SKU001")
        private String sku;

        @ApiModelProperty(value = "批次码", required = true, example = "SKU001")
        private String batchCode;

        @ApiModelProperty(value = "数量", required = true, example = "10")
        private int quantity;

        @ApiModelProperty(value = "供应商编码", example = "PO123456", required = true)
        private String supplierCode;
    }
}
