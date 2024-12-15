package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/11/20.
 */
@Data
@AllArgsConstructor
public class ProduceDataRamImpDto {
    @ApiModelProperty(value = "sku")
    private String rawSku;

    @ApiModelProperty(value = "sku数量")
    private String skuCnt;
}
