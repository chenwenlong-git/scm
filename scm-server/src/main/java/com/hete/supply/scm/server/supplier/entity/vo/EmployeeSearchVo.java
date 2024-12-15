package com.hete.supply.scm.server.supplier.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.server.scm.process.entity.dto.EmployeeRestTimeDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/24 23:54
 */
@Data
@NoArgsConstructor
public class EmployeeSearchVo {
    @ApiModelProperty(value = "id")
    private Long employeeGradeRelationId;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    @ApiModelProperty(value = "职级id")
    private Long employeeGradeId;

    @ApiModelProperty(value = "职级名称")
    private String gradeName;

    @ApiModelProperty(value = "员工编号")
    private String employeeNo;

    @ApiModelProperty(value = "停工时间")
    @JsonProperty("employeeRestTimes")
    private List<EmployeeRestTimeDto> employeeRestTimeList;
}
