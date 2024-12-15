package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/8/22 15:10
 */
@Data
@NoArgsConstructor
public class ProduceDataItemProcessDescListDto {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述值，多个以英文逗号分开")
    private String descValue;

}
