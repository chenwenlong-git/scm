package com.hete.supply.scm.server.scm.adjust.entity.dto;

import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/6/18 15:31
 */
@Data
@NoArgsConstructor
public class GoodsPriceGetPurchaseListDto {

    @ApiModelProperty(value = "检索条件")
    @Valid
    private List<GoodsPriceGetPurchaseItemDto> goodsPriceGetListPurchaseItemList;

    @ApiModelProperty(value = "检索条件")
    @Valid
    private List<SkuUniversalDto> skuUniversalList;

    @Data
    public static class SkuUniversalDto {
        @NotNull(message = "设置通用不能为空")
        @ApiModelProperty(value = "设置通用")
        private GoodsPriceUniversal goodsPriceUniversal;

        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;

        @NotBlank(message = "供应商编码不能为空")
        @ApiModelProperty(value = "供应商编码")
        private String supplierCode;
    }

    @Data
    public static class GoodsPriceGetPurchaseItemDto {

        @NotBlank(message = "供应商编码不能为空")
        @ApiModelProperty(value = "供应商编码")
        private String supplierCode;

        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;
    }

}
