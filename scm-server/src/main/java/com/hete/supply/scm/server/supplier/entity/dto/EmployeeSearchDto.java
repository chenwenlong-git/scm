package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author weiwenxin
 * @date 2023/7/24 23:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EmployeeSearchDto extends ComPageDto {
    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    @ApiModelProperty(value = "员工职级表主键id")
    private Long employeeGradeId;

    @ApiModelProperty(value = "订单类型")
    private ProcessOrderType processOrderType;

    @ApiModelProperty(value = "员工编号")
    private Set<String> employeeNos;

}
