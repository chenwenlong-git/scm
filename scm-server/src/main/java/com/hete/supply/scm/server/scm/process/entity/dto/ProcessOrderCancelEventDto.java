package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 取消出库单参数
 *
 * @author zhanzufen
 * @date 2023/3/17
 */
@Data
public class ProcessOrderCancelEventDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "出库类型")
    private WmsEnum.DeliveryType deliveryType;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;


}
