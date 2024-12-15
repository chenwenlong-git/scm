package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/3/8.
 */
@Data
@ApiModel(description = "原料收货信息")
public class RepairMaterialReceiptVo {
    @ApiModelProperty(notes = "原料收货批次码")
    private String batchCode;

    @ApiModelProperty(notes = "出库数量")
    private int outQuantity;

    @ApiModelProperty(notes = "收货数量")
    private int receiptQuantity;

    @ApiModelProperty(notes = "已归还数量")
    private int returnedQuantity;

    @ApiModelProperty(notes = "可归还数量")
    private int returnableQuantity;

    @ApiModelProperty(notes = "加工绑定数量")
    private int processedBindingQuantity;

    @ApiModelProperty(notes = "质检次品数量")
    private int qualityInspectionDefectQuantity;

    // Getter and setter methods
}
