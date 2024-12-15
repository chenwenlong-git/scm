package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/3/22 15:28
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverOrderRawVo {
    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "状态")
    private WmsEnum.DeliveryState deliveryState;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "是否指定库位")
    private BooleanType particularLocation;


    @ApiModelProperty(value = "批次码数量列表")
    private List<ProcessDeliveryOrderVo.DeliveryProduct> productList;
}
