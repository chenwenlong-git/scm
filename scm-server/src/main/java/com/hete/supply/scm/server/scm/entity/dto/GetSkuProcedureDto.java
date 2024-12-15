package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 获取sku工序信息
 *
 * @author yanjiawei
 * @date 2023/07/25 09:47
 */
@Data
@ApiModel(value = "获取sku工序信息", description = "获取sku工序信息")
public class GetSkuProcedureDto {

    @ApiModelProperty(value = "订单类型")
    @NotNull(message = "订单类型不能为空")
    private ProcessOrderType processOrderType;

    @ApiModelProperty(value = "加工sku")
    @NotBlank(message = "sku信息不能为空")
    private String processSku;
}