package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/3/11.
 */
@Data
@ApiModel(description = "仓库信息查询请求")
public class QueryProcOrderWarehouseInfoDto {

    @ApiModelProperty(value = "加工单类型")
    @NotNull(message = "加工单类型不能为空")
    private ProcessOrderType processOrderType;
}
