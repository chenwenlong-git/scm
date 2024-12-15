package com.hete.supply.scm.server.scm.adjust.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/8/26 17:00
 */
@Data
@NoArgsConstructor
public class GoodsPriceAddBo {

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku不能为空")
    private String sku;

    @ApiModelProperty(value = "供应商代码")
    @NotBlank(message = "供应商代码不能为空")
    private String supplierCode;

    @ApiModelProperty(value = "关联渠道ID")
    @NotNull(message = "关联渠道ID不能为空")
    private Long channelId;

    @ApiModelProperty(value = "渠道价格")
    @NotNull(message = "渠道价格不能为空")
    private BigDecimal channelPrice;

}
