package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/1/10 13:51
 */
@Data
@NoArgsConstructor
public class SampleResultNoDto {
    @NotBlank(message = "选样结果单号不能为空")
    @ApiModelProperty(value = "选样结果单号")
    private String sampleResultNo;
}
