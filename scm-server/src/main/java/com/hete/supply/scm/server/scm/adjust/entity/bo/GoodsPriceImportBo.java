package com.hete.supply.scm.server.scm.adjust.entity.bo;

import com.alibaba.nacos.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/6/20 16:49
 */
@Data
@NoArgsConstructor
public class GoodsPriceImportBo {

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "渠道价格")
    private String channelPrice;

    public GoodsPriceImportBo(String channelName, String channelPrice) {
        this.channelName = StringUtils.isNotBlank(channelName) ? channelName.trim() : channelName;
        this.channelPrice = channelPrice;
    }

}
