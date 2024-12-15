package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/9/11 11:13
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrPriceUpdateBo {

    @NotBlank(message = "蕾丝面积属性值不能为空")
    @ApiModelProperty(value = "蕾丝面积属性值")
    private String laceAttrValue;


    @NotBlank(message = "裆长尺寸属性值不能为空")
    @ApiModelProperty(value = "裆长尺寸属性值")
    private String sizeAttrValue;


    @ApiModelProperty(value = "sku价格")
    @NotNull(message = "sku价格不能为空")
    private BigDecimal skuPrice;

    @NotNull(message = "材料属性值不能为空")
    @ApiModelProperty(value = "材料属性值")
    private String materialAttrValue;

}
