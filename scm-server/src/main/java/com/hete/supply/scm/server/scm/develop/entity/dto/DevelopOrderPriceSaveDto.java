package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/8/15 10:30
 */
@Data
@NoArgsConstructor
public class DevelopOrderPriceSaveDto {

    @ApiModelProperty(value = "关联渠道ID")
    @NotNull(message = "渠道ID不能为空")
    private Long channelId;

    @ApiModelProperty(value = "大货价格")
    @NotNull(message = "大货价格不能为空")
    @DecimalMax(value = "99999999.99", message = "大货价格不能超过1亿")
    @Digits(integer = 8, fraction = 2, message = "大货价格小数位数不能超过两位")
    @Positive(message = "大货价格必须大于0")
    private BigDecimal price;

    @ApiModelProperty(value = "默认设置样品价格")
    private BooleanType isDefaultPrice;

}
