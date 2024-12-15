package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/7 16:04
 */
@Data
@NoArgsConstructor
public class DevelopSampleReviewVo {
    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "样品阶段")
    private DevelopSampleMethod developSampleMethod;
}
