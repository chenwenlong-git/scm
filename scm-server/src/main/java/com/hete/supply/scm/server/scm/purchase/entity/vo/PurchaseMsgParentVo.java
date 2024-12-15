package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/13 14:05
 */
@Data
@NoArgsConstructor
public class PurchaseMsgParentVo {
    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseParentOrderStatus purchaseParentOrderStatus;

    @ApiModelProperty(value = "采购总数")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "采购sku数量列表")
    private List<PurchaseSkuCntVo> purchaseSkuCntList;

    @Data
    public static class PurchaseSkuCntVo {
        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "采购数")
        private Integer purchaseCnt;
    }
}
