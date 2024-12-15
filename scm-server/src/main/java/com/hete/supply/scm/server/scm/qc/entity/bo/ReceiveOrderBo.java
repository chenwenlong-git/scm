package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2023/10/13.
 */
@Data
@ApiModel(value = "ReceiveOrderBo", description = "收货单信息")
public class ReceiveOrderBo {
    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "入库类型")
    private WmsEnum.ReceiveType receiveType;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "商品/辅料类目")
    private String goodsCategory;

    @ApiModelProperty(value = "是否正常采购")
    private Boolean isNormalOrder;

    @ApiModelProperty(value = "是否供应商直发")
    private Boolean isDirectSend;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收货单状态")
    private WmsEnum.ReceiveOrderState receiveOrderState;

    @ApiModelProperty(value = "供应链唯一单号,scmBizNo相同时区分业务单据")
    private String unionKey;

    @ApiModelProperty(value = "供应链单据号：采购子单号/样品子单号/加工单号/采购发货单号")
    private String scmBizNo;

    @ApiModelProperty(value = "赫特spu")
    private String spu;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "发货物流单号")
    private String trackingNumber;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货人编码")
    private String operator;

    @ApiModelProperty(value = "收货人名称")
    private String operatorName;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;

    @ApiModelProperty(value = "供应商发货时间")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "开始收货时间")
    private LocalDateTime startReceiveTime;

    @ApiModelProperty(value = "完成收货时间")
    private LocalDateTime finishReceiveTime;

    @ApiModelProperty(value = "拒收原因")
    private String rejectReason;

    @ApiModelProperty(value = "收货备注")
    private String remark;

    @ApiModelProperty(value = "质检类型")
    private WmsEnum.QcType qcType;

    @ApiModelProperty(value = "第三方入库单号")
    private String thirdInOrderNo;

    @ApiModelProperty(value = "sku开发类型")
    private WmsEnum.SkuDevType skuDevType;

    @ApiModelProperty(value = "上架时间")
    private LocalDateTime onShelvesTime;
}
