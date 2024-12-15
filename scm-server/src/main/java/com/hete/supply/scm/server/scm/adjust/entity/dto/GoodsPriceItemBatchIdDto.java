package com.hete.supply.scm.server.scm.adjust.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/7/23 16:11
 */
@Data
@NoArgsConstructor
public class GoodsPriceItemBatchIdDto {

    @NotEmpty(message = "批量商品价格禁止信息不能为空")
    @ApiModelProperty(value = "批量商品价格禁止信息")
    @Valid
    private List<GoodsPriceItemBatchIdInfoDto> goodsPriceItemBatchIdInfoList;

    @Data
    public static class GoodsPriceItemBatchIdInfoDto {

        @NotNull(message = "id不能为空")
        @ApiModelProperty(value = "id")
        private Long goodsPriceItemId;

        @NotNull(message = "版本号不能为空")
        @ApiModelProperty(value = "版本号")
        private int version;

    }


}
