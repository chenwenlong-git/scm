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
@ApiModel(description = "打印返修单原料归还信息的值对象")
public class RepairOrderPrintMaterialReturnVo {
    @ApiModelProperty(value = "返修单号", example = "RX123456")
    private String repairOrderNo;

    @ApiModelProperty(value = "原料归还信息列表")
    private List<PrintMaterialReturnInfoVo> printMaterialReturnInfoList;

    @Data
    @ApiModel(description = "成品信息收货单")
    public static class PrintMaterialReturnInfoVo {

        @ApiModelProperty(value = "收货单号", example = "SH123")
        private String receiptOrderNo;

        @ApiModelProperty(value = "仓库编码", example = "WH001")
        private String warehouseCode;
    }
}
