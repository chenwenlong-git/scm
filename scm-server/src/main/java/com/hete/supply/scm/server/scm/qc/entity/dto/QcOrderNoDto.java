package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/10/10 09:32
 */
@Data
@NoArgsConstructor
public class QcOrderNoDto {
    @NotBlank(message = "质检单号不能为空")
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;
}
