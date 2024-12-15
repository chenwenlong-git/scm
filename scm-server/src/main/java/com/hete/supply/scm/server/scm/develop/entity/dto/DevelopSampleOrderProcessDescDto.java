package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/4 13:58
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderProcessDescDto {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述值，多个以英文逗号分开")
    private String descValue;


}
