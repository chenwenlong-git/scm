package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Data
@ApiModel(description = "获取返修单加工结果批量查询DTO")
public class GetRepairOrderProcessResultInfoDto {

    @ApiModelProperty(value = "返修单号查询列表", allowableValues = "range[1, 100]")
    private String repairOrderNo;
}
