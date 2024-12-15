package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/27 16:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReturnOrderMqDto extends BaseMqMessageDto {
    @NotNull(message = "预计退货总数不能为空")
    @ApiModelProperty(value = "预计退货总数")
    private Integer expectedReturnCnt;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @NotNull(message = "退货类型不能为空")
    @ApiModelProperty(value = "退货类型")
    private ReturnType returnType;

    @NotEmpty(message = "退货类型不能为空")
    @ApiModelProperty(value = "退货明细")
    @Valid
    private List<ReturnOrderMqItemDto> purchaseReturnOrderItemDtoList;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "scm操作人编码")
    @NotBlank(message = "scm操作人编码不能为空")
    private String operator;

    @ApiModelProperty(value = "scm操作人名称")
    @NotBlank(message = "scm操作人名称不能为空")
    private String operatorName;

    @NotBlank(message = "仓库编码不能为空")
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @Data
    public static class ReturnOrderMqItemDto {
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

        @NotNull(message = "明细ID不能为空")
        @ApiModelProperty(value = "明细ID")
        private Long bizDetailId;

        @NotBlank(message = "次品处理单号不能为空")
        @ApiModelProperty(value = "次品处理单号")
        private String defectHandlingNo;

        @ApiModelProperty(value = "加工单号，退货类型：加工单独有")
        private String processOrderNo;

        @ApiModelProperty(value = "收货单号")
        private String receiveOrderNo;
    }
}
