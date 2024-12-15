package com.hete.supply.scm.server.scm.purchase.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.RawDeliverMode;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/10/12 10:01
 */
@Data
@NoArgsConstructor
public class WmsDeliverBo {
    @ApiModelProperty(value = "关联单号")
    private String relatedOrderNo;

    @ApiModelProperty(value = "原料仓库")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "订单出库类型")
    private WmsEnum.DeliveryType deliveryType;

    @ApiModelProperty(value = "订单出库方式")
    private RawDeliverMode rawDeliverMode;

    @ApiModelProperty(value = "是否立即下发")
    private BooleanType dispatchNow;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "收货人")
    private String consignee;
}
