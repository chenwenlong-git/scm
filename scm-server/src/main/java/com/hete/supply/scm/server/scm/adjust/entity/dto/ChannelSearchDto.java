package com.hete.supply.scm.server.scm.adjust.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/8/28 11:27
 */
@Data
@NoArgsConstructor
public class ChannelSearchDto {

    @ApiModelProperty(value = "状态:启用(TRUE)、禁用(FALSE)")
    private BooleanType channelStatus;

}
