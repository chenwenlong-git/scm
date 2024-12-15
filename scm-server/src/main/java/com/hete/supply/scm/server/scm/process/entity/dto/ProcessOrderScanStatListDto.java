package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.server.scm.process.enums.ProcessOrderScanFilterType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "工序扫码提成参数", description = "工序扫码提成参数")
public class ProcessOrderScanStatListDto extends ComPageDto {

    @ApiModelProperty(value = "类型")
    private ProcessOrderScanFilterType type;

    @JsonIgnore
    @ApiModelProperty(value = "扫码完成时间-开始时间")
    private LocalDateTime completeTimeStart;

    @JsonIgnore
    @ApiModelProperty(value = "扫码完成时间-结束时间")
    private LocalDateTime completeTimeEnd;

    @JsonIgnore
    @ApiModelProperty(value = "扫码完成人")
    private String completeUser;
}
