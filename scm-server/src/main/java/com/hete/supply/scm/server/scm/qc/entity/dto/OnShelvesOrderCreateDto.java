package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

/**
 * @author yanjiawei
 * Created on 2023/10/13.
 */
@Data
@ApiModel(description = "上架单创建DTO")
public class OnShelvesOrderCreateDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "上架单列表")
    private Collection<OnShelvesOrderDto> onShelvesOrders;
}
