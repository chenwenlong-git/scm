package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.RawReceiptBizType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/28 15:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseRawReceiptSearchDto extends ComPageDto {
    @ApiModelProperty(value = "采购原料收货单号")
    private String purchaseRawReceiptOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeEnd;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeStart;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeEnd;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "收货单状态")
    private List<ReceiptOrderStatus> receiptOrderStatusList;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "采购原料发货单号")
    private String purchaseRawDeliverOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "原料收货类型")
    private RawReceiptBizType rawReceiptBizType;

    @ApiModelProperty(value = "样品子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "sku批次码批量")
    private List<String> skuBatchCodeList;

    @ApiModelProperty(value = "采购原料收货单号批量")
    private List<String> purchaseRawReceiptOrderNoList;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "采购子单单号批量")
    private List<String> purchaseChildOrderNoList;

    @ApiModelProperty(value = "原料收货类型批量(前端忽略该参数)")
    private List<RawReceiptBizType> rawReceiptBizTypeList;
}
