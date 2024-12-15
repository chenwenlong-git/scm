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
@ApiModel(description = "获取返修单状态批量查询实体")
public class GetRepairOrderStatusBatchDto {

    @Size(max = 200, message = "计划单号查询列表最大200条")
    @ApiModelProperty(value = "计划单号查询列表", allowableValues = "range[1, 200]")
    private Collection<String> planOrderNos;
}
