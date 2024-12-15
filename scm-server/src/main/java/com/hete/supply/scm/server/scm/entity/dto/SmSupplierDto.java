package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/18 16:11
 */
@Data
@NoArgsConstructor
public class SmSupplierDto {
    @ApiModelProperty(value = "id")
    private Long smSupplierId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "供应商代码")
    @NotBlank(message = "供应商代码不能为空")
    @Length(max = 32, message = "供应商代码长度不能超过32个字符")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    @NotBlank(message = "供应商名称不能为空")
    @Length(max = 32, message = "供应商名称长度不能超过32个字符")
    private String supplierName;

    @ApiModelProperty(value = "采购价")
    @NotNull(message = "采购价不能为空")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "产能")
    private Integer capacity;

    @ApiModelProperty(value = "入驻时间")
    private LocalDateTime joinTime;
}
