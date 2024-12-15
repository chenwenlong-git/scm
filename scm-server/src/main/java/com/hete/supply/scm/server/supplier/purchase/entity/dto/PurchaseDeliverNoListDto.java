package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/10 09:19
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverNoListDto {
    @ApiModelProperty(value = "发货单号列表")
    @Valid
    @NotEmpty(message = "发货单号列表不能为空")
    private List<String> purchaseDeliverOrderNoList;
}
