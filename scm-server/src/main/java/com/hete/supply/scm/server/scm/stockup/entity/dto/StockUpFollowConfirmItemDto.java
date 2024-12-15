package com.hete.supply.scm.server.scm.stockup.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/1/9 20:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StockUpFollowConfirmItemDto extends StockUpOrderNoDto {
    @NotNull(message = "备货单价不能为空")
    @ApiModelProperty(value = "备货单价")
    private BigDecimal stockUpPrice;

    @Length(max = 255, message = "跟单备注长度不能超过255个字符")
    @ApiModelProperty(value = "跟单备注")
    private String followRemark;
}
