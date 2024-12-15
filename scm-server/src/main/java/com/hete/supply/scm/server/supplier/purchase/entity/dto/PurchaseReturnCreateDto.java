package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/14 14:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseReturnCreateDto extends BaseMqMessageDto {
    @NotBlank(message = "采购退货单号不能为空")
    @ApiModelProperty(value = "采购退货单号")
    private String purchaseReturnOrderNo;

    @NotNull(message = "退货类型不能为空")
    @ApiModelProperty(value = "退货类型：收货拒收、质检不合格、其他")
    private WmsEnum.ReturnType returnType;

    @NotBlank(message = "物流不能为空")
    @ApiModelProperty(value = "物流")
    private String logistics;

    @NotBlank(message = "运单号不能为空")
    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @NotNull(message = "退货数不能为空")
    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作人")
    private String operatorName;

    @ApiModelProperty(value = "采购退货明细列表")
    @Valid
    @NotEmpty(message = "采购退货明细列表不能为空")
    private List<PurchaseReturnCreateItemDto> purchaseReturnCreateItemList;

    @Data
    public static class PurchaseReturnCreateItemDto {
        @NotBlank(message = "采购退货单号不能为空")
        @ApiModelProperty(value = "采购退货单号")
        private String purchaseReturnOrderNo;

        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;

        @NotBlank(message = "sku批次码不能为空")
        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;

        @NotBlank(message = "采购子单单号不能为空")
        @ApiModelProperty(value = "采购子单单号")
        private String scmBizNo;

        @NotNull(message = "退货数不能为空")
        @ApiModelProperty(value = "退货数量")
        private Integer returnCnt;
    }
}
