package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/1/5.
 */
@Data
@ApiModel("返修单下单人用户信息")
public class PlanOrderVo {
    @ApiModelProperty("下单人用户编号")
    private String planUser;

    @ApiModelProperty("下单人用户名称")
    private String planUserName;
}
