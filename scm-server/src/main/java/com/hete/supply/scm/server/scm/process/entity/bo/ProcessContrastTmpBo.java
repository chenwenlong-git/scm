package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/20 18:12
 */
@Data
@NoArgsConstructor
public class ProcessContrastTmpBo {

    @ApiModelProperty(value = "新的二级工序")
    private String newProcessSecondName;

    @ApiModelProperty(value = "旧的二级工序")
    private String oldProcessSecondName;

    @ApiModelProperty(value = "一级工序")
    private String processFirstName;

    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;


}
