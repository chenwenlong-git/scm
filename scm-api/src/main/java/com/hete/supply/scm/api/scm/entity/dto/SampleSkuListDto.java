package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/13 09:36
 */
@Data
@NoArgsConstructor
public class SampleSkuListDto {
    @ApiModelProperty(value = "sku列表")
    @NotEmpty(message = "sku列表不能为空")
    private List<String> skuList;
}
