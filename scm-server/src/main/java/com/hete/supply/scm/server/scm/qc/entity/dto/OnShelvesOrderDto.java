package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2023/10/13.
 */
@Data
@ApiModel(description = "上架单DTO")
public class OnShelvesOrderDto {
    @NotBlank(message = "上架单号为空")
    @ApiModelProperty(value = "上架单号")
    private String onShelvesOrderNo;

    @NotNull(message = "上架类型为空")
    @ApiModelProperty(value = "上架单生成类型：让步(CONCESSION)")
    private WmsEnum.OnShelvesOrderCreateType type;

    @NotBlank(message = "质检单号为空")
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @NotNull(message = "计划上架数量为空")
    @ApiModelProperty(value = "计划上架数量")
    private Integer planAmount;
}
