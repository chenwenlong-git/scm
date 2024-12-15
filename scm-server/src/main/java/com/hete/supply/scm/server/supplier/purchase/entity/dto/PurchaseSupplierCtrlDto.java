package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.supply.scm.server.supplier.purchase.enums.PurchaseCtrlType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/17 11:00
 */
@Data
@NoArgsConstructor
public class PurchaseSupplierCtrlDto {
    @ApiModelProperty(value = "供应商采购操作类型")
    @NotNull(message = "供应商采购操作类型不能为空")
    private PurchaseCtrlType purchaseCtrlType;

    @ApiModelProperty(value = "id")
    @NotEmpty(message = "id不能为空")
    private List<Long> purchaseChildOrderIdList;
}
