package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/10/30 18:35
 */
@Data
@NoArgsConstructor
public class DevelopAndSampleNoVo {
    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;
}
