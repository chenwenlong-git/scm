package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2023/3/7 10:45
 */
@Data
@NoArgsConstructor
public class SampleSpecialItemDto {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    @Length(max = 32, message = "供应商代码不能超过32个字符")
    private String supplierCode;

    @NotNull(message = "采购数不能为空")
    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "打样单价")
    @NotNull(message = "打样单价不能为空")
    private BigDecimal proofingPrice;

    @NotNull(message = "样品成本单价不能为空")
    @ApiModelProperty(value = "样品成本单价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "供应商生产信息")
    @Length(max = 255, message = "供应商生产信息不能超过255个字符")
    private String supplierProduction;
}
