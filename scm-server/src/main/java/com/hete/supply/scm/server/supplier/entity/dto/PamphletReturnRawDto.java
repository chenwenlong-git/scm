package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.scm.server.supplier.develop.entity.dto.DevelopPamphletOrderRawItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/17 18:01
 */
@Data
@NoArgsConstructor
public class PamphletReturnRawDto {
    @NotBlank(message = "版单号不能为空")
    @ApiModelProperty(value = "版单号号")
    private String developPamphletOrderNo;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "原料产品明细列表")
    @Valid
    @NotEmpty(message = "原料产品明细列表不能为空")
    private List<DevelopPamphletOrderRawItemDto> rawItemList;
}
