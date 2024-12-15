package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/5/5 11:08
 */
@Data
@NoArgsConstructor
public class ReceiveOrderForScmBo {

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "入库类型")
    private WmsEnum.ReceiveType receiveType;

    @ApiModelProperty(value = "收货单状态")
    private WmsEnum.ReceiveOrderState receiveOrderState;

    @ApiModelProperty(value = "供应链单据号")
    private String scmBizNo;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "仓库编码")
    private String trackingNumber;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "sku")
    private String skuCode;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryAmount;

    @ApiModelProperty(value = "收货数量")
    private Integer receiveAmount;

    @ApiModelProperty(value = "拒收数量")
    private Integer rejectAmount;

    @ApiModelProperty(value = "已上架数量")
    private Integer onShelvesAmount;

}
