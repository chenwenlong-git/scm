package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/10/17 13:48
 */
@Data
@NoArgsConstructor
public class ProduceDataDetailProcessVo {

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "二级工序代码")
    private String processSecondCode;


    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序环节")
    private ProcessFirst processFirst;

    @ApiModelProperty("工序类别")
    private ProcessLabel processLabel;

}
