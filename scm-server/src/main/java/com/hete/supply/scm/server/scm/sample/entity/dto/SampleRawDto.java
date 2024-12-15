package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/3/30 14:18
 */
@Data
@NoArgsConstructor
public class SampleRawDto {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderRawId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "原料sku不能为空")
    @ApiModelProperty(value = "原料sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotNull(message = "出库数不能为空")
    @ApiModelProperty(value = "出库数")
    @Min(value = 1, message = "出库数不能小于0")
    private Integer deliveryCnt;
}
