package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/7/3 09:41
 */
@Data
@NoArgsConstructor
public class ProduceDataSaveTopDto {

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "版本")
    private int version;

    @NotNull(message = "商品类目id不能为空")
    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

    @ApiModelProperty(value = "克重")
    @NotNull(message = "克重不能为空")
    @DecimalMin(value = "0.01", message = "克重必须大于0")
    private BigDecimal weight;

    @ApiModelProperty(value = "商品采购价格")
    @NotNull(message = "商品采购价格不能为空")
    private BigDecimal goodsPurchasePrice;

    @ApiModelProperty(value = "原料管理")
    @NotNull(message = "原料管理不能为空")
    private BooleanType rawManage;

}
