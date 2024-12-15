package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/21 20:07
 */
@Data
@NoArgsConstructor
public class PurchaseReturnSimpleVo {
    @ApiModelProperty(value = "采购退货单号")
    private String purchaseReturnOrderNo;

    @ApiModelProperty(value = "退货单状态")
    private ReturnOrderStatus returnOrderStatus;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;
}
