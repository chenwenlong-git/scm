package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/6/28.
 */
@ApiModel(description = "查询质检单业务单据信息的请求参数")
@Data
public class BizOrderCreateQcReqDto {

    @ApiModelProperty(value = "业务单据号", example = "BD123456")
    private String outBoundNo;

    @ApiModelProperty(value = "运单号")
    private String expressOrderNo;
}
