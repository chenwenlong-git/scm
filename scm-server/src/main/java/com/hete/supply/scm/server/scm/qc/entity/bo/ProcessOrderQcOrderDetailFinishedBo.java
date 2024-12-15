package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel("加工单质检单详情完成信息")
public class ProcessOrderQcOrderDetailFinishedBo {
    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "正品数")
    private Integer passAmount;

    @ApiModelProperty(value = "次品数")
    private Integer notPassAmount;
}
