package com.hete.supply.scm.server.scm.purchase.entity.bo;

import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/11/8 16:31
 */
@Data
@NoArgsConstructor
public class SkuStockBo {
    @NotBlank(message = "原料发货仓不能为空")
    @ApiModelProperty(value = "原料发货仓")
    private String rawWarehouseCode;

    @NotNull(message = "不良品类型能为空")
    @ApiModelProperty(value = "良品类型")
    private WmsEnum.ProductQuality productQuality;

    @NotNull(message = "出库类型不能为空")
    @ApiModelProperty(value = "出库类型")
    private WmsEnum.DeliveryType deliveryType;

    @NotEmpty(message = "采购原料列表不能为空")
    @ApiModelProperty(value = "采购原料列表")
    private List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList;

    @NotBlank(message = "平台采购订单关系不能为空")
    @ApiModelProperty(value = "平台采购订单关系")
    private String platform;
}
