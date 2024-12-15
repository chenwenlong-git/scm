package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/10/9.
 */
@Data
public class GetMaintainableAttrDto {
    @NotNull(message = "sku编码不能为空")
    @ApiModelProperty(value = "sku编码")
    private String sku;
}
