package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 员工职级查询实体
 *
 * @author yanjiawei
 * @date 2023/07/28 09:31
 */
@Data
@NoArgsConstructor
public class EmployeePlanCountBo {

    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "已排单数")
    private Integer completePlanCount;
}