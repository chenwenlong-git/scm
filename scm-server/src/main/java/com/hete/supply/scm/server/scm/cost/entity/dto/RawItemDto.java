package com.hete.supply.scm.server.scm.cost.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/2/28 11:33
 */
@Data
@NoArgsConstructor
public class RawItemDto {
    @NotBlank(message = "原料sku不能为空")
    @ApiModelProperty(value = "原料sku")
    private String rawSku;

    @NotNull(message = "原料sku数量不能为空")
    @ApiModelProperty(value = "原料sku数量")
    private Integer rawCnt;

    @ApiModelProperty(value = "成本价格（前端不需要传该参数）")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "原料仓库代码")
    private String rawWarehouseCode;
}
