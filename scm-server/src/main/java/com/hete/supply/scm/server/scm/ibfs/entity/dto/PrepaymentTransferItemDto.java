package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/5/20 14:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PrepaymentTransferItemDto extends PrepaymentNoDto {
    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("意见")
    private String comment;
}
