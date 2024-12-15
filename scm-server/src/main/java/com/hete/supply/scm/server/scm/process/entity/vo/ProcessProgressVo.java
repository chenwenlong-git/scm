package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessProgressStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 加工进度
 *
 * @author yanjiawei
 * @date 2023/07/23 18:04
 */
@Data
@NoArgsConstructor
@ApiModel(value = "加工进度", description = "加工进度")
public class ProcessProgressVo {

    @ApiModelProperty(value = "工序名称")
    private String processName;
    @ApiModelProperty(value = "工序状态")
    private ProcessProgressStatus processProgressStatus;
}