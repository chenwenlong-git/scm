package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/8/5 16:09
 */
@Data
@NoArgsConstructor
public class SkuGetSuggestSupplierDto {

    @ApiModelProperty(value = "SKU列表信息")
    @NotEmpty(message = "SKU列表信息不能为空")
    @Size(max = 20, message = "SKU列表信息数量不能超过20个")
    private List<SkuAndBusinessIdBatchDto> skuAndBusinessIdBatchList;

    @Data
    @Valid
    public static class SkuAndBusinessIdBatchDto {

        @NotNull(message = "业务ID不能为空")
        @ApiModelProperty(value = "业务ID")
        private Long businessId;

        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;

        @NotNull(message = "最晚上架时间不能为空")
        @ApiModelProperty(value = "最晚上架时间(天CN)")
        private LocalDate latestOnShelfTime;

        @NotNull(message = "预计下单数量不能为空")
        @ApiModelProperty(value = "预计下单数量")
        private Integer placeOrderCnt;

    }


}
