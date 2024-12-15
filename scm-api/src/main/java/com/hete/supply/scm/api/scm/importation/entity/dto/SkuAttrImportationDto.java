package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/6/18 17:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SkuAttrImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "蕾丝面积属性值")
    private String laceAttrValue;

    @ApiModelProperty(value = "档长尺寸属性值")
    private String sizeAttrValue;

    @ApiModelProperty(value = "材料属性值")
    private String materialAttrValue;

    @ApiModelProperty(value = "sku价格")
    private BigDecimal skuPrice;

}
