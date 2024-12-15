package com.hete.supply.scm.server.supplier.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/22 11:40
 */
@Data
@NoArgsConstructor
public class SampleReturnSimpleVo {
    @ApiModelProperty(value = "样品退货单号")
    private String sampleReturnOrderNo;


    @ApiModelProperty(value = "退货单状态")
    private ReceiptOrderStatus returnOrderStatus;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "物流")
    private String logistics;
}
