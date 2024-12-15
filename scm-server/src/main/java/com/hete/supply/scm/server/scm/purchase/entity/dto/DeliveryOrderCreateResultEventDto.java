package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.RawDeliverMode;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.WmsReturnCode;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/3/21 15:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeliveryOrderCreateResultEventDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "关联单号，如采购子单号")
    private String relatedOrderNo;

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "出库类型")
    private WmsEnum.DeliveryType deliveryType;

    @ApiModelProperty(value = "错误码")
    private WmsReturnCode code;

    @ApiModelProperty(value = "错误提示")
    private String message;

    @ApiModelProperty(value = "原料出库方式")
    private RawDeliverMode rawDeliverMode;

    @ApiModelProperty(value = "指定库位")
    private BooleanType particularLocation;
}
