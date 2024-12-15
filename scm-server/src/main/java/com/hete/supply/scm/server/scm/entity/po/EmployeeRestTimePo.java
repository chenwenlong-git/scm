package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 员工休息时间表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("employee_rest_time")
@ApiModel(value = "EmployeeRestTimePo对象", description = "员工休息时间表")
public class EmployeeRestTimePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "employee_rest_time_id", type = IdType.ASSIGN_ID)
    private Long employeeRestTimeId;


    @ApiModelProperty(value = "员工编号")
    private String employeeNo;


    @ApiModelProperty(value = "员工名称")
    private String employeeName;


    @ApiModelProperty(value = "休息开始时间")
    private LocalDateTime restStartTime;


    @ApiModelProperty(value = "休息结束时间")
    private LocalDateTime restEndTime;


    @ApiModelProperty(value = "创建人名称")
    private String createUsername;


    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;


}
