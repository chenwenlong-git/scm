package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import com.hete.supply.scm.server.scm.entity.vo.RawReceiveOrderVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/3/20 11:54
 */
@Data
@NoArgsConstructor
public class PurchaseRawMsgVo {
    @ApiModelProperty(value = "原料提供方")
    private RawSupplier rawSupplier;

    @ApiModelProperty(value = "原料sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "单件用量")
    private Integer skuBomCnt;

    @ApiModelProperty(value = "预计消耗数量")
    private Integer exceptConsumeCnt;

    @ApiModelProperty(value = "出库数(需求数)")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "出库数(需求数)")
    private Integer actualDeliveryCnt;

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "分配数量")
    private Integer dispenseCnt;

    @ApiModelProperty(value = "库存变更id")
    private Long supplierInventoryRecordId;

    @ApiModelProperty(value = "收货数（入库数）")
    private Integer receiptCnt;

    @ApiModelProperty(value = "出库单列表(RawSupplier.HETE)")
    private List<PurchaseDeliverOrderRawVo> purchaseDeliverOrderRawList;

    @ApiModelProperty(value = "供应商库存记录(RawSupplier.SUPPLIER)")
    private List<PurchaseInventoryRecordVo> purchaseInventoryRecordList;

    @ApiModelProperty(value = "入库单列表")
    private List<RawReceiveOrderVo.ReceiveOrderVo> purchaseReceiptOrderRawList;
}
