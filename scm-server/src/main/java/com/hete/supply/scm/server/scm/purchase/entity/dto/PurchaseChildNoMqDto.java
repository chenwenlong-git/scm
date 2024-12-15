package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/8/9 10:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseChildNoMqDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "采购子单号")
    private List<String> purchaseChildOrderNoList;

    @ApiModelProperty(value = "采购订单透传参数")
    private List<PurchaseChildItemMqDto> purchaseChildItemMqList;
}
