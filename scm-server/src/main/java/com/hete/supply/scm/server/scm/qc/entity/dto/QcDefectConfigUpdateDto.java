package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/10/17 14:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QcDefectConfigUpdateDto extends QcDefectConfigIdAndVersionDto {
    @NotBlank(message = "次品类别不能为空")
    @Length(max = 10, message = "次品类别不能超过10个字符")
    @ApiModelProperty(value = "次品类别")
    private String defectCategory;

    @NotBlank(message = "次品代码不能为空")
    @Length(max = 5, message = "次品代码不能超过5个字符")
    @ApiModelProperty(value = "次品代码")
    private String defectCode;
}
