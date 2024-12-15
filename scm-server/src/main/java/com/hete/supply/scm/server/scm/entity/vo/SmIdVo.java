package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/5 10:45
 */
@Data
@NoArgsConstructor
public class SmIdVo {
    @ApiModelProperty(value = "id")
    private Long subsidiaryMaterialId;
}
