package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/3 15:58
 */
@Data
@NoArgsConstructor
public class DevelopPrenatalFirstMsgVo {
    @ApiModelProperty(value = "产前样单号")
    private String prenatalSampleOrderNo;

    @ApiModelProperty(value = "产前样单状态")
    private PurchaseOrderStatus prenatalPurchaseOrderStatus;

    @ApiModelProperty(value = "首单号")
    private String firstSampleOrderNo;

    @ApiModelProperty(value = "首单状态")
    private PurchaseOrderStatus firstPurchaseOrderStatus;

    @ApiModelProperty(value = "产前样采购母单单号")
    private String prenatalPurchaseParentOrderNo;

    @ApiModelProperty(value = "首单采购母单单号")
    private String firstPurchaseParentOrderNo;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "是否支持下单")
    private BooleanType supportOrder;
}
