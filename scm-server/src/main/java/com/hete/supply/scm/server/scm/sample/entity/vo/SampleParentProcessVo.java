package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 样品母单工序
 *
 * @author ChenWenLong
 * @date 2023/5/4 10:54
 */
@Data
@NoArgsConstructor
public class SampleParentProcessVo {

    @ApiModelProperty(value = "二级工序代码")
    private String processSecondCode;

    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty("工序")
    private ProcessLabel processLabel;
}
