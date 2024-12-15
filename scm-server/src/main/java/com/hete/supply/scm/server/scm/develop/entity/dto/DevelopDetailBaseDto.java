package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/4 13:58
 */
@Data
@NoArgsConstructor
public class DevelopDetailBaseDto {

    @ApiModelProperty(value = "产品名称")
    @NotBlank(message = "产品名称不能为空")
    private String skuEncode;

    @ApiModelProperty(value = "样品价格")
    @NotNull(message = "样品价格不能为空")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "开发子单属性列表")
    @Valid
    private List<DevelopChildOrderAttrDto> developChildOrderAttrList;

    @ApiModelProperty(value = "渠道大货价格")
    @NotNull(message = "渠道大货价格不能为空")
    DevelopOrderPriceSaveDto developOrderPrice;

}
