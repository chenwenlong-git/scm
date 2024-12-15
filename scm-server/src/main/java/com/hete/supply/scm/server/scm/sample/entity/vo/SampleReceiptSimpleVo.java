package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleReceiptOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/22 11:07
 */
@Data
@NoArgsConstructor
public class SampleReceiptSimpleVo {
    @ApiModelProperty(value = "样品收货单号")
    private String sampleReceiptOrderNo;


    @ApiModelProperty(value = "样品收货单状态")
    private SampleReceiptOrderStatus receiptOrderStatus;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "总收货数")
    private Integer totalReceipt;
}
