package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 工序人员排产计划表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_procedure_employee_plan")
@ApiModel(value = "ProcessProcedureEmployeePlanPo对象", description = "工序人员排产计划表")
public class ProcessProcedureEmployeePlanPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_procedure_employee_plan_id", type = IdType.ASSIGN_ID)
    private Long processProcedureEmployeePlanId;

    @ApiModelProperty(value = "产能池编号")
    private String productionPoolCode;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;


    @ApiModelProperty(value = "加工单工序表主键id")
    private Long processOrderProcedureId;

    @ApiModelProperty(value = "工序ID")
    private Long processId;

    @ApiModelProperty(value = "工序名称")
    private String processName;


    @ApiModelProperty(value = "员工编号")
    private String employeeNo;


    @ApiModelProperty(value = "员工名称")
    private String employeeName;


    @ApiModelProperty(value = "工序产能数")
    private Integer processNum;


    @ApiModelProperty(value = "工序提成")
    private BigDecimal commission;

    @ApiModelProperty(value = "原料接货时间")
    private LocalDateTime receiveMaterialTime;

    @ApiModelProperty(value = "预计开始时间")
    private LocalDateTime expectBeginTime;

    @ApiModelProperty(value = "实际开始时间")
    private LocalDateTime actBeginTime;

    @ApiModelProperty(value = "预计结束时间")
    private LocalDateTime expectEndTime;

    @ApiModelProperty(value = "实际结束时间")
    private LocalDateTime actEndTime;
}
