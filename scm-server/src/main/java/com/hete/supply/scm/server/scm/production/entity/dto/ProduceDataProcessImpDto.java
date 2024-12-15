package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/11/20.
 */
@Data
@AllArgsConstructor
public class ProduceDataProcessImpDto {
    @ApiModelProperty(value = "工序类别")
    private String process;

    @ApiModelProperty(value = "二级工序名")
    private String processSecond;
}
