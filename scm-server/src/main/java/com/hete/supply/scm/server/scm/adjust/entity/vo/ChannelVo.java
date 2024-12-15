package com.hete.supply.scm.server.scm.adjust.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/6/18 18:11
 */
@Data
@NoArgsConstructor
public class ChannelVo {

    @ApiModelProperty(value = "id")
    private Long channelId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "状态")
    private BooleanType channelStatus;

}
