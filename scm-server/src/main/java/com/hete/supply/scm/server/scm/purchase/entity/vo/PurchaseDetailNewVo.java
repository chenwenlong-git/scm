package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/13 21:08
 */
@Data
@NoArgsConstructor
public class PurchaseDetailNewVo {
    @ApiModelProperty(value = "id")
    private Long purchaseParentOrderId;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "需求对象")
    private SkuType skuType;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseParentOrderStatus purchaseParentOrderStatus;

    @ApiModelProperty(value = "采购需求单明细")
    private List<PurchaseDetailNewItemVo> purchaseDetailNewItemList;

    @ApiModelProperty(value = "采购子单明细")
    private List<PurchaseChildDetailItemVo> purchaseChildDetailItemList;

    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;

    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;
}
