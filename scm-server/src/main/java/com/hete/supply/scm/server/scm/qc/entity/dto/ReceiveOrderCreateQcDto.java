package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(description = "收货单创建质检单实体")
public class ReceiveOrderCreateQcDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "收货单质检订单列表")
    private List<@Valid ReceiveOrderQcOrderDto> qcOrders;
}
