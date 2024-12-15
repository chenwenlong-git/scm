package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/30 10:22
 */
@Data
@NoArgsConstructor
public class DevelopSampleNoListVo {
    @ApiModelProperty(value = "样品单号")
    private List<String> developSampleOrderNoList;

}
