package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/12/6 14:32
 */
@Data
@NoArgsConstructor
public class SkuPlatformDto {

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "sku长度不能超过100个字符")
    private String sku;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;
}
