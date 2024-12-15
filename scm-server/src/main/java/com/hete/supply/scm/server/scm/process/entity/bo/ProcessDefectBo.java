package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: RockyHuas
 * @date: 2023/06/28 11:46
 */
@Data
public class ProcessDefectBo {

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "加工次品单号")
    private String defectHandlingNo;


}
