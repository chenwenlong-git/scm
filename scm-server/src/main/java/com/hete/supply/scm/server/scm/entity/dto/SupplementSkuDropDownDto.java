package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplementOrderPurchaseType;
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
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class SupplementSkuDropDownDto {

    @ApiModelProperty(name = "单据信息")
    @NotEmpty(message = "单据信息不能为空")
    @Valid
    private List<SkuDropDownItemDto> skuDropDownItemList;

    @Data
    public static class SkuDropDownItemDto {
        @NotNull(message = "单据类型不能为空")
        @ApiModelProperty(value = "单据类型")
        private SupplementOrderPurchaseType supplementOrderPurchaseType;

        @NotBlank(message = "单据号不能为空")
        @ApiModelProperty(value = "单据号")
        private String businessNo;
    }

}
