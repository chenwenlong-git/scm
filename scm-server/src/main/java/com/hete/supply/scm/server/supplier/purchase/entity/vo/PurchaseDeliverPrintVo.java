package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import com.hete.supply.scm.server.supplier.entity.vo.PrintDeliverPrintItemVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/14 22:29
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverPrintVo {
    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @ApiModelProperty(value = "箱数")
    private Integer boxCnt;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "打印人")
    private String printUsername;

    @ApiModelProperty(value = "发货明细")
    private List<PrintDeliverPrintItemVo> deliverPrintItemList;
}
