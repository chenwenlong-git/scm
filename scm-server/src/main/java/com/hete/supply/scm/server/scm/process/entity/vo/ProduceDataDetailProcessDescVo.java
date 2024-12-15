package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/10/17 13:47
 */
@Data
@NoArgsConstructor
public class ProduceDataDetailProcessDescVo {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述值")
    private String descValue;

}
