package com.hete.supply.scm.server.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 员工职级工序能力关系表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("employee_grade_process")
@ApiModel(value = "EmployeeGradeProcessPo对象", description = "员工职级工序能力关系表")
public class EmployeeGradeProcessPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "employee_grade_process_id", type = IdType.ASSIGN_ID)
    private Long employeeGradeProcessId;


    @ApiModelProperty(value = "职级id")
    private Long employeeGradeId;


    @ApiModelProperty(value = "工序id")
    private Long processId;


    @ApiModelProperty(value = "工序名称")
    private String processName;


    @ApiModelProperty(value = "工序产能数")
    private Integer processNum;


}
