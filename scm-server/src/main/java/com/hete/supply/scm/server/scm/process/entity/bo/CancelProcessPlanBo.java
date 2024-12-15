package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * 工序人员时间
 *
 * @author yanjiawei
 * @date 2023/07/31 07:07
 */
@Data
@ApiModel(value = "取消排产参数", description = "取消排产参数")
@AllArgsConstructor
public class CancelProcessPlanBo {
    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;
}