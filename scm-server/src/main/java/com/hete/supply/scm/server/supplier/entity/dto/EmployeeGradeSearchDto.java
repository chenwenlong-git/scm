package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/7/24 23:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EmployeeGradeSearchDto extends ComPageDto {
    @ApiModelProperty(value = "职级类别")
    private GradeType gradeType;


}
