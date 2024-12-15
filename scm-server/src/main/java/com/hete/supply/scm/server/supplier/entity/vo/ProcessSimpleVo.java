package com.hete.supply.scm.server.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/7/31 09:25
 */
@Data
@NoArgsConstructor
public class ProcessSimpleVo {
    @ApiModelProperty(value = "工序id")
    private Long processId;

    @ApiModelProperty(value = "工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序产能数")
    private Integer processNum;
}
