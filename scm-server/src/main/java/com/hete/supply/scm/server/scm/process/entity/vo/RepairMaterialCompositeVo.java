package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/3/8.
 */
@Data
@ApiModel(description = "原料组合信息（返修单原料信息&原料收货信息&原料收货明细）")
public class RepairMaterialCompositeVo {
    @ApiModelProperty(notes = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(notes = "原料主键id")
    private Long materialId;

    @ApiModelProperty(notes = "原料SKU")
    private String sku;

    @ApiModelProperty(notes = "产品名称")
    private String skuEncode;

    @ApiModelProperty(notes = "需求数量")
    private int requiredQuantity;

    @ApiModelProperty(notes = "原料收货信息列表")
    private List<RepairMaterialReceiptVo> repairMaterialReceiptVos;
}
