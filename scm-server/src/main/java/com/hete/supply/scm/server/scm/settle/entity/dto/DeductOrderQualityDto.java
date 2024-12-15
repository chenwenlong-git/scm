package com.hete.supply.scm.server.scm.settle.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DeductOrderPurchaseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2022/12/30 11:53
 */
@Data
@NoArgsConstructor
public class DeductOrderQualityDto {

    @NotNull(message = "单据类型不能为空")
    @ApiModelProperty(value = "单据类型")
    private DeductOrderPurchaseType deductOrderPurchaseType;

    @NotBlank(message = "单据号不能为空")
    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @NotBlank(message = "spu不能为空")
    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "数量")
    private Integer skuNum;

    @NotNull(message = "扣款金额不能为空")
    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;

    @NotNull(message = "结算金额不能为空")
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @NotBlank(message = "扣款备注不能为空")
    @ApiModelProperty(value = "扣款备注")
    private String deductRemarks;


}

