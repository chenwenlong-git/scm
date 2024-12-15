package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:56
 */
@Data
@NoArgsConstructor
public class MonthGoodsPaymentVo {
    @ApiModelProperty(value = "本月货款")
    private BigDecimal monthGoodsPayment;
}
