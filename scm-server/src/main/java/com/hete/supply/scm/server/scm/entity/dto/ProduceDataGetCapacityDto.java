package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/8/9 17:14
 */
@Data
@NoArgsConstructor
public class ProduceDataGetCapacityDto {

    @ApiModelProperty(value = "SKU列表信息")
    @NotEmpty(message = "SKU列表信息不能为空")
    private List<ProduceDataGetCapacityItemDto> produceDataGetCapacityItemList;

    @Data
    @Valid
    public static class ProduceDataGetCapacityItemDto {

        @NotNull(message = "业务ID不能为空")
        @ApiModelProperty(value = "业务ID")
        private Long businessId;

        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;

        @NotBlank(message = "供应商代码不能为空")
        @ApiModelProperty(value = "供应商代码")
        private String supplierCode;

        @ApiModelProperty(value = "预计下单数量")
        @NotNull(message = "预计下单数量不能为空")
        private Integer placeOrderCnt;
    }


}
