package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/8/15 15:40
 */
@Data
@NoArgsConstructor
public class DevelopOrderPriceVo {

    @ApiModelProperty(value = "关联渠道ID")
    private Long channelId;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

}
