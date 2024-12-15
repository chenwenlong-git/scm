package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ChenWenLong
 * @date 2023/3/13 17:47
 */
@Data
@ApiModel(value = "查询工序列表", description = "工序列表参数")
public class ProcessQueryListDto {
    @ApiModelProperty("工序")
    private ProcessLabel processLabel;

}
