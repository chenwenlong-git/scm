package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/5/16 15:45
 */
@Data
@NoArgsConstructor
public class SampleProcessBySkuDto {

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotNull(message = "加工数不能为空")
    @ApiModelProperty(value = "加工数")
    @Min(value = 1, message = "加工数不能小于0")
    private Integer processNum;

    @ApiModelProperty(value = "发货仓库编码")
    @NotBlank(message = "发货仓库编码不能为空")
    private String warehouseCode;

}
