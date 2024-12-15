package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/20 18:12
 */
@Data
@NoArgsConstructor
public class OperatorUserBo {

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作人")
    private String operatorUsername;


}
