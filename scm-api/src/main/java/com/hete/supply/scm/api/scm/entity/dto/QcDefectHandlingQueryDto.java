package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2023/12/7.
 */
@Data
public class QcDefectHandlingQueryDto {
    @NotBlank(message = "质检单号不能为空")
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;
}
