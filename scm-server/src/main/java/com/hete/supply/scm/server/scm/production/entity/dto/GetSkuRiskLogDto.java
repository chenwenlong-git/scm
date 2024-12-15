package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class GetSkuRiskLogDto {

    @NotNull(message = "sku编码不能为空")
    @ApiModelProperty(value = "sku编码")
    private String sku;
}
