package com.hete.supply.scm.server.scm.cost.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/7/10 16:34
 */
@Data
@NoArgsConstructor
public class SkuAndWarehouseBatchBo {

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotBlank(message = "仓库编码不能为空")
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @NotNull(message = "仓库编码不能为空")
    @ApiModelProperty(value = "聚合类型：单仓、多仓")
    private PolymerizeType polymerizeType;

    @NotNull(message = "成本时间不能为空")
    @ApiModelProperty(value = "成本时间")
    private String costTime;

}
