package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/4/4 10:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleChildItemDto extends SampleSpecialItemDto {
    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku 不能为空")
    @Length(max = 100, message = "sku字符长度不能超过 100 位")
    private String sku;
}
