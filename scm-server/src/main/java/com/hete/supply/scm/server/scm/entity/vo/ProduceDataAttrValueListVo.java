package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/4/10 10:31
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrValueListVo {

    @ApiModelProperty(value = "sku")
    private String sku;
}
