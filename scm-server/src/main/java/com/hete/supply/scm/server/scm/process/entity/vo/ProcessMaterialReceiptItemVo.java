package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author RockyHuas
 * @date 2022/11/11 14:04
 */
@Data
@NoArgsConstructor
public class ProcessMaterialReceiptItemVo {

    @ApiModelProperty(value = "主键 ID")
    private Long processMaterialReceiptItemId;


    @ApiModelProperty(value = "关联的原料收货单 id")
    private Long processMaterialReceiptId;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku 批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;


    @ApiModelProperty(value = "收货数量")
    private Integer receiptNum;

    @ApiModelProperty(value = "版本号")
    private Integer version;


}
