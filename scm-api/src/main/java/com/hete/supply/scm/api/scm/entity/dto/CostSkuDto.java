package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/2/29 10:26
 */
@Data
@NoArgsConstructor
public class CostSkuDto {
    @Size(max = 200, message = "sku入参不能超过200个")
    @NotEmpty(message = "sku入参不能为空")
    @ApiModelProperty(value = "入参")
    private List<CostSkuItemDto> costSkuItemList;

}
