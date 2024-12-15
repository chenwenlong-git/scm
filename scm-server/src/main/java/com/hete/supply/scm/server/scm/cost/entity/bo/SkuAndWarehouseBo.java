package com.hete.supply.scm.server.scm.cost.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/2/28 09:52
 */
@Data
@NoArgsConstructor
public class SkuAndWarehouseBo {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @NotNull(message = "仓库编码不能为空")
    @ApiModelProperty(value = "聚合类型：单仓、多仓")
    private PolymerizeType polymerizeType;

    @ApiModelProperty(value = "仓库聚合维度")
    private PolymerizeWarehouse polymerizeWarehouse;
}
