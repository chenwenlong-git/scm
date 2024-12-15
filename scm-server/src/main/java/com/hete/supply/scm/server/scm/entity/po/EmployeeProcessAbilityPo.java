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
 * 人员工序产能信息表(产能池)
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("employee_process_ability")
@ApiModel(value = "EmployeeProcessAbilityPo对象", description = "工序人员产能信息表(产能池)")
public class EmployeeProcessAbilityPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "employee_process_ability_id", type = IdType.ASSIGN_ID)
    private Long employeeProcessAbilityId;

    @ApiModelProperty(value = "产能池编号")
    private String productionPoolCode;

    @ApiModelProperty(value = "员工编号")
    private String employeeNo;


    @ApiModelProperty(value = "员工名称")
    private String employeeName;


    @ApiModelProperty(value = "工序 ID")
    private Long processId;


    @ApiModelProperty(value = "工序名称")
    private String processName;


    @ApiModelProperty(value = "总加工数量")
    private Integer totalProcessedNum;


    @ApiModelProperty(value = "可加工数量")
    private Integer availableProcessedNum;

    @ApiModelProperty(value = "产能池有效期截止时间")
    private LocalDateTime validityTime;
}
