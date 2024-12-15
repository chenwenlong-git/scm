package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/3/20 14:01
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrValueBySkuDto {

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku不能为空")
    private String sku;

    @ApiModelProperty(name = "属性ID")
    @NotEmpty(message = "属性ID不能为空")
    @Valid
    private List<Long> attributeNameIdList;

}
