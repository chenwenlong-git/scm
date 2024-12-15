package com.hete.supply.scm.server.supplier.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/3 10:31
 */
@Data
@NoArgsConstructor
public class SampleReturnNoDto {
    @NotBlank(message = "样品退货单号不能为空")
    @ApiModelProperty(value = "样品退货单号")
    private String sampleReturnOrderNo;
}
