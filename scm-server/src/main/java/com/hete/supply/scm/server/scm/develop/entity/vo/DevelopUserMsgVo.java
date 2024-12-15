package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/3 16:02
 */
@Data
@NoArgsConstructor
public class DevelopUserMsgVo {
    @ApiModelProperty(value = "开发人")
    private String devUser;


    @ApiModelProperty(value = "开发人")
    private String devUsername;


    @ApiModelProperty(value = "跟单人")
    private String followUser;


    @ApiModelProperty(value = "跟单人")
    private String followUsername;


    @ApiModelProperty(value = "审版人")
    private String reviewUser;


    @ApiModelProperty(value = "审版人")
    private String reviewUsername;


    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUser;


    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUsername;
}
