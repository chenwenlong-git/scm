package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yanjiawei
 * Created on 2023/12/28.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "返修单变更消息")
public class RepairOrderChangeDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "返修单号", example = "R2023001", required = true)
    private String repairNo;
}
