package com.hete.supply.scm.server.scm.settle.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleItemType;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseChildOrderChangeSearchVo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/6/6 18:32
 */
@Data
@NoArgsConstructor
public class PatrolPurchaseSettleOrderBo {

    @ApiModelProperty(value = "异常数据记录")
    private List<PurchaseSettleItemType> purchaseSettleItemTypeList;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "结算单详情列表")
    private List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList;

    @ApiModelProperty(value = "采购单详情列表")
    private List<PurchaseChildOrderChangeSearchVo> purchaseChildOrderChangeSearchVoList;

    @ApiModelProperty(value = "补款单详情列表")
    private List<SupplementOrderPo> supplementOrderPoList;

    @ApiModelProperty(value = "扣款单详情列表")
    private List<DeductOrderPo> deductOrderPoList;

    @ApiModelProperty(value = "采购发货单详情列表")
    private List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList;

}
