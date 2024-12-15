package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/27 16:16
 */
@Data
@NoArgsConstructor
public class EmployGradeEditDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long employeeGradeId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty("版本号")
    private Integer version;

    @NotBlank(message = "职级名称不能为空")
    @ApiModelProperty(value = "职级名称")
    @Length(max = 20, message = "职级名称长度不能超过20")
    private String gradeName;

    @NotNull(message = "职级优先级不能为空")
    @ApiModelProperty(value = "职级优先级，数值越大职级越高")
    @Digits(integer = 3, fraction = 1, message = "职级配置最多为一位小数，请重新填写后提交")
    private BigDecimal gradeLevel;

    @NotEmpty(message = "能力范围不能为空")
    @ApiModelProperty(value = "能力范围")
    @Valid
    private List<ProcessSimpleDto> processSimpleList;
}
