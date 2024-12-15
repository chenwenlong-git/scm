package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * @Description 加工单原料校验库存实体
 * @Date 2023/9/25 14:35
 */
@Data
@ApiModel(description = "校验原料库存的请求对象")
public class MaterialInventoryCheckBo {

    @ApiModelProperty(value = "原料SKU列表")
    private String materialSku;

    @ApiModelProperty(value = "原料SKU所需库存数")
    private Integer requiredInventory;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "产品质量")
    private WmsEnum.ProductQuality productQuality;
}
