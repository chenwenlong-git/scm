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
 * 员工职级关系表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("employee_grade_relation")
@ApiModel(value = "EmployeeGradeRelationPo对象", description = "员工职级关系表")
public class EmployeeGradeRelationPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "employee_grade_relation_id", type = IdType.ASSIGN_ID)
    private Long employeeGradeRelationId;


    @ApiModelProperty(value = "员工编号")
    private String employeeNo;


    @ApiModelProperty(value = "员工名称")
    private String employeeName;


    @ApiModelProperty(value = "员工职级表主键id")
    private Long employeeGradeId;


}
