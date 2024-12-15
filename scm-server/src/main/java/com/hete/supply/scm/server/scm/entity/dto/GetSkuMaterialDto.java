package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 获取sku原料信息
 *
 * @author yanjiawei
 * @date 2023/07/25 09:48
 */
@Data
@ApiModel(value = "获取sku原料信息", description = "获取sku原料信息")
public class GetSkuMaterialDto {

    @ApiModelProperty(value = "订单类型")
    @NotNull(message = "订单类型不能为空")
    private ProcessOrderType processOrderType;

    @ApiModelProperty(value = "processSku")
    @NotBlank(message = "sku信息不能为空")
    private String processSku;

    @ApiModelProperty(value = "平台编码")
    @NotBlank(message = "平台编码不能为空")
    private String platform;
}