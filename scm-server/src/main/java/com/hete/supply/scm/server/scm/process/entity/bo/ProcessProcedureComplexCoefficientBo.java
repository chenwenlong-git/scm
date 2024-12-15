package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 加工单产能系数
 *
 * @author yanjiawei
 * @date 2023/07/31 19:55
 */
@Data
@NoArgsConstructor
public class ProcessProcedureComplexCoefficientBo {

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;
    @ApiModelProperty(value = "复杂系数")
    private Integer complexCoefficient;
}