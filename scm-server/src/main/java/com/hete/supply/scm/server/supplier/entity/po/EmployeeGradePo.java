package com.hete.supply.scm.server.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 员工职级表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("employee_grade")
@ApiModel(value = "EmployeeGradePo对象", description = "员工职级表")
public class EmployeeGradePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "employee_grade_id", type = IdType.ASSIGN_ID)
    private Long employeeGradeId;


    @ApiModelProperty(value = "职级类别")
    private GradeType gradeType;


    @ApiModelProperty(value = "职级名称")
    private String gradeName;


    @ApiModelProperty(value = "职级优先级，数值越大职级越高")
    private BigDecimal gradeLevel;


}
