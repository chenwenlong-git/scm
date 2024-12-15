package com.hete.supply.scm.server.scm.adjust.entity.vo;

import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/6/18 16:00
 */
@Data
@NoArgsConstructor
public class GoodsPriceApproveVo {

    @ApiModelProperty(value = "操作人")
    private String createUser;

    @ApiModelProperty(value = "操作人名称")
    private String createUsername;


    @ApiModelProperty(value = "渠道价格")
    private BigDecimal channelPrice;


    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;

    @ApiModelProperty(value = "生效备注")
    private String effectiveRemark;

    @ApiModelProperty(value = "生效状态")
    private GoodsPriceEffectiveStatus goodsPriceEffectiveStatus;


}
