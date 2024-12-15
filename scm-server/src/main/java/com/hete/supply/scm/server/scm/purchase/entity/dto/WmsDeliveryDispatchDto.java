package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/8/12 11:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WmsDeliveryDispatchDto extends BaseMqMessageDto {
    @ApiModelProperty(name = "关联单号")
    private String relatedOrderNo;

    @ApiModelProperty(name = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(name = "出库单类型")
    private WmsEnum.DeliveryType deliveryType;

    @ApiModelProperty(name = "操作人")
    private String operator;

    @ApiModelProperty(name = "操作人")
    private String operatorName;
}
