package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/10/12 15:55
 */
@Data
@NoArgsConstructor
public class QcDefectConfigIdAndVersionDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long qcOrderDefectConfigId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;
}
