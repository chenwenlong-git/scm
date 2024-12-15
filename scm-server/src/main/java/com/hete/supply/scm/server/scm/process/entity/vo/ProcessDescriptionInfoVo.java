package com.hete.supply.scm.server.scm.process.entity.vo;
/**
 * 工序描述信息
 *
 * @author yanjiawei
 * Created on 2023/9/9.
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanjiawei
 * @date 2023年09月09日 17:24
 */
@Data
@NoArgsConstructor
public class ProcessDescriptionInfoVo {
    @ApiModelProperty(value = "加工描述名称")
    private String processDescName;

    @ApiModelProperty(value = "加工描述值")
    private String processDescValue;
}
