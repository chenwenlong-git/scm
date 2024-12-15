package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/9/27 15:25
 */
@Data
@NoArgsConstructor
public class ProcessDescInfoDto {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述值，多个以英文逗号分开")
    private String descValue;

}
