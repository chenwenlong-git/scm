package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 规格书平台
 *
 * @author yanjiawei
 * Created on 2024/11/5.
 */
@Data
public class SpecPlatformVo {
    @ApiModelProperty(value = "平台编码")
    private String platformCode;

    @ApiModelProperty(value = "平台名称")
    private String platformName;
}
