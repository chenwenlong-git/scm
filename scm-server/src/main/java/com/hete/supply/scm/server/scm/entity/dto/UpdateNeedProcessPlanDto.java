package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * 无需排产
 *
 * @author yanjiawei
 * @date 2023/07/23 23:49
 */
@Data
@ApiModel(value = "无需排产", description = "无需排产")
public class UpdateNeedProcessPlanDto {

    @ApiModelProperty(value = "加工单号")
    @NotEmpty(message = "加工单号不能为空")
    private List<String> processOrderNos;
}