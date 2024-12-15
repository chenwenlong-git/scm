package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "加工描述查询参数", description = "加工描述查询参数")
public class ProcessDescQueryDto extends ComPageDto {

    @ApiModelProperty("工序环节")
    private ProcessFirst processFirst;

    @ApiModelProperty("加工描述")
    private String name;

}
