package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleReceiptOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/14 00:38
 */
@Data
@NoArgsConstructor
public class SampleReceiptDetailVo {
    @ApiModelProperty(value = "id")
    private Long sampleReceiptOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品收货单号")
    private String sampleReceiptOrderNo;

    @ApiModelProperty(value = "样品收货单状态")
    private SampleReceiptOrderStatus receiptOrderStatus;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "总发货数")
    private Integer totalDeliver;

    @ApiModelProperty(value = "总收货数")
    private Integer totalReceipt;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;

    @ApiModelProperty(value = "收货明细列表")
    private List<SampleReceiptOrderItemVo> sampleReceiptOrderItemList;

    @ApiModelProperty(value = "样品子单明细列表")
    private List<SampleChildOrderItemVo> sampleChildOrderList;
}
