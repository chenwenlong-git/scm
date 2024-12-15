package com.hete.supply.scm.server.supplier.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/8/17 18:05
 */
@Data
@NoArgsConstructor
public class DevelopPamphletOrderRawItemDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long developPamphletOrderRawId;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;

    @NotBlank(message = "原料sku不能为空")
    @ApiModelProperty(value = "原料sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotNull(message = "归还数不能为空")
    @ApiModelProperty(value = "归还数")
    @Min(value = 1, message = "归还数不能小于0")
    private Integer returnCnt;
}
