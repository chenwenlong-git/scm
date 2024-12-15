package com.hete.supply.scm.server.scm.adjust.entity.vo;

import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/7/23 15:13
 */
@Data
@NoArgsConstructor
public class GoodsPriceItemSearchListVo {

    @ApiModelProperty(value = "id")
    private Long goodsPriceItemId;

    @ApiModelProperty(value = "版本号")
    private int version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "生效状态")
    private GoodsPriceEffectiveStatus goodsPriceEffectiveStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

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


}
