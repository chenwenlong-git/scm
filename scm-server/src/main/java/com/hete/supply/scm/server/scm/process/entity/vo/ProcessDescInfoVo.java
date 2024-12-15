package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工序描述公共VO
 *
 * @author ChenWenLong
 * @date 2024/9/27 14:19
 */
@Data
@NoArgsConstructor
public class ProcessDescInfoVo {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述值，多个以英文逗号分开")
    private String descValue;

}
