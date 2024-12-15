package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/18 16:13
 */
@Data
@NoArgsConstructor
public class SmProductDto {
    @ApiModelProperty(value = "id")
    private Long smProductId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku不能为空")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    private Integer count;
}
