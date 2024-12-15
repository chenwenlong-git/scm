package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 员工职级查询实体
 *
 * @author yanjiawei
 * @date 2023/07/28 07:29
 */
@Data
@NoArgsConstructor
public class EmployeeLevelBo {

    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工等级（数值越大等级越高）")
    private BigDecimal gradeLevel;
    @ApiModelProperty(value = "职级类别")
    private GradeType gradeType;
    @ApiModelProperty(value = "职级名称")
    private String gradeName;
}