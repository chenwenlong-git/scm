package com.hete.supply.scm.server.scm.qc.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/10/30 20:54
 */
@Data
@NoArgsConstructor
public class QcContainerVo {
    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "容器内的货物数量")
    private Integer amount;
}
