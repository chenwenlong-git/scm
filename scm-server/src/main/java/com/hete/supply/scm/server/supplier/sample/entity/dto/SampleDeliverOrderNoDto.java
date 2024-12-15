package com.hete.supply.scm.server.supplier.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/3 11:58
 */
@Data
@NoArgsConstructor
public class SampleDeliverOrderNoDto {
    @NotBlank(message = "样品发货单号不能为空")
    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;
}
