package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
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
public class DeliveryOrderCancelEventDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "出库类型")
    private WmsEnum.DeliveryType deliveryType;


}
