package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yanjiawei
 * Created on 2024/1/11.
 */
@Data
@ApiModel(value = "工序查询参数", description = "工序查询参数")
@EqualsAndHashCode(callSuper = true)
public class ProcessEmployeeQueryRequestDto extends ComPageDto {
    @ApiModelProperty(value = "用户编号")
    private String employeeNo;

    @ApiModelProperty(value = "用户名称")
    private String employeeName;
}
