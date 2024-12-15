package com.hete.supply.scm.server.supplier.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/24 23:54
 */
@Data
@NoArgsConstructor
public class EmployeeGradeSearchVo {
    @ApiModelProperty(value = "id")
    private Long employeeGradeId;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty(value = "职级类别")
    private GradeType gradeType;


    @ApiModelProperty(value = "职级名称")
    private String gradeName;

    @ApiModelProperty(value = "能力范围")
    private List<ProcessSimpleVo> processSimpleList;

    @ApiModelProperty(value = "职级优先级，数值越大职级越高")
    private BigDecimal gradeLevel;

}
