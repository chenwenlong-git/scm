package com.hete.supply.scm.server.supplier.entity.vo;

import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverItemVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/14 09:36
 */
@Data
@NoArgsConstructor
public class ShippingMarkItemDetailVo {
    @ApiModelProperty(value = "发货单号")
    private String deliverOrderNo;

    @ApiModelProperty(value = "业务子单单号")
    private String bizChildOrderNo;

    @ApiModelProperty(value = "箱唛箱号（序号）")
    private String shippingMarkNum;

    @ApiModelProperty(value = "箱唛箱号（序号）条形码")
    private String shippingMarkNumBarCode;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "sku详情")
    private List<PurchaseDeliverItemVo> purchaseDeliverItemList;
}
