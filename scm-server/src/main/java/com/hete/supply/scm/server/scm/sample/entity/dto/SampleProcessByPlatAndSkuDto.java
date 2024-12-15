package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author RockyHuas
 * @date 2023/6/12 15:45
 */
@Data
@NoArgsConstructor
public class SampleProcessByPlatAndSkuDto {

    @ApiModelProperty(value = "平台")
    @NotBlank(message = "平台不能为空")
    @Length(max = 60, message = "平台字符长度不能超过 60 位")
    private String platform;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;
}
