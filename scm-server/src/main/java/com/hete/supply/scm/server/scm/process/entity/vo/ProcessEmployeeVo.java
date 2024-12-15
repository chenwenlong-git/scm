package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/1/11.
 */
@Data
@ApiModel(value = "加工部员工信息列表")
public class ProcessEmployeeVo {

    @ApiModelProperty(value = "用户编号")
    private String employeeNo;

    @ApiModelProperty(value = "用户名称")
    private String employeeName;
}
