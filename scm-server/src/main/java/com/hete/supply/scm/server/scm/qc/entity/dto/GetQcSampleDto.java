package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2024/11/1.
 */
@Data
public class GetQcSampleDto {
    @NotBlank(message = "质检单号不能为空")
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;
}
