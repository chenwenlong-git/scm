package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/6/17 17:44
 */
@Data
@NoArgsConstructor
public class PurchaseAdjustPriceItemDto {
    @NotBlank(message = "采购订单号不能为空")
    @ApiModelProperty(value = "采购订单号")
    private String purchaseChildOrderNo;

    @NotNull(message = "调整价格（调整后）不能为空")
    @ApiModelProperty(value = "调整价格（调整后）")
    private BigDecimal adjustPrice;

    @Length(max = 255, message = "调价事由长度不能超过255个字符")
    @ApiModelProperty(value = "调价事由")
    private String adjustReason;

    @Length(max = 255, message = "调价备注长度不能超过255个字符")
    @ApiModelProperty(value = "调价备注")
    private String adjustRemark;
}
