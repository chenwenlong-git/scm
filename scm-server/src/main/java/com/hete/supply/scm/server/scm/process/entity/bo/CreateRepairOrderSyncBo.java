package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/1/8.
 */
@Data
public class CreateRepairOrderSyncBo {
    @ApiModelProperty(value = "计划单号")
    private String planNo;
    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;
}
