package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/8/7 18:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseWmsCreateDto extends BaseMqMessageDto {
    @Valid
    @NotEmpty(message = "wms下单列表不能为空")
    private List<PurchaseWmsCreateItemDto> purchaseWmsCreateItemList;
}
