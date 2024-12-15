package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.server.scm.enums.MaterialSkuType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author RockyHuas
 * @date 2022/11/12 12:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderMaterialVo {

    @ApiModelProperty(value = "原料产品明细 id")
    private Long processOrderMaterialId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "归还数量")
    private Integer backNum;

    @ApiModelProperty(value = "原料SKU所属类型")
    @NotNull(message = "原料SKU所属类型不能为空")
    private MaterialSkuType materialSkuType;

    @ApiModelProperty("版本号")
    private Integer version;

}
