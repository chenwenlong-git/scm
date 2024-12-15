package com.hete.supply.scm.server.supplier.entity.dto;

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
 * @date 2023/1/7 17:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RawReceiptMqDto extends BaseMqMessageDto {
    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号")
    private String relatedOrderNo;

    @NotNull(message = "出库单类型不能为空")
    private WmsEnum.DeliveryType deliveryType;

    @NotBlank(message = "出库单号不能为空")
    @ApiModelProperty(value = "出库单号（采购原料发货单号）")
    private String purchaseRawDeliverOrderNo;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "原料发货明细")
    @Valid
    @NotEmpty(message = "原料发货明细不能为空")
    private List<RawReceiptItemDto> rawReceiptItemList;

    @Data
    @NoArgsConstructor
    public static class RawReceiptItemDto {
        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String skuCode;

        @NotBlank(message = "批次码不能为空")
        @ApiModelProperty(value = "批次码")
        private String skuBatchCode;

        @NotNull(message = "发货数量不能为空")
        @ApiModelProperty(value = "发货数量")
        private Integer deliverCnt;
    }
}
