package com.hete.supply.scm.server.supplier.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.server.scm.process.entity.dto.EmployeeRestTimeDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/27 16:45
 */
@Data
@NoArgsConstructor
public class EmployEditDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long employeeGradeRelationId;

    @NotNull(message = "版本号不能为空")
    @ApiModelProperty("版本号")
    private Integer version;

    @NotNull(message = "职级id不能为空")
    @ApiModelProperty(value = "职级id")
    private Long employeeGradeId;

    @ApiModelProperty(value = "停工时间")
    @JsonProperty("employeeRestTimes")
    private List<EmployeeRestTimeDto> employeeRestTimeList;
}
