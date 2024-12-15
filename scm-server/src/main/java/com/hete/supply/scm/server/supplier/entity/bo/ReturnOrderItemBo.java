package com.hete.supply.scm.server.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2023/6/27 15:10
 */
@Data
@NoArgsConstructor
public class ReturnOrderItemBo {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotNull(message = "预计退货数量不能为空")
    @ApiModelProperty(value = "预计退货数量")
    private Integer expectedReturnCnt;

    @NotNull(message = "结算金额不能为空")
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;


    @ApiModelProperty(value = "需扣款金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "发货明细ID")
    private Long bizDetailId;

    @ApiModelProperty(value = "退货单来源单据号")
    private String returnBizNo;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "次品处理单号")
    private String defectHandlingNo;
}
