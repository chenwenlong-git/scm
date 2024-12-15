package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/5/21 16:39
 */
@Data
@NoArgsConstructor
public class RecoOrderTransferItemDto extends RecoOrderNoAndVersionDto {

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("意见")
    private String comment;
}
