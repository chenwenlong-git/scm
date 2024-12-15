package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 发货仓库编码与SKU关系配置
 *
 * @author yanjiawei
 * Created on 2024/11/11.
 */
@Data
public class DeliveryWarehouseSkuBo {
    @ApiModelProperty(value = "原料发货仓库编码")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "SKU列表")
    private List<String> skuList;
}
