package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/1/5.
 */
@Data
@ApiModel("批次码查询返回结果")
public class RepairItemBatchCodeVo {
    @ApiModelProperty("返修单明细ID")
    private Long repairOrderItemId;

    @ApiModelProperty("批次码")
    private String batchCode;
}
