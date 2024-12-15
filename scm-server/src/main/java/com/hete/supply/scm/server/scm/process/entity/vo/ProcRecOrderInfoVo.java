package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2024/7/24.
 */
@Data
public class ProcRecOrderInfoVo {
    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "上架数")
    private Integer actualShelfNum;

    @ApiModelProperty(value = "最晚上架时间")
    private LocalDateTime finishOnShelfTime;
}
