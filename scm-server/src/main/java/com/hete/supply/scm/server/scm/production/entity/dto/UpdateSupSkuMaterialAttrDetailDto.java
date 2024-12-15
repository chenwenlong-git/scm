package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.CrotchLength;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/9/20.
 */
@Data
public class UpdateSupSkuMaterialAttrDetailDto {

    @ApiModelProperty(value = "裆长尺寸")
    @NotNull(message = "裆长尺寸不能为空")
    private CrotchLength crotchLength;

    @ApiModelProperty(value = "裆长部位")
    @NotBlank(message = "裆长部位不能为空")
    @Length(max = 500, message = "裆长部位长度字符不能超过500")
    private String crotchPosition;

    @ApiModelProperty(value = "深色克重")
    @PositiveOrZero(message = "深色克重必须大于0")
    @Digits(integer = 6, fraction = 2, message = "深色克重整数最多6位，小数最多2位")
    private BigDecimal darkWeight;

    @ApiModelProperty(value = "浅色克重")
    @PositiveOrZero(message = "浅色克重必须大于0")
    @Digits(integer = 6, fraction = 2, message = "浅色克重整数最多6位，小数最多2位")
    private BigDecimal lightWeight;

    @ApiModelProperty(value = "裆长配比")
    @NotBlank(message = "裆长配比不能为空")
    @Length(max = 500, message = "裆长配比字符长度不能超过500")
    private String crotchLengthRatio;

    @ApiModelProperty(value = "原料克重")
    @NotNull(message = "原料克重不能为空")
    @DecimalMin(value = "0.00", inclusive = false, message = "原料克重必须大于0.00")
    @Digits(integer = 6, fraction = 2, message = "原料克重整数最多6位，小数最多2位")
    private BigDecimal weight;
}
