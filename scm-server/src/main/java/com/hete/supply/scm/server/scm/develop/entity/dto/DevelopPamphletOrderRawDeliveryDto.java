package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/17 14:37
 */
@Data
@NoArgsConstructor
public class DevelopPamphletOrderRawDeliveryDto {
    @NotBlank(message = "版单号不能为空")
    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @NotBlank(message = "原料仓库编码不能为空")
    @ApiModelProperty(value = "原料仓库编码")
    private String rawWarehouseCode;

    @NotBlank(message = "原料仓库名称不能为空")
    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;

    @ApiModelProperty(value = "原料产品明细列表")
    @Valid
    @NotEmpty(message = "原料产品明细列表不能为空")
    private List<DevelopPamphletOrderRawListDto> rawList;

}
