package com.hete.supply.scm.server.scm.feishu.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/10/31 10:11
 */
@Data
@NoArgsConstructor
public class BfExcDataBo {
    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图片")
    private String imgCode;

    @ApiModelProperty(value = "内容")
    private String content;
}
