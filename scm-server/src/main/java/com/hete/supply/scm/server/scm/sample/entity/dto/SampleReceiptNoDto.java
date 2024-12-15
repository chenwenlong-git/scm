package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/14 19:34
 */
@Data
@NoArgsConstructor
public class SampleReceiptNoDto {
    @NotBlank(message = "样品收货单号不能为空")
    @ApiModelProperty(value = "样品收货单号")
    private String sampleReceiptOrderNo;
}
