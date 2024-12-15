package com.hete.supply.scm.server.scm.adjust.entity.vo;

import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
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
public class GoodsPriceGetPurchaseListVo {


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "关联渠道ID")
    private Long channelId;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "渠道价格")
    private BigDecimal channelPrice;

    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;

    @ApiModelProperty(value = "生效备注")
    private String effectiveRemark;

    @ApiModelProperty(value = "设置通用")
    private GoodsPriceUniversal goodsPriceUniversal;

}
