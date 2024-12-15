package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/3/25.
 */
@Data
@ApiModel(description = "打印返修单成品收货信息的值对象")
public class RepairOrderPrintProductReceiptVo {
    @ApiModelProperty(value = "返修单号", example = "RX123456")
    private String repairOrderNo;

    @ApiModelProperty(value = "成品信息收货单列表")
    private List<ProductReceiptInfoVo> productReceiptInfoList;

    @Data
    @ApiModel(description = "成品信息收货单")
    public static class ProductReceiptInfoVo {

        @ApiModelProperty(value = "收货单号", example = "SH123")
        private String receiptOrderNo;

        @ApiModelProperty(value = "仓库编码", example = "WH001")
        private String warehouseCode;
    }
}
