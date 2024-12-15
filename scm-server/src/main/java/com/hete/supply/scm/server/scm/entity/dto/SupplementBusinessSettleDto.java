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
 * @date 2023/11/15 17:57
 */
@Data
@NoArgsConstructor
public class SupplementBusinessSettleDto {

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(name = "单据信息")
    @NotEmpty(message = "单据信息不能为空")
    @Valid
    private List<SupplementBusinessSettleItemDto> supplementBusinessSettleItemList;

    @Data
    public static class SupplementBusinessSettleItemDto {

        @NotNull(message = "单据类型不能为空")
        @ApiModelProperty(value = "单据类型")
        private SupplementOrderPurchaseType supplementOrderPurchaseType;

        @NotBlank(message = "单据号不能为空")
        @ApiModelProperty(value = "单据号")
        private String businessNo;
    }

}
