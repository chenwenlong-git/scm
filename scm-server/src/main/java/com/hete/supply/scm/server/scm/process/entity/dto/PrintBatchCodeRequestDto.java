package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/1/5.
 */
@Data
@ApiModel("批次码查询条件")
public class PrintBatchCodeRequestDto {

    @ApiModelProperty(value = "返修单明细ID", required = true)
    private Long repairOrderItemId;

}

