package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单次品赋码参数", description = "加工单次品赋码参数")
public class ProcessOrderAddSkuDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;

    @ApiModelProperty(value = "次品sku")
    @NotBlank(message = "次品sku 不能为空")
    @Length(max = 100, message = "次品sku字符长度不能超过 100 位")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    @Length(max = 32, message = "sku批次码字符长度不能超过 32 位")
    private String skuBatchCode;

    @ApiModelProperty(value = "变体属性")
    private String variantProperties;

    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须为正整数")
    private Integer processNum;

    @ApiModelProperty(value = "采购单价")
    @DecimalMin(value = "0", message = "采购单价不能小于0")
    private BigDecimal purchasePrice;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
