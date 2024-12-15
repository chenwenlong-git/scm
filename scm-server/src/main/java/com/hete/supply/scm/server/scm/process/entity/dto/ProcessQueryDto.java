package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
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
@ApiModel(value = "加工工序查询参数", description = "加工工序查询参数")
public class ProcessQueryDto extends ComPageDto {
    @ApiModelProperty(value = "工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序类别")
    private ProcessLabel processLabel;

    @ApiModelProperty("状态，ENABLED：启用、DISABLED：禁用")
    private ProcessStatus processStatus;

    @ApiModelProperty(value = "工序环节")
    private ProcessFirst processFirst;

    @ApiModelProperty(value = "工序类型")
    private ProcessType processType;
}
