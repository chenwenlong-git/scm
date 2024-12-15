package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/3/4 14:34
 */
@Data
@NoArgsConstructor
public class ProduceDataUpdatePurchasePriceBo {


    @NotBlank(message = "更新生产信息的商品采购价格时SKU不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "商品采购价格")
    @NotNull(message = "更新生产信息的商品采购价格不能为空")
    private BigDecimal goodsPurchasePrice;


}
