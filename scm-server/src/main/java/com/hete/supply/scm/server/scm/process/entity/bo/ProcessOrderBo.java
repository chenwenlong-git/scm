package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @Author: RockyHuas
 * @date: 2022/11/19 11:46
 */
@Data
public class ProcessOrderBo {

    /**
     * 加工单编号
     */
    @Valid
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作人")
    private String operatorUsername;

    private String traceId;
}
