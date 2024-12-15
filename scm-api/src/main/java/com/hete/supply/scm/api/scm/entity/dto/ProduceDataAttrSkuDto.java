package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/9/24 11:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProduceDataAttrSkuDto extends ComPageDto {
    @NotNull(message = "关联属性ID不能为空")
    @ApiModelProperty(value = "关联属性ID")
    private Long attributeNameId;

    @NotEmpty(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private List<String> skuList;
}
