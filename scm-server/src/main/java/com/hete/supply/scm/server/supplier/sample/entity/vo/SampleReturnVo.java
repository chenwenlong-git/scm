package com.hete.supply.scm.server.supplier.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/3 10:29
 */
@Data
@NoArgsConstructor
public class SampleReturnVo {
    @ApiModelProperty(value = "样品退货单号")
    private String sampleReturnOrderNo;

    @ApiModelProperty(value = "退货单状态")
    private ReceiptOrderStatus returnOrderStatus;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;


    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;


    @ApiModelProperty(value = "收货人id")
    private String receiptUser;


    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;
}
