package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * sku生产周期
 *
 * @author yanjiawei
 * Created on 2024/8/12.
 */
@Data
@NoArgsConstructor
public class SkuCycleBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "周期天数")
    private BigDecimal cycleDays = BigDecimal.ZERO;

    public long roundUp() {
        // 使用 BigDecimal 的 setScale 方法和 RoundingMode.CEILING 进行向上取整
        BigDecimal roundedValue = this.cycleDays.setScale(0, RoundingMode.CEILING);
        // 将结果转换为 long 类型并返回
        return roundedValue.longValue();
    }
}
