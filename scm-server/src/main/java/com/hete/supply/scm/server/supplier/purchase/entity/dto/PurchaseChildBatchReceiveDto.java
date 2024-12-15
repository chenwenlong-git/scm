package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/17 11:01
 */
@Data
@NoArgsConstructor
public class PurchaseChildBatchReceiveDto {
    @ApiModelProperty(value = "是否接单")
    @NotNull(message = "是否接单不能为空")
    private BooleanType isReceived;

    @NotEmpty(message = "采购子单号列表不能为空")
    @ApiModelProperty(value = "采购子单号列表")
    private List<String> purchaseChildOrderNoList;

    @ApiModelProperty(value = "拒绝原因")
    private String refuseRemarks;
}
