package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author RockyHuas
 * @date 2023/01/31 17:21
 */
@Data
@NoArgsConstructor
public class SkuListDto {

    @NotEmpty(message = "sku列表不能为空")
    @ApiModelProperty(value = "sku")
    private List<String> skuList;
}
