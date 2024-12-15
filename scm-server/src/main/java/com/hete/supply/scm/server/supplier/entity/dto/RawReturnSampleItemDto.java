package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/12/6 15:59
 */
@Data
@NoArgsConstructor
public class RawReturnSampleItemDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long sampleChildOrderRawId;

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
