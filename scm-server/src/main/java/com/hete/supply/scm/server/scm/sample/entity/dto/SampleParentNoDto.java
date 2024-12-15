package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/2 16:16
 */
@Data
@NoArgsConstructor
public class SampleParentNoDto {
    @NotBlank(message = "样品采购单号不能为空")
    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;
}
